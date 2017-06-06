package ru.ifmo.server;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Тарас on 03.06.2017.
 */
public class XMLParser extends AbstractParser {
    public XMLParser(InputStream in) {
        super(in);
    }

    public ServerConfig parse()throws Exception{
        XMLInputFactory factory = XMLInputFactory.newFactory();

        XMLEventReader reader = factory.createXMLEventReader(in); // надо ли оборачивать в try/finaly?

        StringBuilder valueText = new StringBuilder();

        while (reader.hasNext()){
            XMLEvent event = reader.nextEvent();

            if (event.getEventType() == XMLStreamReader.START_ELEMENT){
                StartElement startElement = event.asStartElement();

                Attribute attUrl = startElement.getAttributeByName(new QName("url"));
                Attribute attClass = startElement.getAttributeByName(new QName("class"));

                if (attUrl != null && attClass != null)
                    addHandler(attUrl.getValue(), attClass.getValue());

                else if (attClass != null){
                    //addFilter(attClass.getValue()); жду фильтра
                }
            }

            else if (event.getEventType() == XMLStreamConstants.CHARACTERS){
                Characters characters = event.asCharacters();

                valueText.append(characters.getData());
            }
            else  if (event.getEventType() == XMLStreamConstants.END_ELEMENT){
                EndElement endElement = event.asEndElement();

                String valueEnd = endElement.getName().getLocalPart();

                if (!valueText.toString().trim().isEmpty())
                    reflectiveSet(valueEnd, valueText.toString().trim());

                valueText.setLength(0);
            }
        }

        in.close();
        reader.close();

        return config;
    }
}
