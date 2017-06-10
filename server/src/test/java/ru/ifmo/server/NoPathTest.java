package ru.ifmo.server;

import org.junit.*;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Тарас on 08.06.2017.
 */
public class NoPathTest {
    static ServerConfig config;

    @BeforeClass
    public static void setUp() throws IOException {
        config = new Loader().load(null);
    }

    @Test
    public void testPort(){
        assertEquals(8888, config.getPort());
    }

    @Test
    public void testSocketTimeout(){
        assertEquals(5, config.getSocketTimeout());
    }

    @Test
    public void testHandlers() throws ReflectiveOperationException {
        String hN = Class.forName("ru.ifmo.server.FailHandler").newInstance().toString();

        assertEquals("{/info.html=" + hN + ", " + "/info=" + hN + "}", config.getHandlers());
    }
}
