package ru.ifmo.server.Filters;

import ru.ifmo.server.Request;
import ru.ifmo.server.Response;

public class RedirectingFilter extends Filter {

    public RedirectingFilter() {
    }

    public RedirectingFilter(Request request, Response response) {
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

            response.sendRedirect("yandex.ru");
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


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
