package ru.ifmo.example.server;

import ru.ifmo.server.*;

/**
 * Created by HomePC on 07.06.2017.
 */
public class ExampleRedirect {
    public static void main(String[] args) {
        ServerConfig config= new ServerConfig()
                .addHandler("/index", new Handler() {
                    @Override
                    public void handle(Request request, Response response) throws Exception {
                        response.Redirect("http://google.ru");
                    }
                });
        config.setPort(8088);
        Server.start(config);



    }


}
