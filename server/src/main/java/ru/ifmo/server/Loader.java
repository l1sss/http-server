package ru.ifmo.server;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by Тарас on 06.06.2017.
 */
public class Loader {

    private static final String PROPERTIES_FILE_NAME = "web-server.properties";
    private static final String XML_FILE_NAME = "web-server.xml";

    public ServerConfig load(String path) throws ServerException{
        try {
            //final ServerConfig config;

            InputStream in;
            ConfigType type;

            if (path != null){
                in = new FileInputStream(path);

                if (path.endsWith(".xml"))
                    type = ConfigType.XML;

                else if (path.endsWith(".properties"))
                    type = ConfigType.PROPERTIES;

                else throw new ServerException("Unsupported file format: " + path + ". Supported xml or properties");
            }
            else {
                in = MyParse.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);//ищет файл

                if (in != null) {
                    type = ConfigType.PROPERTIES;
                }
                else {
                    in = MyParse.class.getClassLoader().getResourceAsStream(XML_FILE_NAME);//ищет файл

                    if (in != null) {
                        type = ConfigType.XML;
                    }

                    else throw new ServerException("File not found");// берем значения по умолчанию?
                }
            }

            return parse(in, type);

        } catch (Exception e) {
            throw new ServerException(e.getMessage(), e);
        }
    }

    private ServerConfig parse(InputStream in, ConfigType type) throws Exception {
        assert in != null;
        assert type != null;

        switch (type) {
            case XML:
                return new XMLParser(in).parse();
            case PROPERTIES:
                return new PropParser(in).parse();
        }

        throw new ServerException("Unsupported properties type: " + type);
    }

    private enum ConfigType {
        XML, PROPERTIES
    }
}
