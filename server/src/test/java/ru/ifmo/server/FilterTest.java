package ru.ifmo.server;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static ru.ifmo.server.TestUtils.assertStatusCode;

/**
 * Created by l1s on 12.06.17.
 */
public class FilterTest {
    private static final HttpHost host = new HttpHost("localhost", ServerConfig.DFLT_PORT);

    private static final String FILTER_URL = "/test_filter";
    private static int cnt = 0;

    private static Server server;
    private static CloseableHttpClient client;

    @BeforeClass
    public static void initialize() {
        ServerConfig cfg = new ServerConfig()
                .addHandler(FILTER_URL, new FilterHandler());

        Filter filter1 = new TestUtils.HeaderFilter("filter1", ++cnt);
        Filter filter2 = new TestUtils.HeaderFilter("filter2", ++cnt);
        Filter filter3 = new TestUtils.HeaderFilter("filter3", ++cnt);

        cfg.setFilters(filter1, filter2, filter3);

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
    public void testFilter() throws Exception {
        URI uri = new URIBuilder(FILTER_URL).build();
        HttpRequest request = new HttpGet(uri);
        request.addHeader("User-Agent", "TestAgent");

        CloseableHttpResponse response = client.execute(host, request);

        assertStatusCode(HttpStatus.SC_OK, response);
        assertEquals(SuccessHandler.TEST_RESPONSE +
                        "<br>Headers: {Accept-Encoding=gzip,deflate, Connection=Keep-Alive, " +
                        "Host=localhost:8080, User-Agent=TestAgent, filter1=1, filter2=2, filter3=3}" +
                        SuccessHandler.CLOSE_HTML,
                EntityUtils.toString(response.getEntity()));
    }
}