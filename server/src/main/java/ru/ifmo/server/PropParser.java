package ru.ifmo.server;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

//import static ru.ifmo.server.MyParse.*;

/**
 * Created by Тарас on 03.06.2017.
 */
public class PropParser extends AbstractParser {
    public PropParser(InputStream in) {
        super(in);
    }

    public ServerConfig parse()throws Exception{
        Properties prop = new Properties();

        try{
            prop.load(in); // так обернуть, и сразу закрыть?
        }
        finally {
            in.close();
        }

        for (String key : prop.stringPropertyNames()) {
            if (key.equals("handlers")) {

                String[] handlers = prop.getProperty(key).split(",");

                for (String handler : handlers){
                    String[] hand = handler.trim().split("=");

                    addHandler(hand[0], hand[1]);
                }
            }
            else
                reflectiveSet(key, prop.getProperty(key));
        }
        return config;
    }
}
