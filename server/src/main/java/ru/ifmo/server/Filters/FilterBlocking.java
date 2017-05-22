package ru.ifmo.server.Filters;

import ru.ifmo.server.Request;
import ru.ifmo.server.Response;

public class FilterBlocking extends Filter {

    public FilterBlocking() {
    }

    public FilterBlocking(Request request, Response response) {
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
        if (this.active) {
            if (request.getPath().equalsIgnoreCase(String.valueOf(Blocked.values()))) {

            }
        }


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

    private enum Blocked {VK, FACEBOOK, YOUTUBE}

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
