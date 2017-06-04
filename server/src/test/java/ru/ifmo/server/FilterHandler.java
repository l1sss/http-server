package ru.ifmo.server;

import static ru.ifmo.server.Http.OK_HEADER;
import static ru.ifmo.server.SuccessHandler.CLOSE_HTML;
import static ru.ifmo.server.SuccessHandler.TEST_RESPONSE;

public class FilterHandler implements Handler {
    @Override
    public void handle(Request request, Response response) throws Exception {
        response.getOutputStream().write((OK_HEADER + TEST_RESPONSE +
                        "<br>Headers: " + request.getHeaders() + CLOSE_HTML).getBytes());
        response.getOutputStream().flush();
    }
}
