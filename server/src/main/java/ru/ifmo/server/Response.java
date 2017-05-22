package ru.ifmo.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides {@link java.io.OutputStream} ro respond to client.
 */
public class Response {
    final Socket socket;
    private int code;
    private byte[] body;
    private Map<String, String> headers = new HashMap<>();
    private DataOutputStream writer;
    private long length = -1;
    private String contentType;


    //GETTER and SETTER//

    public int getStatusCode() {
        return code;
    }

    public void setStatusCode(int code) {
        this.code = code;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(String body) throws IOException {
        this.body = body.getBytes();
    }

    public void setBody(byte[] bytes) {
        body = bytes;
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

    private DataOutputStream getWriter() {
        return writer;
    }

    public String setContentType(String contentType){
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
    public OutputStream getOutputStream() {
        try {
            return socket.getOutputStream();
        }
        catch (IOException e) {
            throw new ServerException("Cannot get output stream", e);
        }
    }
}
