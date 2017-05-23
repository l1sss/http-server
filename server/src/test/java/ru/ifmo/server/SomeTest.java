package ru.ifmo.server;

import org.junit.Test;

/**
 * Created by l1s on 22.05.17.
 */
public class SomeTest {
    @Test
    public void test() throws Exception {
        ServerConfig cfg = new ServerConfig();

        cfg.addHandler("/index", new Handler() {
            @Override
            public void handle(Request request, Response response) throws Exception {

            }
        });

        Server.start(cfg);
    }
}
