package ru.ifmo.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides {@link java.io.OutputStream} ro respond to client.
 */
public class Response {
    final Socket socket;
    Map<String, String> headers;
    List<Cookie> resCookies;

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

    public void setHeader(String name, String value) {
        if (headers == null)
            headers = new HashMap<>();

        headers.put(name, value);
    }

    public void setCookies(Cookie cookie) {
        if (resCookies == null)
            resCookies = new ArrayList<>();

        resCookies.add(cookie);
    }
}