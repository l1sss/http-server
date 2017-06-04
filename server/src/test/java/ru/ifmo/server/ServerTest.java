package ru.ifmo.server;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static ru.ifmo.server.TestUtils.assertStatusCode;

/**
 * Tests main server functionality.
 */
public class ServerTest {
    private static final HttpHost host = new HttpHost("localhost", ServerConfig.DFLT_PORT);

    private static final String SUCCESS_URL = "/test_success";
    private static final String NOT_FOUND_URL = "/test_not_found";
    private static final String SERVER_ERROR_URL = "/test_fail";
    private static final String FILTER_URL = "/test_filter";
    private static int cnt = 0;

    private static Server server;
    private static CloseableHttpClient client;

    @BeforeClass
    public static void initialize() {
        ServerConfig cfg = new ServerConfig()
                .addHandler(SUCCESS_URL, new SuccessHandler())
                .addHandler(SERVER_ERROR_URL, new FailHandler())
                .addHandler(FILTER_URL,new FilterHandler());

        Filter filter1 = new TestUtils.HeaderFilter("filter1",++cnt);
        Filter filter2 = new TestUtils.HeaderFilter("filter2",++cnt);
        Filter filter3 = new TestUtils.HeaderFilter("filter3",++cnt);

        cfg.setFilters(filter1,filter2,filter3);

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
    public void testSuccess() throws Exception {
        // TODO test headers
        URI uri = new URIBuilder(SUCCESS_URL)
                .addParameter("1", "1")
                .addParameter("2", "2")
                .addParameter("testArg1", "testValue1")
                .addParameter("testArg2", "2")
                .addParameter("testArg3", "testVal3")
                .addParameter("testArg4", "")
                .build();

        HttpGet get = new HttpGet(uri);

        CloseableHttpResponse response = client.execute(host, get);

        assertStatusCode(HttpStatus.SC_OK, response);
        assertEquals(SuccessHandler.TEST_RESPONSE +
                        "<br>{1=1, 2=2, testArg1=testValue1, testArg2=2, testArg3=testVal3, testArg4=null}" +
                        SuccessHandler.CLOSE_HTML,
                EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testFilter() throws Exception {

        URI uri = new URIBuilder(FILTER_URL).build();
        HttpRequest request = new HttpGet(uri);

        CloseableHttpResponse response = client.execute(host, request);

        assertStatusCode(HttpStatus.SC_OK, response);
        assertEquals(SuccessHandler.TEST_RESPONSE +
                        "<br>Headers: {Host=localhost:8080, Connection=Keep-Alive, "
                        +"User-Agent=Apache-HttpClient/4.5.2 "
                        + "(Java/1.8.0_121), Accept-Encoding=gzip,deflate, filter1=1, filter2=2, filter3=3}" +
                        SuccessHandler.CLOSE_HTML,
                EntityUtils.toString(response.getEntity()));
    }


    @Test
    public void testNotFound() throws Exception {
        HttpGet get = new HttpGet(NOT_FOUND_URL);

        CloseableHttpResponse response = client.execute(host, get);

        assertStatusCode(HttpStatus.SC_NOT_FOUND, response);
        assertNotNull(EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testServerError() throws Exception {
        HttpGet get = new HttpGet(SERVER_ERROR_URL);

        CloseableHttpResponse response = client.execute(host, get);

        assertStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR, response);
        assertNotNull(EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testPost() throws Exception {
        HttpRequest request = new HttpPost(SUCCESS_URL);

        assertNotImplemented(request);
    }

    @Test
    public void testPut() throws Exception {
        HttpRequest request = new HttpPut(SUCCESS_URL);

        assertNotImplemented(request);
    }

    @Test
    public void testDelete() throws Exception {
        HttpRequest request = new HttpDelete(SUCCESS_URL);

        assertNotImplemented(request);
    }

    @Test
    public void testHead() throws Exception {
        HttpRequest request = new HttpHead(SUCCESS_URL);

        CloseableHttpResponse response = client.execute(host, request);

        assertStatusCode(HttpStatus.SC_NOT_IMPLEMENTED, response);
    }

    @Test
    public void testOptions() throws Exception {
        HttpRequest request = new HttpOptions(SUCCESS_URL);

        assertNotImplemented(request);
    }

    @Test
    public void testTrace() throws Exception {
        HttpRequest request = new HttpTrace(SUCCESS_URL);

        assertNotImplemented(request);
    }

    @Test
    public void testPatch() throws Exception {
        HttpRequest request = new HttpPatch(SUCCESS_URL);

        assertNotImplemented(request);
    }

    private void assertNotImplemented(HttpRequest request) throws Exception {
        CloseableHttpResponse response = client.execute(host, request);

        assertStatusCode(HttpStatus.SC_NOT_IMPLEMENTED, response);
        assertNotNull(EntityUtils.toString(response.getEntity()));
    }


}
