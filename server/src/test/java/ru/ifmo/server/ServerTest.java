package ru.ifmo.server;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
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
    private static final String POST_URL = "/test_post";

    private static Server server;
    private static CloseableHttpClient client;

    @BeforeClass
    public static void initialize() {
        ServerConfig cfg = new ServerConfig()
                .addHandler(SUCCESS_URL, new SuccessHandler())
                .addHandler(SERVER_ERROR_URL, new FailHandler())
                .addHandler(POST_URL, new PostAndPutHandler());

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

    @Test //some error...
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
    public void testPostWithEmptyBody() throws Exception {
        URI uri = new URI(POST_URL);

        HttpPost post = new HttpPost(uri);

        CloseableHttpResponse response = client.execute(host, post);

        assertStatusCode(HttpStatus.SC_OK, response);
        assertEquals(PostAndPutHandler.TEST_RESPONSE +
                        "<br>Arguments: {}" +
                        "<br>Content type: null" +
                        "<br>Content length: 0" +
                        "<br>Text content: null" +
                        PostAndPutHandler.CLOSE_HTML,
                EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testPostWithEmptyBodyAndSomeArguments() throws Exception {
        URI uri = new URIBuilder(POST_URL)
                .addParameter("iLoveWriteCode", "true")
                .addParameter("iLoveWriteTests", "false")
                .addParameter("noMoreTests", "")
                .build();

        HttpPost post = new HttpPost(uri);

        CloseableHttpResponse response = client.execute(host, post);

        assertStatusCode(HttpStatus.SC_OK, response);
        assertEquals(PostAndPutHandler.TEST_RESPONSE +
                    "<br>Arguments: {iLoveWriteCode=true, iLoveWriteTests=false, noMoreTests=null}" +
                    "<br>Content type: null" +
                    "<br>Content length: 0" +
                    "<br>Text content: null" +
                    PostAndPutHandler.CLOSE_HTML,
                EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testPostWithTextContent() throws Exception {
        URI uri = new URI(POST_URL);

        HttpPost post = new HttpPost(uri);
        StringEntity entity = new StringEntity("some text in request body");
        post.setEntity(entity);

        CloseableHttpResponse response = client.execute(host, post);

        assertStatusCode(HttpStatus.SC_OK, response);
        assertEquals(PostAndPutHandler.TEST_RESPONSE +
                        "<br>Arguments: {}" +
                        "<br>Content type: text/plain; charset=ISO-8859-1" +
                        "<br>Content length: 25" +
                        "<br>Text content: some text in request body" +
                        PostAndPutHandler.CLOSE_HTML,
                EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testPostWithTextContentAndSomeArguments() throws Exception {
        URI uri = new URIBuilder(POST_URL)
                .addParameter("test1", "noMore")
                .addParameter("test2", "") //!!!
                .addParameter("test3", "soBoring")
                .build();

        HttpPost post = new HttpPost(uri);
        StringEntity entity = new StringEntity("some text in request body");
        post.setEntity(entity);

        CloseableHttpResponse response = client.execute(host, post);

        assertStatusCode(HttpStatus.SC_OK, response);
        assertEquals(PostAndPutHandler.TEST_RESPONSE +
                        "<br>Arguments: {test1=noMore, test2=null, test3=soBoring}" +
                        "<br>Content type: text/plain; charset=ISO-8859-1" +
                        "<br>Content length: 25" +
                        "<br>Text content: some text in request body" +
                        PostAndPutHandler.CLOSE_HTML,
                EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testPutWithEmptyBody() throws Exception {
        URI uri = new URI(POST_URL);

        HttpPut put = new HttpPut(uri);

        CloseableHttpResponse response = client.execute(host, put);

        assertStatusCode(HttpStatus.SC_OK, response);
        assertEquals(PostAndPutHandler.TEST_RESPONSE +
                        "<br>Arguments: {}" +
                        "<br>Content type: null" +
                        "<br>Content length: 0" +
                        "<br>Text content: null" +
                        PostAndPutHandler.CLOSE_HTML,
                EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testPutWithEmptyBodyAndSomeArguments() throws Exception {
        URI uri = new URIBuilder(POST_URL)
                .addParameter("iLoveWriteCode", "true")
                .addParameter("iLoveWriteTests", "false")
                .addParameter("noMoreTests", "")
                .build();

        HttpPut put = new HttpPut(uri);

        CloseableHttpResponse response = client.execute(host, put);

        assertStatusCode(HttpStatus.SC_OK, response);
        assertEquals(PostAndPutHandler.TEST_RESPONSE +
                        "<br>Arguments: {iLoveWriteCode=true, iLoveWriteTests=false, noMoreTests=null}" +
                        "<br>Content type: null" +
                        "<br>Content length: 0" +
                        "<br>Text content: null" +
                        PostAndPutHandler.CLOSE_HTML,
                EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testPutWithTextContent() throws Exception {
        URI uri = new URI(POST_URL);

        HttpPut put = new HttpPut(uri);
        StringEntity entity = new StringEntity("some text in request body");
        put.setEntity(entity);

        CloseableHttpResponse response = client.execute(host, put);

        assertStatusCode(HttpStatus.SC_OK, response);
        assertEquals(PostAndPutHandler.TEST_RESPONSE +
                        "<br>Arguments: {}" +
                        "<br>Content type: text/plain; charset=ISO-8859-1" +
                        "<br>Content length: 25" +
                        "<br>Text content: some text in request body" +
                        PostAndPutHandler.CLOSE_HTML,
                EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testPutWithTextContentAndSomeArguments() throws Exception {
        URI uri = new URIBuilder(POST_URL)
                .addParameter("test1", "noMore")
                .addParameter("test2", "") //!!!
                .addParameter("test3", "soBoring")
                .build();

        HttpPut put = new HttpPut(uri);
        StringEntity entity = new StringEntity("some text in request body");
        put.setEntity(entity);

        CloseableHttpResponse response = client.execute(host, put);

        assertStatusCode(HttpStatus.SC_OK, response);
        assertEquals(PostAndPutHandler.TEST_RESPONSE +
                        "<br>Arguments: {test1=noMore, test2=null, test3=soBoring}" +
                        "<br>Content type: text/plain; charset=ISO-8859-1" +
                        "<br>Content length: 25" +
                        "<br>Text content: some text in request body" +
                        PostAndPutHandler.CLOSE_HTML,
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

    private void assertNotImplemented(HttpRequest request) throws Exception {
        CloseableHttpResponse response = client.execute(host, request);

        assertStatusCode(HttpStatus.SC_NOT_IMPLEMENTED, response);
        assertNotNull(EntityUtils.toString(response.getEntity()));
    }
}
