package ru.ifmo.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.PrintWriter;

import static ru.ifmo.server.Http.*;

/**
 * Provides {@link java.io.OutputStream} ro respond to client.
 */
public class Response {
    int statusCode;
    Map<String, String> headers = new HashMap<>();
    List<Cookie> cookies;
    PrintWriter printWriter;
    ByteArrayOutputStream bufOut;

    public void setStatusCode(int code) {
        if ((code < 100) || (code > 599)) {
            throw new ServerException("Not valid http status code: " + code);
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

    public void setContentType(String contentType) {
        headers.put(CONTENT_TYPE, contentType);
    }

    public String getContentType() {
        return headers.get(CONTENT_TYPE);
    }

    public void setContentLength(long length) {
        headers.put("Content-Length", String.valueOf(length));
    }

    public String getContentLength() {
        return headers.get(CONTENT_LENGTH);
    }

    /**
     * @return {@link OutputStream} connected to the client.
     */
    public ByteArrayOutputStream getOutputStreamBuffer() {
        if (bufOut == null)
            bufOut = new ByteArrayOutputStream();

        return bufOut;
    }

    public void setBody(byte[] data) {
        try {
            getOutputStreamBuffer().write(data);
        } catch (IOException e) {
            throw new ServerException("Cannot get output stream", e);
        }
    }

    /**
     * add cookie in cookies collection
     */
    public void setCookies(Cookie cookie) {
        if (cookies == null)
            cookies = new ArrayList<>();

        cookies.add(cookie);
    }

    /**
     * @return cookies collection
     */
    public List<Cookie> getCookies() {
        return cookies;
    }

    public PrintWriter getWriter() {
        if (printWriter == null)
            printWriter = new PrintWriter(getOutputStreamBuffer());

        return printWriter;
    }
}