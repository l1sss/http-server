package ru.ifmo.server.Filters;

import ru.ifmo.server.Request;
import ru.ifmo.server.Response;
import ru.ifmo.server.ServerConfig;

public class FilterChar extends Filter {

    public FilterChar() {
    }

    public FilterChar(Request request, Response response) {
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
        request.getHeaders();


        if (nextFilter != null)
            nextFilter.doFilter(request, response);
    }

    @Override
    public void setNextFilter(Filter nextFilter) {
        this.nextFilter = nextFilter;
    }

    @Override
    void destroy() {
        this.active = false;
    }
}
