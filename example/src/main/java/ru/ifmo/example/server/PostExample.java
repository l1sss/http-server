package ru.ifmo.example.server;

import ru.ifmo.server.*;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by l1s on 13.06.17.
 */
public class PostExample {
    public static void main(String[] args) {
        ServerConfig config = new ServerConfig()
                .addHandler("/post", new Handler() {
                    @Override
                    public void handle(Request request, Response response) throws Exception {
                        if (request.getBody().getBinContent() != null) {
                            File file = new File("/home/l1s/test/img." + request.getBody().getContentFormat());

                            try (FileOutputStream fout = new FileOutputStream(file)) {
                                byte[] content = request.getBody().getBinContent();
                                fout.write(content);
                                fout.flush();
                            }
                            response.getWriter().write("Content saved\n");
                        }

                        if (request.getBody().getTxtContent() != null) {
                            response.getWriter().write(request.getBody().getTxtContent());
                        }
                    }
                });

        Server.start(config);
    }
}