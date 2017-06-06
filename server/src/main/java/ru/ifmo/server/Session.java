package ru.ifmo.server;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by l1s on 30.05.17.
 */
public class Session {
    public static final String SESSION_COOKIE_NAME = "SESSIONID";

    final String SESSION_ID_SYMBOLS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    final int SESSION_ID_LENGTH = 32;
    final int SESSION_LIVETIME = 1800;


    String id;
    LocalDateTime expire;
    boolean expired;
    Map<String, Object> sessionData;

    public Session() {
        this.id = generateSessionId();
        this.setExpire(SESSION_LIVETIME);
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getExpire() {
        return expire;
    }

    public void setExpire(int seconds) {
        this.expire = LocalDateTime.now().plusSeconds(seconds);
    }

    public void invalidate() {
        expired = true;
        Server.removeSession(id);
    }

    public <T> void setParam(String key, T value) {
        if (!expired) {
            if (sessionData == null) {
                sessionData = new HashMap<>();
            }
            sessionData.put(key, value);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getParam(String key) {
        if (sessionData == null)
            return null; //????????????????????????????????

        return (T) sessionData.get(key);
    }

    public String generateSessionId() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < SESSION_ID_LENGTH; i++) {
            sb.append(SESSION_ID_SYMBOLS.charAt((int) (Math.random() * SESSION_ID_SYMBOLS.length())));
        }

        return sb.toString();
    }
}