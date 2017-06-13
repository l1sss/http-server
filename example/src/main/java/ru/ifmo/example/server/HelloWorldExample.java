//package ru.ifmo.example.server;
//
//import ru.ifmo.server.*;
//
///**
// * Simple hello world example.
// */
//public class HelloWorldExample {
//    public static void main(String[] args) {
//        ServerConfig config = new ServerConfig()
//                .addHandler("/index", new Handler() {
//                    @Override
//                    public void handle(Request request, Response response) throws Exception {
////                      Writer writer = new OutputStreamWriter(response.getOutputStream());
////                        writer.write(Http.OK_HEADER + "Hello World!");
////                        writer.flush();
////
//
//
//                        response.Redirect("http://vk.com");
//                        // response.Forward("http://habrahabr.ru");
//                    }
//                });
//        config.setPort(8088);
//        Server.start(config);
//
//    }
//}
