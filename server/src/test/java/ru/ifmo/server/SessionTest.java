package ru.ifmo.server;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static ru.ifmo.server.TestUtils.assertStatusCode;

/**
 * Created by l1s on 12.06.17.
 */
public class SessionTest {
    private static final HttpHost host = new HttpHost("localhost", ServerConfig.DFLT_PORT);

    private static final String SESSION_URL = "/test_session";

    private static Server server;
    private static CloseableHttpClient client;

    @BeforeClass
    public static void initialize() {
        ServerConfig cfg = new ServerConfig()
                .addHandler(SESSION_URL, new SuccessHandler());

        server = Server.start(cfg);
        client = HttpClients.createDefault();
    }

    @AfterClass
    public static void stop() {
        IOUtils.closeQuietly(server);
        IOUtils.closeQuietly(client);

        server = null;
        client = null;
    }

    @Test
    public void testSessionInvalidate() throws Exception {
        HttpGet get = new HttpGet(SESSION_URL);

        CloseableHttpResponse response = client.execute(host, get);

        HttpGet get2 = new HttpGet(SESSION_URL);

        CloseableHttpResponse response2 = client.execute(host, get2);

        assertStatusCode(HttpStatus.SC_OK, response);
        assertStatusCode(HttpStatus.SC_OK, response2);
        assertEquals(1, server.getSessions().size());

        for (Map.Entry<String, Session> pair : server.getSessions().entrySet()) {
            pair.getValue().invalidate();
        }

        Thread.sleep(100);

        assertEquals(0, server.getSessions().size());
    }

    @Test
    public void testSessionData() throws Exception {
        Session session = new Session();
        session.setParam("testKey", "testValue");
        assertEquals("testValue", session.getParam("testKey"));
    }

    @Test(expected = SessionException.class)
    public void testSessionException() throws Exception {
        Session session = new Session();
        session.invalidate();
        session.setParam("testKey", "testValue");
    }
}
