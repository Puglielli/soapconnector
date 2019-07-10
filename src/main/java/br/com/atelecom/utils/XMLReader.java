package br.com.atelecom.utils;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;

import static javax.xml.xpath.XPathConstants.STRING;

public class XMLReader {

    public static String readTagContent(String tagName, String xml) {
        try {
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();

            String expression = "//" + tagName;

            XPathExpression expr = xpath.compile(expression);
            return expr.evaluate(getDocumentXML(xml), STRING).toString();
        } catch (Exception ex) {
            return "not found";
        }
    }

    private static Document getDocumentXML(String response) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            return factory.newDocumentBuilder().parse(new InputSource(new StringReader(response)));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }
}