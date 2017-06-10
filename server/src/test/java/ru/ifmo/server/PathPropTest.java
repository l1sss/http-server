package ru.ifmo.server;

import org.junit.*;
import java.io.IOException;
import static junit.framework.TestCase.*;

/**
 * Created by Тарас on 08.06.2017.
 */
public class PathPropTest {
    static ServerConfig config;

    @BeforeClass
    public static void setUp() throws IOException {
        config = new Loader().load("D:\\Код\\Java\\ifmo\\diploma\\http-server\\server\\src\\test\\resources\\web-server.properties");
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
