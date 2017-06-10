package ru.ifmo.example.server;

import ru.ifmo.server.*;

import java.io.PrintWriter;

/**
 * Simple hello world example.
 */
public class SessionExample {
    public static void main(String[] args) {
        Handler handler = new Handler() {
            @Override
            public void handle(Request request, Response response) throws Exception {
                String name = request.getArguments().get("name");
                Session session = request.getSession();
                if (name != null)
                    session.setParam("name", name);

                PrintWriter sb = response.getWriter();
                sb.append("Arguments: ").append(name);
                sb.append("\n");
                sb.append("Username: ").append(session.getParam("name"));
            }
        };

        ServerConfig cfg = new ServerConfig();
        cfg.addHandler("/session", handler);
        Server.start(cfg);
    }
}