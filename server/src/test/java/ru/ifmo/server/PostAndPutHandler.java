package ru.ifmo.server;

import static ru.ifmo.server.Http.OK_HEADER;
import static ru.ifmo.server.SuccessHandler.CLOSE_HTML;
import static ru.ifmo.server.SuccessHandler.TEST_RESPONSE;

/**
 * Created by l1s on 25.05.17.
 */
public class PostAndPutHandler implements Handler {
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