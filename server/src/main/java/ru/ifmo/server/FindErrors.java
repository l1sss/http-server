package ru.ifmo.server;

import java.util.Map;

/**
 * Created by Andrew on 14.06.2017.
 */
public class FindErrors {
    public static String findErrorPage(int code, ServerConfig serverConfig) {
        Map<Integer, String> errors = serverConfig.getErrorPages();
        if (errors == null)
            return null;

        if (errors.containsKey(code))
            return errors.get(code);

        return null;
    }
}
