package ru.ifmo.server;

import org.junit.*;
import java.io.IOException;
import static junit.framework.TestCase.assertEquals;

/**
 * Created by Тарас on 08.06.2017.
 */
public class PathXmlTest {
    static ServerConfig config;

    @BeforeClass
    public static void setUp() throws IOException {
        config = new Loader().load("D:\\Код\\Java\\ifmo\\diploma\\http-server\\server\\src\\test\\resources\\web-server.xml");
    }

    @Test
    public void testPort(){
        assertEquals(7777, config.getPort());
    }

    @Test
    public void testSocketTimeout(){
        assertEquals(9, config.getSocketTimeout());
    }

    @Test
    public void testHandlers() throws ReflectiveOperationException {
        String hN = Class.forName("ru.ifmo.server.FailHandler").newInstance().toString();

        assertEquals("{/info.html=" + hN + ", " + "/info=" + hN + "}", config.getHandlers());
    }
}
