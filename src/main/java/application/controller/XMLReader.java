package application.controller;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

public class XMLReader {

    public static ArrayList<String> getTimeArray(){
        ArrayList<String> output=new ArrayList<String>();
        String FILENAME = "application/xml/time_array.xml";
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            // optional, but recommended
            // process XML securely, avoid attacks like XML External Entities (XXE)
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(XMLReader.class.getClassLoader().getResourceAsStream(FILENAME));

            // optional, but recommended
            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            // get <string-array>
            NodeList list = doc.getElementsByTagName("string-array");

            for (int temp = 0; temp < list.getLength(); temp++) {

                Node node = list.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList children = node.getChildNodes();
                    for (int i = 0; i < children.getLength(); i++)
                        if (children.item(i).getNodeName() == "item")
                            output.add(children.item(i).getTextContent());
                }


            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return output;
    }
}
