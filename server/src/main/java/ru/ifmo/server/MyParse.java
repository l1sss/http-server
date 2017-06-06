package ru.ifmo.server;

import com.sun.org.apache.bcel.internal.util.ClassPath;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * Created by Тарас on 28.05.2017.
 */
public class MyParse {
    static ServerConfig config = new ServerConfig();

    static Class configClass = config.getClass();
    static Method[] methods = configClass.getMethods();

    public static void main(String[] args) throws Exception {
         // для тестов
        File tempFile = File.createTempFile("web-server", ".properties");//создается файл в temp

        tempFile.deleteOnExit();//файл удаляется по завершении
        // copy from in to tempFile
        String absolutePath = tempFile.getAbsolutePath();//берется путь к темпФайлу
        // pass to loader absolutePath

        Loader loader = new Loader();

        System.out.println(loader.loader().toString());
    }
}
