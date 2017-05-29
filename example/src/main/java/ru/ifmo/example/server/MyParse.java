package ru.ifmo.example.server;

import javax.xml.stream.*;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Тарас on 28.05.2017.
 */
public class MyParse {
    static HashMap<String, String> map = new HashMap<>();

    public static void main(String[] args) throws Exception {
       parseProperties();

       //parseXML();
        parseProperties();
    }

    public static void parseProperties(){
        Properties prop = new Properties();

        try (FileInputStream in = new FileInputStream("D:\\Код\\Java\\ifmo\\diploma\\http-server\\example\\src\\main\\resources\\myProp.properties")){
            prop.load(in);

            for (String key : prop.stringPropertyNames()){
                map.put(key, prop.getProperty(key));
            }

            for (Map.Entry pair : map.entrySet()){
                System.out.println(pair.getKey() + " = " + pair.getValue());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void parseXML() throws Exception {
        XMLInputFactory factory = XMLInputFactory.newFactory();

        InputStream in = MyParse.class.getClassLoader().getResourceAsStream("myXML.xml");

        XMLEventReader reader = factory.createXMLEventReader(in);

        String valueStart = null;

        StringBuilder valueText = new StringBuilder();

        String valueEnd = null;

        String id = null;

        Map<String, String> map = new HashMap<>();

        while (reader.hasNext()){
            XMLEvent event = reader.nextEvent();

            if (event.getEventType() == XMLStreamReader.START_ELEMENT){
                StartElement startElement = event.asStartElement();

                valueStart = startElement.getName().getLocalPart();

                //id = startElement.getAttributeByName(new QName("user")).getValue();
                //System.out.println(id);
            }

            else if (event.getEventType() == XMLStreamConstants.CHARACTERS){
                Characters characters = event.asCharacters();

                valueText.append(characters.getData());
            }

            else  if (event.getEventType() == XMLStreamConstants.END_ELEMENT){
                EndElement endElement = event.asEndElement();

                valueEnd = endElement.getName().getLocalPart();

                if (valueEnd.equals(valueStart) && !valueText.toString().trim().isEmpty())
                    map.put(valueStart, valueText.toString().trim());

                valueText.setLength(0);
            }
        }

        for (Map.Entry<String, String> pair : map.entrySet())
            System.out.println(pair.getKey() + " = " + pair.getValue());

        in.close();
        reader.close();
    }
}
