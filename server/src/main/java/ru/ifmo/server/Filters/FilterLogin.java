package ru.ifmo.server.Filters;

import ru.ifmo.server.Http;
import ru.ifmo.server.Request;
import ru.ifmo.server.Response;

public class FilterLogin extends Filter {

    public FilterLogin() {
    }

    public FilterLogin(Request request, Response response) {
        this.request = request;
        this.response = response;
    }


    @Override
    public void init() {
        this.active = true;
    }

    @Override
    void doFilter(Request request, Response response) {
        init();

        if (nextFilter != null)
            nextFilter.doFilter(request, response);
    }

    @Override
    public void setNextFilter(Filter nextFilter) {


    }

    @Override
    void destroy() {
        this.active = false;
    }
}
