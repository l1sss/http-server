package ru.ifmo.server;

import static ru.ifmo.server.Http.OK_HEADER;

/**
 * Created by l1s on 25.05.17.
 */
public class PostHandler implements Handler {
    public static final String OPEN_HTML = "<html><body>";
    public static final String CLOSE_HTML = "</html></body>";

    public static final String TEST_RESPONSE = OPEN_HTML + "<html><body>Test response";

    @Override
    public void handle(Request request, Response response) throws Exception {
        response.getOutputStream().write((OK_HEADER + TEST_RESPONSE +
                "<br>Arguments: " + request.getArguments() +
                "<br>Content type: " + request.getBody().getContentType() +
                "<br>Content length: " + request.getBody().getContentLength() +
                "<br>Text content: " + request.getBody().getTxtContent() + CLOSE_HTML).getBytes());
        response.getOutputStream().flush();
    }
}
