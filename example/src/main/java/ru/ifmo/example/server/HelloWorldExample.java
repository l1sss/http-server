package ru.ifmo.example.server;

import ru.ifmo.server.*;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Simple hello world example.
 */
public class HelloWorldExample {
    public static void main(String[] args) {
        ServerConfig config = new ServerConfig()
                .addHandler("/test", new Handler() {
                    @Override
                    public void handle(Request request, Response response) throws Exception {
                        if (request.getMethod() == HttpMethod.GET) {
                            Writer writer = new OutputStreamWriter(response.getOutputStream());
                            writer.write(Http.OK_HEADER + "Hello GET!\n");
                            writer.flush();
                        }

                        else if (request.getMethod() == HttpMethod.POST) {
                            if (request.getBody().getBinContent() != null) {
                                String contentPath = "/home/l1s/IdeaProjects/http-server-master/bincontent/";
                                String name = "test";

                                try (FileOutputStream fos = new FileOutputStream(contentPath + name)) {
                                    byte[] content = request.getBody().getBinContent();
                                    fos.write(content);
                                    fos.flush();
                                }
                            }

                            Writer writer = new OutputStreamWriter(response.getOutputStream());

                            writer.write(Http.OK_HEADER +
                                    "Content type: " +
                                    request.getBody().getContentType() +
                                    "\nContent length: " +
                                    request.getBody().getContentLength() +
                                    "\nContent format: " +
                                    request.getBody().getContentFormat());

                            writer.flush();
                        }
                    }
                });

        Server.start(config);
    }
}