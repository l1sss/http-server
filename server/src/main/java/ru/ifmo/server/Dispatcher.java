package ru.ifmo.server;

/**
 * Created by SLimZik on 21.05.2017.
 */
public interface Dispatcher{
    String dispatch(Request request, Response response) throws Exception;
}
