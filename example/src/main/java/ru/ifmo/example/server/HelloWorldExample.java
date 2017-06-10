package ru.ifmo.example.server;

import ru.ifmo.server.*;

/**
 * Simple hello world example.
 */
public class HelloWorldExample {
    public static void main(String[] args) {
        ServerConfig config = new ServerConfig()
                .addHandler("/index", new Handler() {
                    @Override
                    public void handle(Request request, Response response) throws Exception {
                        response.getWriter().write("Hello, world!");
                    }
                });

        Server.start(config);
    }
}
