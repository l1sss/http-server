package ru.ifmo.example.server;

import ru.ifmo.server.*;
import ru.ifmo.server.Filters.EncodingFilter;
import ru.ifmo.server.Filters.LoginFilter;
import ru.ifmo.server.Filters.RedirectingFilter;

import java.io.OutputStreamWriter;
import java.io.Writer;

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
                            writer.write(Http.OK_HEADER + "Hello POST!\n" +
                                    "Content type: " +
                                    request.getBody().getContentType() +
                                    "\nContent length: " +
                                    request.getBody().getContentLength() +
                                    "\nBody: " +
                                    request.getBody().getStringBody());
                            writer.flush();
                        }
                    }
                });
        config.setFilters(new LoginFilter(),new EncodingFilter(),new RedirectingFilter());

        Server.start(config);
    }
}
