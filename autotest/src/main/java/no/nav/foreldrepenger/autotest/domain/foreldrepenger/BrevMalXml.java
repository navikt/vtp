package no.nav.foreldrepenger.autotest.domain.foreldrepenger;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public class BrevMalXml {

    Document doc;
    
    public BrevMalXml (Document root) {
        this.doc = root;
    }
    
    
    public static BrevMalXml fromString(String text) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource source = new InputSource();
        source.setCharacterStream(new StringReader(text));
        return new BrevMalXml(builder.parse(source));
    }
    
    public static BrevMalXml fromFile(String path) throws Exception {
        return fromString(""); //TODO file read
    }
    
    public static BrevMalXml fromResource(String path) throws Exception {
        return fromFile("" + path); //TODO
    }
    
    public boolean isComparable(BrevMalXml otherXml) {
        boolean result = true;
        
        result = result && isComparableNode("fag dagsats", otherXml);
        
        return result;
    }
    
    public boolean isComparableNode(String path, BrevMalXml  otherXml) {
        return getNodeText(path).equals(otherXml.getNodeText(path));
    }
    
    public Element getNode(String path) {
        String[] jumps = path.split(" ");
        Element root = doc.getDocumentElement();
        for (String jump : jumps) {
            root = (Element) root.getElementsByTagName(jump).item(0);
        }
        return root;
    }
    
    public String getNodeText(String path) {
        return getNode(path).getTextContent();
    }
}
