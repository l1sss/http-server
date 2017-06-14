package ru.ifmo.server;

import java.nio.file.Files;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ifmo.server.util.Utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ru.ifmo.server.Http.*;
import static ru.ifmo.server.util.Utils.htmlMessage;

/**
 * Ifmo Web Server.
 * <p>
 * To start server use {@link #start(ServerConfig)} and register at least
 * one handler to process HTTP requests.
 * Usage example:
 * <pre>
 * {@code
 * ServerConfig config = new ServerConfig()
 *      .addHandler("/index", new Handler() {
 *          public void handle(Request request, Response response) throws Exception {
 *              Writer writer = new OutputStreamWriter(response.getOutputStream());
 *              writer.write(Http.OK_HEADER + "Hello World!");
 *              writer.flush();
 *          }
 *      });
 *
 * Server server = Server.start(config);
 *      }
 *     </pre>
 * </p>
 * <p>
 * To stop the server use {@link #stop()} or {@link #close()} methods.
 * </p>
 *
 * @see ServerConfig
 */
public class Server implements Closeable {
    private static final char LF = '\n';
    private static final char CR = '\r';
    private static final String CRLF = "" + CR + LF;
    private static final char AMP = '&';
    private static final char EQ = '=';
    private static final char HEADER_VALUE_SEPARATOR = ':';
    private static final char SPACE = ' ';
    private static final int READER_BUF_SIZE = 1024;

    private final ServerConfig config;

    private ServerSocket socket;

    private ExecutorService sockProcessorPool;

    private ExecutorService acceptorPool;

    private static final Logger LOG = LoggerFactory.getLogger(Server.class);

    private Server(ServerConfig config) {
        if (config.filters == null || config.filters.length == 0)
            config.firstFilter = new TailFilter();
        else {
            config.firstFilter = config.filters[0];

            for (int i = 1; i < config.filters.length; i++)
                config.filters[i - 1].setNextFilter(config.filters[i]);

            config.filters[config.filters.length - 1].setNextFilter(new TailFilter());
        }

        this.config = new ServerConfig(config);
    }

    /**
     * Starts server according to config. If null passed
     * defaults will be used.
     *
     * @param config Server config or null.
     * @return Server instance.
     * @see ServerConfig
     */
    public static Server start(ServerConfig config) {
        if (config == null)
            config = new ServerConfig();

        try {
            if (LOG.isDebugEnabled())
                LOG.debug("Starting server with config: {}", config);

            Server server = new Server(config);

            server.openConnection();
            server.startPools();

            LOG.info("Server started on port: {}", config.getPort());
            return server;
        } catch (IOException e) {
            throw new ServerException("Cannot start server on port: " + config.getPort());
        }
    }

    private void openConnection() throws IOException {
        socket = new ServerSocket(config.getPort());
    }

    private void startPools() {
        acceptorPool = Executors.newSingleThreadExecutor(new ServerThreadFactory("con-acceptor"));
        sockProcessorPool = Executors.newCachedThreadPool(new ServerThreadFactory("exec-handler"));

        acceptorPool.execute(new ConnectionHandler());
    }

    /**
     * Stops the server.
     */
    public void stop() {
        acceptorPool.shutdownNow();
        sockProcessorPool.shutdownNow();
        Utils.closeQuiet(socket);

        socket = null;
    }

    private void processConnection(Socket sock) throws IOException {
        if (LOG.isDebugEnabled())
            LOG.debug("Accepting connection on: {}", sock);

        Request req;

        try {
            req = parseRequest(sock);

            if (LOG.isDebugEnabled())
                LOG.debug("Parsed request: {}", req);
        } catch (URISyntaxException e) {
            if (LOG.isDebugEnabled())
                LOG.error("Malformed URL", e);

            respond(SC_BAD_REQUEST, "Malformed URL", htmlMessage(SC_BAD_REQUEST + " Malformed URL"),
                    sock.getOutputStream());

            return;
        } catch (Exception e) {
            LOG.error("Error parsing request", e);

            respond(SC_SERVER_ERROR, "Server Error", htmlMessage(SC_SERVER_ERROR + " Server error"),
                    sock.getOutputStream());

            return;
        }

        if (!isMethodSupported(req.method)) {
            respond(SC_NOT_IMPLEMENTED, "Not Implemented", htmlMessage(SC_NOT_IMPLEMENTED + " Method \""
                    + req.method + "\" is not supported"), sock.getOutputStream());

            return;
        }

        Response response = new Response();

        try {
            config.firstFilter.doFilter(req, response);
        } catch (Exception e) {
            if (LOG.isDebugEnabled())
                LOG.error("Server error:", e);

            respond(SC_SERVER_ERROR, "Server Error", htmlMessage(SC_SERVER_ERROR + " Server error"),
                    sock.getOutputStream());

            return;
        }

        responseToClient(response, sock.getOutputStream());
    }


