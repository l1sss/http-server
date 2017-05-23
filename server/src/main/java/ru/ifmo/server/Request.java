package ru.ifmo.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static ru.ifmo.server.Http.CONTENT_LENGTH;
import static ru.ifmo.server.Http.CONTENT_TYPE;
import static ru.ifmo.server.Http.CONTENT_TYPE_SEPARATOR;

/**
 * Keeps request information: method, headers, params
 * and provides {@link java.io.InputStream} to get additional data
 * from client.
 */
public class Request {
    final Socket socket;
    HttpMethod method;
    URI path;
    Body body;

    Map<String, String> headers;
    Map<String, String> args;

    Request(Socket socket) {
        this.socket = socket;
        this.body = new Body();
    }

    /**
     * @return {@link InputStream} connected to the client.
     */
    public InputStream getInputStream() {
        try {
            return socket.getInputStream();
        }
        catch (IOException e) {
            throw new ServerException("Unable retrieve input stream.", e);
        }
    }

    /**
     * @return HTTP method of this request.
     */
    public HttpMethod getMethod() {
        return method;
    }

    /**
     * @return Request path.
     */
    public String getPath() {
        return path.getPath();
    }

    /**
     * @return Request headers.
     */
    public Map<String, String> getHeaders() {
        if (headers == null)
            return Collections.emptyMap();

        return Collections.unmodifiableMap(headers);
    }

    /**
     * @return Request body.
     */
    public Body getBody() {
        return body;
    }

    void addHeader(String key, String value) {
        if (headers == null)
            headers = new LinkedHashMap<>();

        if (key.equals(CONTENT_TYPE)) {
            body.setContentType(value);
            body.setContentFormat(parseFormat(value));
        }
        else if (key.equals(CONTENT_LENGTH))
            body.setContentLength(Integer.parseInt(value));

        headers.put(key, value);
    }

    /**
     *
     * @param line - body content-type
     * @return String content format.
     */
    private String parseFormat(String line) {
        String format = null;

        int len = line.length();

        for (int i = 0; i < len; i++) {
            if (line.charAt(i) == CONTENT_TYPE_SEPARATOR)
                format = line.substring(i + 1, line.length()).trim();
        }

        return format;
    }

    void addArgument(String key, String value) {
        if (args == null)
            args = new LinkedHashMap<>();

        args.put(key, value);
    }

    /**
     * @return Arguments passed to this request.
     */
    public Map<String, String> getArguments() {
        if (args == null)
            return Collections.emptyMap();

        return Collections.unmodifiableMap(args);
    }

    @Override
    public String toString() {
        return "Request{" +
                "socket=" + socket +
                ", method=" + method +
                ", path=" + path +
                ", headers=" + headers +
                ", args=" + args +
                '}';
    }
}