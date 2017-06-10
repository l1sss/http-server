package ru.ifmo.server;

import java.util.TreeMap;

import static ru.ifmo.server.SuccessHandler.CLOSE_HTML;
import static ru.ifmo.server.SuccessHandler.TEST_RESPONSE;

public class FilterHandler implements Handler {
    @Override
    public void handle(Request request, Response response) throws Exception {
        response.getWriter().write((TEST_RESPONSE +
                "<br>Headers: " + new TreeMap<>(request.getHeaders()) + CLOSE_HTML));

    }
}