    private void responseToClient(Response response, OutputStream out) throws IOException {
        try {
            if (response.statusCode == 0)
                response.statusCode = SC_OK;

            if (response.printWriter != null)
                response.printWriter.flush();

            if (response.bufOut != null && response.getContentLength() == null)
                response.setHeader(CONTENT_LENGTH, String.valueOf(response.bufOut.size()));

            out.write(("HTTP/1.0" + SPACE + response.statusCode + CRLF).getBytes());

            for (Map.Entry<String, String> entry : response.headers.entrySet())
                out.write((entry.getKey() + ":" + SPACE + entry.getValue() + CRLF).getBytes());

            out.write(CRLF.getBytes());

            if (response.bufOut != null)
                out.write(response.bufOut.toByteArray());

            out.flush();

        } catch (IOException e) {
            throw new ServerException("Cannot get output stream", e);
        }
    }

    private Request parseRequest(Socket socket) throws IOException, URISyntaxException {
        InputStream in = socket.getInputStream();

        Request req = new Request(socket);
        StringBuilder sb = new StringBuilder(READER_BUF_SIZE); // TODO

        while (readLine(in, sb) > 0) {
            if (req.method == null)
                parseRequestLine(req, sb);
            else
                parseHeader(req, sb);

            sb.setLength(0);
        }

        if ((req.getMethod() == HttpMethod.POST || req.getMethod() == HttpMethod.PUT) && req.getBody().contentPresent()) {
            if (req.getBody().getContentType().contains("text"))
                parseTxtBody(in, sb, req);
            else
                parseBinBody(in, req);
        }

        return req;
    }

    private void parseRequestLine(Request req, StringBuilder sb) throws URISyntaxException {
        int start = 0;
        int len = sb.length();

        for (int i = 0; i < len; i++) {
            if (sb.charAt(i) == SPACE) {
                if (req.method == null)
                    req.method = HttpMethod.valueOf(sb.substring(start, i)); // Parse method
                else if (req.path == null) {
                    req.path = new URI(sb.substring(start, i)); // Parse path

                    break; // Ignore protocol for now
                }

                start = i + 1;
            }
        }

        assert req.method != null : "Request method can't be null";
        assert req.path != null : "Request path can't be null";

        String query = req.path.getQuery();

        if (query != null) {
            start = 0;

            String key = null;
            String value = null;

            for (int i = 0; i < query.length(); i++) {
                boolean last = i == query.length() - 1;

                if (key == null && query.charAt(i) == EQ) {
                    key = query.substring(start, i);

                    start = i + 1;
                } else if (key != null && (query.charAt(i) == AMP || last)) {
                    value = query.substring(start, last ? i + 1 : i);
                    if (value.equals("")) value = null;
                    req.addArgument(key, value);

                    key = null;
                    start = i + 1;
                }
            }

            if (key != null)
                req.addArgument(key, null);
        }
    }

    private void parseHeader(Request req, StringBuilder sb) {
        String key = null;

        int start = 0;
        int len = sb.length();

        for (int i = 0; i < len; i++) {
            if (sb.charAt(i) == HEADER_VALUE_SEPARATOR) {
                key = sb.substring(start, i).trim();

                start = i + 1;

                break;
            }
        }

        req.addHeader(key, sb.substring(start, len).trim());
    }

    private void parseTxtBody(InputStream in, StringBuilder sb, Request request) throws IOException {
        InputStreamReader reader = new InputStreamReader(in);

        int contentLength = request.getBody().getContentLength();

        char[] buf = new char[1024];
        int len;
        int count = 0;

        while ((len = reader.read(buf)) > 0) {
            sb.append(new String(buf, 0, len));

            count += len;
            if (count == contentLength)
                break;
        }

        request.getBody().txtContent = sb.toString();
    }

