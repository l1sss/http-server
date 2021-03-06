package ru.ifmo.server;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by Тарас on 03.06.2017.
 */
public abstract class AbstractParser implements Parser {

    protected ServerConfig config;
    protected InputStream in;
    private Map<Class<? extends Handler>, Handler> handlers;
    private HashSet<String> filters;

    public AbstractParser(InputStream in) {
        this.in = in;
        config = new ServerConfig();
        handlers = new HashMap<>();
        filters = new HashSet<>();
    }

    @SuppressWarnings("unchecked")
    protected void addHandler(String url, String className) throws ReflectiveOperationException {
        Class<Handler> handlerClass = (Class<Handler>) Class.forName(className);

        Handler handler;
        if (!handlers.containsKey(handlerClass)) {
            handler = handlerClass.getConstructor().newInstance();

            handlers.put(handlerClass, handler);
        }
        else
            handler = handlers.get(handlerClass);

        config.addHandler(url, handler);
    }

    protected void setFilters(String className) throws Exception {

        if (!filters.contains(className)){
            config.setFilters((Filter) Class.forName(className).getConstructor().newInstance());

            filters.add(className);
        }
    }

    protected void reflectiveSet(String key, String value) throws ReflectiveOperationException {
        Method setter = findSetter(key);

        if (setter != null)
            setValue(setter, value);
    }

    private void setValue(Method method, String value) throws ReflectiveOperationException {
        Class<?> paramType = method.getParameterTypes()[0];

        method.invoke(config, getArgument(paramType, value));
    }

    private Object getArgument(Class<?> paramType, String value) throws ClassNotFoundException {
        Object o;
        if (paramType == int.class || paramType == Integer.class)
            return Integer.valueOf(value);

        else if (paramType == byte.class || paramType == Byte.class)
            return Byte.valueOf(value);

        else if (paramType == short.class || paramType == Short.class)
            return Short.valueOf(value);

        else if (paramType == long.class || paramType == Long.class)
            return Long.valueOf(value);

        else if (paramType == double.class || paramType == Double.class)
            return Double.valueOf(value);

        else if (paramType == float.class || paramType == Float.class)
            return Float.valueOf(value);

        else if (paramType == char.class) {
            if (value.length() != 1)
                throw new ServerException("Illegal argument: char. Must be single character.");

            return value.charAt(0);
        }

        else if (paramType == String.class)
            return value;

        else if (paramType == Class.class)
            return Class.forName(value);

        throw new ServerException("Unsupported type " + paramType);
    }

    private Method findSetter(String param) {
        char[] arr = param.toCharArray();

        arr[0] = Character.toUpperCase(arr[0]);

        String methodName = "set" + new String(arr);

        for (Method method : ServerConfig.class.getDeclaredMethods()){
            if (methodName.equals(method.getName()) && method.getParameterCount() == 1) {
                return method;
            }
        }
        return null;
    }
}
