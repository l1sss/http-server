package ru.ifmo.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static ru.ifmo.server.Http.CONTENT_LENGTH;
import static ru.ifmo.server.Http.CONTENT_TYPE;

import static ru.ifmo.server.Session.SESSION_COOKIE_NAME;

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
    Map<String, String> cookies;
    Map<String, Session> sessions;

    Session session;

    Request(Socket socket) {
        this.socket = socket;
    }

    /**
     * @return {@link InputStream} connected to the client.
     */
    public InputStream getInputStream() {
        try {
            return socket.getInputStream();
        } catch (IOException e) {
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
        if (body == null)
            body = new Body();

        return body;
    }

    /**
     * add headers in parse process
     */
    void addHeader(String key, String value) {
        if (headers == null)
            headers = new LinkedHashMap<>();

        if (CONTENT_TYPE.equals(key)) {
            if (body == null)
                body = new Body();
            body.contentType = value;

        } else if (CONTENT_LENGTH.equals(key)) {
            if (body == null)
                body = new Body();
            body.contentLength = Integer.parseInt(value);
        }

        headers.put(key, value);
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

    void insertCookie(String name, String value) {
        if (cookies == null)
            cookies = new HashMap<>();

        cookies.put(name, value);
    }

    public Map<String, String> getCookies() {
        if (cookies == null)
            return Collections.emptyMap();

        return Collections.unmodifiableMap(cookies);
    }

    public String getCookieValue(String key) {
        return cookies.get(key);
    }

    /**
     * @return if contains session id returns true
     */
    private boolean containsSIDCookie() {
        return getCookies().containsKey(SESSION_COOKIE_NAME);
    }

    public Session getSession() {
        if (session == null)
            session = getSession(false);

        return session;
    }

    void initSessions(Map<String, Session> sessions) {
        this.sessions = sessions;
    }

    public Session getSession(boolean isNew) {
        if (!containsSIDCookie() || isNew) {
            session = new Session();
            sessions.put(session.getId(), session);
        } else {
            session = sessions.get(getCookieValue(SESSION_COOKIE_NAME));
            if (session == null)
                session = getSession(true);
        }
        return session;
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