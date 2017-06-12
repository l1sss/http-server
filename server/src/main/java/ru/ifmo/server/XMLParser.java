package ru.ifmo.server;

import ru.ifmo.server.util.Utils;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.*;
import java.io.InputStream;

/**
 * Created by Тарас on 03.06.2017.
 */
public class XMLParser extends AbstractParser {
    public XMLParser(InputStream in) {
        super(in);
    }

    public ServerConfig parse() throws Exception {
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
                        setFilters(attClass.getValue());
                    }
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
            try {
                if (reader != null)
                    reader.close();
            } finally {
                Utils.closeQuiet(in);
            }
        }
    }
}
