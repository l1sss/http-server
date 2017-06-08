package ru.ifmo.example.server;

import ru.ifmo.server.*;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;

/**
 * Created by l1s on 25.05.17.
 */
public class TestExample {
    public static void main(String[] args) {
        ServerConfig config = new ServerConfig()
                .addHandler("/test", new Handler() {
                    @Override
                    public void handle(Request request, Response response) throws Exception {
                        Writer writer = new OutputStreamWriter(response.getOutputStream());
                        writer.write(Http.OK_HEADER + "Hello Test!" +
                                "\nSessionID: " + request.getSession().getId() +
                                "\nArguments: " + request.getArguments() +
                                "\nContent type: " + request.getBody().getContentType() +
                                "\nContent length: " + request.getBody().getContentLength() +
                                "\nText content: " + request.getBody().getTxtContent() +
                                "\nBin content: " + Arrays.toString(request.getBody().getBinContent()));
                        writer.flush();
                    }
                });

        Server.start(config);
    }
}