package ru.ifmo.server;

/**
 * Created by l1s on 19.05.17.
 */
public class Body {
    String contentType;
    String stringBody;
    int contentLength;
    byte[] data;

    public String getStringBody() {
        return stringBody;
    }

    public String getContentType() {
        return contentType;
    }

    public int getContentLength() {
        return contentLength;
    }

    public byte[] getData() {
        return data;
    }
}