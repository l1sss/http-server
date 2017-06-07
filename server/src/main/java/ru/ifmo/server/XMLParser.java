package ru.ifmo.server;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.IOException;
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

    public ServerConfig parse() throws IOException, XMLStreamException, ReflectiveOperationException {
        XMLInputFactory factory = XMLInputFactory.newFactory();

        StringBuilder valueText = new StringBuilder();

        XMLEventReader reader = null;

        try{
            reader = factory.createXMLEventReader(in);

            while (reader.hasNext()){
                XMLEvent event = reader.nextEvent();

                if (event.getEventType() == XMLStreamReader.START_ELEMENT){
                    StartElement startElement = event.asStartElement();

                    Attribute attUrl = startElement.getAttributeByName(new QName("url"));
                    Attribute attClass = startElement.getAttributeByName(new QName("class"));

                    String tagStart = startElement.getName().getLocalPart();

                    if ("handler".equals(tagStart) && attUrl != null && attClass != null)
                        addHandler(attUrl.getValue(), attClass.getValue());

                    else if ("filter".equals(tagStart) && attClass != null){
                        //addFilter(attClass.getValue()); жду фильтра
                    }                                           // надо выбрасывать искл если нет ни хендлеров ни фильтров?
                                                                // или их просто не будет в загрузке?
                }
                else if (event.getEventType() == XMLStreamConstants.CHARACTERS){
                    Characters characters = event.asCharacters();

                    valueText.append(characters.getData());
                }
                else  if (event.getEventType() == XMLStreamConstants.END_ELEMENT){
                    EndElement endElement = event.asEndElement();

                    String tagEnd = endElement.getName().getLocalPart();

                    if (!valueText.toString().trim().isEmpty())
                        reflectiveSet(tagEnd, valueText.toString().trim());

                    valueText.setLength(0);
                }
            }
            return config;
        }
        finally {
            in.close();
            reader.close();
        }
    }
}
