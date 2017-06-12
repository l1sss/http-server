package ru.ifmo.server;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Тарас on 03.06.2017.
 */
public class PropParser extends AbstractParser {
    public PropParser(InputStream in) {
        super(in);
    }

    public ServerConfig parse() throws Exception {
        Properties prop = new Properties();

        try{
            prop.load(in);
        }
        finally {
            in.close();
        }

        for (String key : prop.stringPropertyNames()) {
            if ("handlers".equals(key)) {
                String[] handlers = prop.getProperty(key).split(",");

                for (String handler : handlers){
                    String[] hand = handler.trim().split("=");

                    addHandler(hand[0], hand[1]);
                }
            }
            else if ("filters".equals(key)){
                String[] filters = prop.getProperty(key).split(",");

                for (String filter : filters)
                    setFilters(filter.trim());
            }
            else
                reflectiveSet(key, prop.getProperty(key));
        }

        return config;
    }
}
