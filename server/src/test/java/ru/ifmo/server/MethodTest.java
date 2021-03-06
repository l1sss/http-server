package ru.ifmo.server;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.*;
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
import static ru.ifmo.server.SuccessHandler.CLOSE_HTML;
import static ru.ifmo.server.SuccessHandler.TEST_RESPONSE;
import static ru.ifmo.server.TestUtils.assertStatusCode;

/**
 * Created by l1s on 12.06.17.
 */
public class MethodTest {
    private static final HttpHost host = new HttpHost("localhost", ServerConfig.DFLT_PORT);

    private static final String HEAD_URL = "/test_head";
    private static final String POST_AND_PUT_URL = "/test_post_and_put";
    private static final String OPTIONS_URL = "/test_options";
    private static final String DELETE_URL = "/test_delete";

    private static Server server;
    private static CloseableHttpClient client;

    @BeforeClass
    public static void initialize() {
        ServerConfig cfg = new ServerConfig()
                .addHandler(HEAD_URL, new SuccessHandler())
                .addHandler(DELETE_URL, new SuccessHandler())
                .addHandler(OPTIONS_URL, new OptionsHandler())
                .addHandler(POST_AND_PUT_URL, new PostAndPutHandler());

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
    public void testHead() throws Exception {
        HttpHead head = new HttpHead(HEAD_URL);

        CloseableHttpResponse response = client.execute(host, head);

        assertStatusCode(HttpStatus.SC_OK, response);
    }

    @Test
    public void testDelete() throws Exception {
        HttpDelete delete = new HttpDelete(DELETE_URL);

        CloseableHttpResponse response = client.execute(host, delete);

        assertStatusCode(HttpStatus.SC_OK, response);
    }

    @Test
    public void testOptions() throws Exception {
        HttpOptions options = new HttpOptions(OPTIONS_URL);

        CloseableHttpResponse response = client.execute(host, options);

        assertStatusCode(HttpStatus.SC_OK, response);
        assertEquals(TEST_RESPONSE +
                        "<br>Access-Control-Allow-Methods: [GET, POST, PUT, DELETE, HEAD, OPTIONS]" +
                        CLOSE_HTML,
                EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testPostWithEmptyBody() throws Exception {
        HttpPost post = new HttpPost(POST_AND_PUT_URL);

        CloseableHttpResponse response = client.execute(host, post);

        assertStatusCode(HttpStatus.SC_OK, response);
        assertEquals(TEST_RESPONSE +
                        "<br>Arguments: {}" +
                        "<br>Content type: null" +
                        "<br>Content length: 0" +
                        "<br>Text content: null" +
                        CLOSE_HTML,
                EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testPostWithEmptyBodyAndSomeArguments() throws Exception {
        URI uri = new URIBuilder(POST_AND_PUT_URL)
                .addParameter("iLoveWriteCode", "true")
                .addParameter("iLoveWriteTests", "false")
                .addParameter("noMoreTests", "")
                .build();

        HttpPost post = new HttpPost(uri);

        CloseableHttpResponse response = client.execute(host, post);

        assertStatusCode(HttpStatus.SC_OK, response);
        assertEquals(TEST_RESPONSE +
                        "<br>Arguments: {iLoveWriteCode=true, iLoveWriteTests=false, noMoreTests=null}" +
                        "<br>Content type: null" +
                        "<br>Content length: 0" +
                        "<br>Text content: null" +
                        CLOSE_HTML,
                EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testPostWithTextContent() throws Exception {
        HttpPost post = new HttpPost(POST_AND_PUT_URL);
        StringEntity entity = new StringEntity("some text in request body");
        post.setEntity(entity);

        CloseableHttpResponse response = client.execute(host, post);

        assertStatusCode(HttpStatus.SC_OK, response);
        assertEquals(TEST_RESPONSE +
                        "<br>Arguments: {}" +
                        "<br>Content type: text/plain; charset=ISO-8859-1" +
                        "<br>Content length: 25" +
                        "<br>Text content: some text in request body" +
                        CLOSE_HTML,
                EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testPostWithTextContentAndSomeArguments() throws Exception {
        URI uri = new URIBuilder(POST_AND_PUT_URL)
                .addParameter("test1", "noMore")
                .addParameter("test2", "") //!!!
                .addParameter("test3", "soBoring")
                .build();

        HttpPost post = new HttpPost(uri);
        StringEntity entity = new StringEntity("some text in request body");
        post.setEntity(entity);

        CloseableHttpResponse response = client.execute(host, post);

        assertStatusCode(HttpStatus.SC_OK, response);
        assertEquals(TEST_RESPONSE +
                        "<br>Arguments: {test1=noMore, test2=null, test3=soBoring}" +
                        "<br>Content type: text/plain; charset=ISO-8859-1" +
                        "<br>Content length: 25" +
                        "<br>Text content: some text in request body" +
                        CLOSE_HTML,
                EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testPutWithEmptyBody() throws Exception {
        HttpPut put = new HttpPut(POST_AND_PUT_URL);

        CloseableHttpResponse response = client.execute(host, put);

        assertStatusCode(HttpStatus.SC_OK, response);
        assertEquals(TEST_RESPONSE +
                        "<br>Arguments: {}" +
                        "<br>Content type: null" +
                        "<br>Content length: 0" +
                        "<br>Text content: null" +
                        CLOSE_HTML,
                EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testPutWithEmptyBodyAndSomeArguments() throws Exception {
        URI uri = new URIBuilder(POST_AND_PUT_URL)
                .addParameter("iLoveWriteCode", "true")
                .addParameter("iLoveWriteTests", "false")
                .addParameter("noMoreTests", "")
                .build();

        HttpPut put = new HttpPut(uri);

        CloseableHttpResponse response = client.execute(host, put);

        assertStatusCode(HttpStatus.SC_OK, response);
        assertEquals(TEST_RESPONSE +
                        "<br>Arguments: {iLoveWriteCode=true, iLoveWriteTests=false, noMoreTests=null}" +
                        "<br>Content type: null" +
                        "<br>Content length: 0" +
                        "<br>Text content: null" +
                        CLOSE_HTML,
                EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testPutWithTextContent() throws Exception {
        HttpPut put = new HttpPut(POST_AND_PUT_URL);
        StringEntity entity = new StringEntity("some text in request body");
        put.setEntity(entity);

        CloseableHttpResponse response = client.execute(host, put);

        assertStatusCode(HttpStatus.SC_OK, response);
        assertEquals(TEST_RESPONSE +
                        "<br>Arguments: {}" +
                        "<br>Content type: text/plain; charset=ISO-8859-1" +
                        "<br>Content length: 25" +
                        "<br>Text content: some text in request body" +
                        CLOSE_HTML,
                EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testPutWithTextContentAndSomeArguments() throws Exception {
        URI uri = new URIBuilder(POST_AND_PUT_URL)
                .addParameter("test1", "noMore")
                .addParameter("test2", "") //!!!
                .addParameter("test3", "soBoring")
                .build();

        HttpPut put = new HttpPut(uri);
        StringEntity entity = new StringEntity("some text in request body");
        put.setEntity(entity);

        CloseableHttpResponse response = client.execute(host, put);

        assertStatusCode(HttpStatus.SC_OK, response);
        assertEquals(TEST_RESPONSE +
                        "<br>Arguments: {test1=noMore, test2=null, test3=soBoring}" +
                        "<br>Content type: text/plain; charset=ISO-8859-1" +
                        "<br>Content length: 25" +
                        "<br>Text content: some text in request body" +
                        CLOSE_HTML,
                EntityUtils.toString(response.getEntity()));
    }
}