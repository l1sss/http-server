package ru.ifmo.example.server;

import ru.ifmo.server.*;

import java.io.IOException;

/**
 * simple example, with demonstration
 * of the filtering the query.
 */

public class FilterExample {

    public static void main(String[] args) {
        ServerConfig config = new ServerConfig()
                .addHandler("/filter", new Handler() {
                    @Override
                    public void handle(Request request, Response response) throws Exception {

                       response.getWriter().write(Http.OK_HEADER + request.getHeaders());

                    }
                });

        Filter filter1 = new FilterExample.HeaderFilter("Filter1", 1);
        Filter filter2 = new FilterExample.HeaderFilter("Filter2", 2);
        Filter filter3 = new FilterExample.HeaderFilter("Filter3", 3);

        config.setFilters(filter1, filter2, filter3);

        Server.start(config);
    }

    public static class HeaderFilter extends Filter {

        String value = null;
        int x = 1;

        public HeaderFilter(String string, int x) {
            this.value = string;
            this.x = x;
        }

        @Override
        public void doFilter(Request request, Response response) throws IOException {
            System.out.println("Filter " + x + " before");

            request.addHeader("HeaderFromFilter" + x, value);

            nextFilter.doFilter(request, response);

            System.out.println("Filter " + x + " after");
        }
    }
}