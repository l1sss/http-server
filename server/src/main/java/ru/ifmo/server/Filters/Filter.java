package ru.ifmo.server.Filters;
import ru.ifmo.server.Request;
import ru.ifmo.server.Response;

public abstract class Filter {

    Filter nextFilter;
    boolean active = false;
    Request request;
    Response response;
    String path = request.getPath();

    public abstract void init();

    abstract void doFilter(Request request, Response response);

    public abstract void setNextFilter(Filter nextFilter);

    abstract void destroy();


}
