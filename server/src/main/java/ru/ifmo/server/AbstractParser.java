package ru.ifmo.server;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import static ru.ifmo.server.MyParse.*;

/**
 * Created by Тарас on 03.06.2017.
 */
public abstract class AbstractParser implements Parser {

    protected ServerConfig config;
    protected InputStream in;

    public AbstractParser(InputStream in) {
        this.in = in;
        config = new ServerConfig();
    }

    protected void addHandler(String url, String className) throws Exception {

        config.addHandler(url, (Handler) Class.forName(className).newInstance());
    }

    protected void addFilter(String className) throws Exception {

        //config.addFilter(className);
    }

    protected void reflectiveSet(String key, String value) throws Exception {
        Method setter = findSetter(key);

        if (setter != null)
            setValue(setter, value);
    }

    private void setValue(Method method, String value) throws ReflectiveOperationException {
        Class<?> paramType = method.getParameterTypes()[0];

        method.invoke(config, getArgument(paramType, value));
    }

    private Object getArgument(Class<?> paramType, String value) {
        Object o;
        if (paramType == int.class || paramType == Integer.class) {
            o = Integer.valueOf(value);
            return o;
        }
        else if (paramType == byte.class || paramType == Byte.class) {
            o = Byte.valueOf(value);
            return o;
        }
        else if (paramType == short.class || paramType == Short.class) {
            o = Short.valueOf(value);
            return o;
        }
        else if (paramType == long.class || paramType == Long.class) {
            o = Long.valueOf(value);
            return o;
        }
        else if (paramType == double.class || paramType == Double.class) {
            o = Double.valueOf(value);
            return o;
        }
        else if (paramType == float.class || paramType == Float.class) {
            o = Float.valueOf(value);
            return o;
        }
        else if (paramType == String.class) // надо ли еще приводить типы?
            return value;

        throw new ServerException("Unsupported type " + paramType);
    }

    private Method findSetter(String param) {
        char[] arr = param.toCharArray();

        arr[0] = Character.toUpperCase(arr[0]);

        String methodName = "set" + new String(arr);

        for (Method method : methods){
            if (methodName.equals(method.getName()) && method.getParameterCount() == 1) {
                return method;
            }
        }
        return null;
    }
}
