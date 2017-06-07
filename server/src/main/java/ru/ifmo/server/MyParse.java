package ru.ifmo.server;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by Тарас on 28.05.2017.
 */
public class MyParse {
    public static void main(String[] args) throws Exception {
         // для тестов
        File tempFile = File.createTempFile("web-server", ".properties");//создается файл в temp

        tempFile.deleteOnExit();//файл удаляется по завершении
        // copy from in to tempFile
        String absolutePath = tempFile.getAbsolutePath();//берется путь к темпФайлу
        // pass to loader absolutePath

        Loader loader = new Loader(); // мой - проверка

        System.out.println(loader.load(null).toString());//мой - проверка

        //path = "D:\\Код\\Java\\ifmo\\diploma\\http-server\\server\\src\\main\\resources\\web-server.properties"; // мой-проверка
        //path = "D:\\Код\\Java\\ifmo\\diploma\\http-server\\server\\src\\main\\resources\\web-server.xml"; // мой - проверка
    }
}
