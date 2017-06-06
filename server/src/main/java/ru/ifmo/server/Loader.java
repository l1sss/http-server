package ru.ifmo.server;

import java.io.FileInputStream;
import java.io.InputStream;

import static ru.ifmo.server.MyParse.*;

/**
 * Created by Тарас on 06.06.2017.
 */
public class Loader {
    protected String path = "";

    public Loader(String path){
        this.path = path;
    }

    public Loader(){
        super();
    }

    public ServerConfig loader()throws Exception{
        //path = "D:\\Код\\Java\\ifmo\\diploma\\http-server\\server\\src\\main\\resources\\myProp.properties";
        path = "D:\\Код\\Java\\ifmo\\diploma\\http-server\\server\\src\\main\\resources\\myXML.xml";

        if (!path.isEmpty()){
            if (path.endsWith(".xml"))
                config = new XMLParser(new FileInputStream(path)).parse();

            else if (path.endsWith(".properties"))
                config = new PropParser(new FileInputStream(path)).parse();

            else System.out.println("Неправильный адрес файла");
        }
        else{
            InputStream in = MyParse.class.getClassLoader().getResourceAsStream("myProp.properties");//ищет файл

            if (in != null) {
                config = new PropParser(in).parse();
            }
            else {
                in = MyParse.class.getClassLoader().getResourceAsStream("myXML.xml");//ищет файл

                if (in != null) {
                    config = new XMLParser(in).parse();
                }
                else throw new ServerException("File not found");// берем значения по умолчанию?
            }
        }
        return config;
    }
}
