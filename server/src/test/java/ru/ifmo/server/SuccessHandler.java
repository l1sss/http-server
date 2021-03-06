package ru.ifmo.server;

/**
 * Responds with OK status code, test text in body and parsed params.
 */
public class SuccessHandler implements Handler {
    public static final String OPEN_HTML = "<html><body>";
    public static final String CLOSE_HTML = "</html></body>";

    public static final String TEST_RESPONSE = OPEN_HTML + "<html><body>Test response";

    @Override
    public void handle(Request request, Response response) throws Exception {
        response.getWriter().write((TEST_RESPONSE +
                "<br>" + request.getArguments() + CLOSE_HTML));
    }
}
