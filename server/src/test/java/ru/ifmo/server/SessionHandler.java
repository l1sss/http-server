package ru.ifmo.server;

import static ru.ifmo.server.Http.OK_HEADER;
import static ru.ifmo.server.SuccessHandler.CLOSE_HTML;
import static ru.ifmo.server.SuccessHandler.TEST_RESPONSE;

/**
 * Created by l1s on 08.06.17.
 */
public class SessionHandler implements Handler {
    @Override
    public void handle(Request request, Response response) throws Exception {
        response.getOutputStream().write((OK_HEADER + TEST_RESPONSE +
                "<br>Sessions count: " + request.getSession() +
                CLOSE_HTML).getBytes());
        response.getOutputStream().flush();
    }
}
