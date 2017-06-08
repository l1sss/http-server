package ru.ifmo.server;

/**
 * Created by l1s on 03.06.17.
 */
public class Cookie {
    String name;
    String value;
    String id;
    String lifeTime;

    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Cookie(String name, String value, String id) {
        this.name = name;
        this.value = value;
        this.id = id;
    }

    public Cookie(String name, String value, String id, String liveTime) {
        this.name = name;
        this.value = value;
        this.id = id;
        this.lifeTime = liveTime;
    }
}
