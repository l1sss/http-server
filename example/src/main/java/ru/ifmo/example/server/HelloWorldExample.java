package ru.ifmo.example.server;

import ru.ifmo.server.*;
import ru.ifmo.server.Filter;

import java.io.IOException;
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
                            Writer writer = new OutputStreamWriter(response.getOutputStream());
                            writer.write(Http.OK_HEADER + "Hello FilterChain! ; )\n");
                            writer.flush();}
                })
                .addHandler("/test", new Handler() {
                    @Override
                    public void handle(Request request, Response response) throws Exception {
                        Writer writer = new OutputStreamWriter(response.getOutputStream());
                        writer.write(Http.OK_HEADER + request.getHeaders());
                        writer.flush();
                    }
                });

        Filter filter1 =  new HeaderFilter("Filter1",1);
        Filter filter2 =  new HeaderFilter("Filter2",2);
        Filter filter3 =  new HeaderFilter("Filter3",3);

        config.setFilters(filter1,filter2,filter3);

        Server.start(config);
    }

    public static class HeaderFilter extends Filter{

        String value = null;
        int x=1;
        public HeaderFilter(String string, int x){
            this.value = string;
            this.x = x;
        }
        @Override
        public void doFilter(Request request, Response response) throws IOException {
            System.out.println("Filter " + x + " before");

            request.addHeader("HeaderFromFilter"+x,value);

            nextFilter.doFilter(request, response);

            System.out.println("Filter " + x + " after");
        }
    }
}
