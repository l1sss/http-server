package ru.ifmo.example.server;

import ru.ifmo.server.*;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;

/**
 * Simple hello world example.
 */
public class HelloWorldExample {
    public static void main(String[] args) {
        ServerConfig config = new ServerConfig()
                .addHandler("/index", new Handler() {
                    @Override
                    public void handle(Request request, Response response) throws Exception {
                        if (request.getMethod() == HttpMethod.GET) {
                            Writer writer = new OutputStreamWriter(response.getOutputStream());
                            writer.write(Http.OK_HEADER + "Hello GET!\n");
                            writer.flush();
                        }

                        else if (request.getMethod() == HttpMethod.POST) {
                            Writer writer = new OutputStreamWriter(response.getOutputStream());
                            writer.write(Http.OK_HEADER + "Hello POST\n" +
                                    "Content type: " +
                                    request.getBody().getContentType() +
                                    "\nContent length: " +
                                    request.getBody().getContentLength() +
                                    "\n" +
                                    request.getBody().getStringBody());
                            writer.flush();
                        }
                    }
                });

        Server.start(config);
    }
}