    private void parseBinBody(InputStream in, Request request) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        int contentLength = request.getBody().getContentLength();

        byte[] buf = new byte[1024];
        int len;
        int count = 0;

        while ((len = in.read(buf)) > 0) {
            bout.write(buf, 0, len);

            count += len;
            if (count == contentLength)
                break;
        }

        request.getBody().binContent = bout.toByteArray();
    }

    private int readLine(InputStream in, StringBuilder sb) throws IOException {
        int c;
        int count = 0;

        while ((c = in.read()) >= 0) {
            if (c == LF) {
                break;
            }

            sb.append((char) c);
            count++;
        }

        if (count > 0 && sb.charAt(count - 1) == CR)
            sb.setLength(--count);

        if (LOG.isTraceEnabled())
            LOG.trace("Read line: {}", sb.toString());

        return count;
    }

    private void respond(int code, String statusMsg, String content, OutputStream out) throws IOException {
        String errorPage = FindErrors.findErrorPage(code, this.config);
        if (errorPage != null) {
            respond(errorPage, out);
            return;
        }
        out.write(("HTTP/1.0" + SPACE + code + SPACE + statusMsg + CRLF + CRLF + content).getBytes());
        out.flush();
    }

    private void respond(String path, OutputStream out) {
        try (FileInputStream input = new FileInputStream(new File(path))) {
            byte[] buffer = new byte[1024];
            int read;

            while ((read = input.read(buffer)) > 0) {
                out.write(buffer, 0, read);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Invokes {@link #stop()}. Usable in try-with-resources.
     *
     * @throws IOException Should be never thrown.
     */
    public void close() throws IOException {
        stop();
    }

    private boolean isMethodSupported(HttpMethod method) {

        for (HttpMethod m : HttpMethod.values()) {
            if (m == method)

                return true;
        }

        return false;
    }


    public class TailFilter extends Filter {

        @Override
        public void doFilter(Request req, Response response) throws Exception {
            Handler handler = config.handler(req.getPath());

            if (handler != null) {
                handler.handle(req, response);
            } else {
                searchPath(response, req.socket, req.getPath());
            }
        }
    }


    private class ConnectionHandler implements Runnable {
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Socket sock = socket.accept();

                    sock.setSoTimeout(config.getSocketTimeout());

                    //processConnection(sock);
                    // incapsulate multithread sock execution
                    sockProcessorPool.execute(() -> {
                        try {
                            processConnection(sock);
                        } catch (IOException e) {
                            LOG.error("Error processing connection", e);
                        } finally {
                            Utils.closeQuiet(sock);
                        }
                    });

                } catch (Exception e) {
                    if (!Thread.currentThread().isInterrupted())
                        LOG.error("Error accepting connection", e);
                }
            }
        }
    }

    private void searchPath(Response resp, Socket socket, String path) throws IOException {
        File workDirectory = config.getWorkDirectory();
        File file = new File(config.getWorkDirectory().getAbsolutePath() + path);
        if (file.exists()) {
            resp.getOutputStreamBuffer().write(Files.readAllBytes(file.toPath()));
            resp.setContentType(searchMime(file));

        }

        respond(SC_NOT_FOUND, "Not Found", htmlMessage(SC_NOT_FOUND + " Not Found"),
                socket.getOutputStream());

    }

    private String searchMime(File file) {
        String[] nameAndsuff = file.getName().split("\\.(?=\\w*$)");

        if (nameAndsuff.length == 1)
            return Http.MIME_BINARY;

        switch (nameAndsuff[1]) {
            case "png":
                return Http.MIME_PNG;
            case "jpeg":
                return Http.MIME_JPEG;
            case "gif":
                return Http.MIME_GIF;
            case "html":
                return Http.MIME_HTML;
            case "txt":
                return Http.MIME_TXT;
            case "pdf":
                return Http.MIME_PDF;
            case "css":
                return Http.MIME_CSS;
            case "js":
                return Http.MIME_JS;
            case "doc":
                return Http.MIME_MSWORD;
            case "docx":
                return Http.MIME_MSWORD;
            case "xls":
                return Http.MIME_MSEXCEL;
            case "xlsx":
                return Http.MIME_MSEXCEL;
            default:
                return Http.MIME_BINARY;
        }
    }
}
