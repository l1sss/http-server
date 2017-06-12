package ru.ifmo.server;

/**
 * Define your implementations of this abstract class, register and
 * define a set of filters use
 * {@link ServerConfig#setFilters(Filter... filters) }.
 * Method {@link #doFilter(Request request, Response response)} will be performs
 * assigned to this filter function.
 * Method {@link #setNextFilter(Filter nextFilter)} will make the chain of filters,
 * which should be executed after the server start, before first handler, and after
 * all handlers.
 *
 * @see ServerConfig
 * @see Server
 */

public abstract class Filter {

    protected Filter nextFilter;

    public abstract void doFilter(Request request, Response response) throws Exception;

    public void setNextFilter(Filter nextFilter) {
        this.nextFilter = nextFilter;
    }


}
