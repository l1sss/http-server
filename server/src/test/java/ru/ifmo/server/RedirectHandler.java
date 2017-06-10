package ru.ifmo.server;

/**
 * Created by HomePC on 07.06.2017.
 */
public class RedirectHandler implements Handler {
    @Override
    public void handle(Request request, Response response) throws Exception {
        response.Redirect("http://mail.ru");
    }
}

