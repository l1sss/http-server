package ru.ifmo.server;

import java.util.Arrays;

import static ru.ifmo.server.SuccessHandler.CLOSE_HTML;
import static ru.ifmo.server.SuccessHandler.TEST_RESPONSE;

/**
 * Created by l1s on 26.05.17.
 */
public class OptionsHandler implements Handler {
    @Override
    public void handle(Request request, Response response) throws Exception {
        response.getWriter().write((TEST_RESPONSE +
                "<br>Access-Control-Allow-Methods: " +
                Arrays.toString(HttpMethod.values()) +
                CLOSE_HTML));
    }
}