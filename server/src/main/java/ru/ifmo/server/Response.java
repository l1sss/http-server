package ru.ifmo.server;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides {@link java.io.OutputStream} ro respond to client.
 */
public class Response {
    final Socket socket;
    int statusCode;
    Map<String, String> headers = new HashMap<>();
    PrintWriter printWriter;
    long length = -1;
    String contentType;
    ByteArrayOutputStream bufferOutputStream;


    //GETTER and SETTER//

    public void setStatusCode (int code) {
        if ( (code<100)||(code>505)  ){
            throw new ServerException("Not valid http status code:" + code);
        }
        statusCode = code;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public void setHeaders(Map<String, String> headers) throws IOException {
        this.headers = headers;
    }

    public void setHeader(String key, String value) throws IOException {
        this.headers.put(key, value);
    }

    public String setContentType(String contentType) {
        return this.contentType = contentType;
    }

    public void setContentLength(long length) {
        if (length < 0) {
            throw new RuntimeException("Response Content-Length must be non-negative.");
        }
    }

    Response(Socket socket) {
        this.socket = socket;
    }

    /**
     * @return {@link OutputStream} connected to the client.
     */
    public ByteArrayOutputStream getOutputStreamBuffer() {
        if (bufferOutputStream == null)
            bufferOutputStream = new ByteArrayOutputStream();

        return bufferOutputStream;
    }


    public void setBody(byte[] data) {
        try {
            getOutputStreamBuffer().write(data);
        } catch (IOException e) {
            throw new ServerException("Cannot get output stream", e);
        }
    }

    public PrintWriter getWriter() {
        if (printWriter==null)
            printWriter = new PrintWriter(getOutputStreamBuffer());
        return printWriter;
    }
}
