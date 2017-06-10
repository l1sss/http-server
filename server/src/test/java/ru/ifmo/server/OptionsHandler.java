package ru.ifmo.server;

import java.util.Arrays;

import static ru.ifmo.server.Http.OK_HEADER;
import static ru.ifmo.server.SuccessHandler.CLOSE_HTML;
import static ru.ifmo.server.SuccessHandler.TEST_RESPONSE;

/**
 * Created by l1s on 26.05.17.
 */
public class OptionsHandler implements Handler {
    @Override
    public void handle(Request request, Response response) throws Exception {
        response.getWriter().write((OK_HEADER + TEST_RESPONSE +
                "<br>Access-Control-Allow-Methods: " +
                Arrays.toString(HttpMethod.values()) +
                CLOSE_HTML));
    }
}