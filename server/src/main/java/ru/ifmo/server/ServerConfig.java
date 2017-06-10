package ru.ifmo.server;

import com.sun.org.apache.regexp.internal.RE;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Holds server configs: local port, handler mappings, etc.
 */

public class ServerConfig {
    ExecutorService handlerReflectParam;
    /** Default local port. */
    public static final int DFLT_PORT = 8080;

    private int port = DFLT_PORT;
    private Map<String, Handler> handlers;
    private int socketTimeout;

    public ServerConfig() {
        handlers = new HashMap<>();
    }

    public ServerConfig(ServerConfig config) {
        this();

        port = config.port;
        handlers = new HashMap<>(config.handlers);
        socketTimeout = config.socketTimeout;
    }

    /**
     * @return Local port.
     */
    public int getPort() {
        return port;
    }

    /**
     * Define local port.
     *
     * @param port TCP port.
     * @return Itself for chaining.
     */
    public ServerConfig setPort(int port) {
        this.port = port;

        return this;
    }

    /**
     * Add handler mapping.
     *
     * @param path Path which will be associated with this handler.
     * @param handler Request handler.
     * @return Itself for chaining.
     */
    public ServerConfig addHandler(String path, Handler handler) {
        handlers.put(path, handler);

        return this;
    }

    /**
     * Add handler mappings.
     *
     * @param handlers Map paths to handlers.
     * @return Itself for chaining.
     */
    public ServerConfig addHandlers(Map<String, Handler> handlers) {
        this.handlers.putAll(handlers);

        return this;
    }

    Handler handler(String path) {
        return handlers.get(path);
    }

    /**
     * @return Current handler mapping.
     */
    public Map<String, Handler> getHandlers() {
        return handlers;
    }

    /**
     * Set handler mappings.
     *
     * @param handlers Handler mappings.
     *
     */



//private class Reflect {
   // public AddHandlerclass(String )




    public void setHandlers(Map<String, Handler> handlers) {
        this.handlers = handlers;
    }
;

private class clazz  {



    boolean ClassLoader() {return clazz.class.isAssignableFrom(Handler.class);} //boolean
                  void ConstructorLoader () {  Handler.class.getConstructors();} //[]
    Map c =.class.forName("ReflHandler");


}

    /*
     Automatic handler creation with reflection.
 User should be free of handler creation on configuration stage. Add proper methods in ServerConfig:
 ServerConfig.addHandlerClass(String resource, Class<? extends Handler> hndCls): ServerConfig
 ServerConfig.addHandlerClasses(Map<String, Class<? extends Handler> handlers): ServerConfig
 // They should match regular handler configurations.

 Add annotation or/and interface that will force process handler only in one thread
 (f.e. @SingleThreaded, SingleThreaded). Create it once but associate with specific thread.

      */

    /*
    private class handlerReflectParam implements Runnable{
        void beginReflect() {
            handlerReflectParam = Executors.newSingleThreadExecutor(new ServerThreadFactory
                    ("handler-reflect"));}

        public void run() {
            handlerReflectParam.execute(new Runnable() {
                @Override
                public void run() {

                    if (Handler.class != null)

                    {}
                    
                }


            });
        }

    } */
    /**
     * @return Socket timeout value.
     */
    public int getSocketTimeout() {
        return socketTimeout;
    }

    /**
     * Set socket timeout. By default it's unlimited.
     *
     * @param socketTimeout Socket timeout, 0 means no timeout.
     * @return Itself for chaining.
     */
    public ServerConfig setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;

        return this;
    }


    @Override
    public String toString() {
        return "ServerConfig{" +
                "port=" + port +
                ", handlers=" + handlers +
                ", socketTimeout=" + socketTimeout +
                '}';
    }
}
