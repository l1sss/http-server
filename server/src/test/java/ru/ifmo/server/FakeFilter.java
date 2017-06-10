package ru.ifmo.server;

import java.io.IOException;

/**
 * Created by Тарас on 10.06.2017.
 */
public class FakeFilter extends Filter{
    String value = null;
    int x = 1;

    public FakeFilter(String string, int x) {
        this.value = string;
        this.x = x;
    }

    @Override
    public void doFilter(Request request, Response response) throws IOException {
        System.out.println("Filter " + x + " before");

        //request.addHeader("HeaderFromFilter" + x, value);

        nextFilter.doFilter(request, response);

        System.out.println("Filter " + x + " after");
    }
}
