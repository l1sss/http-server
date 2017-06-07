package ru.ifmo.server;

/**
 * Throws exception on handle method.
 */
public class FakeHandler implements Handler {
    @Override
    public void handle(Request request, Response response) throws Exception {
        throw new Exception("Test exception");
    }
}
