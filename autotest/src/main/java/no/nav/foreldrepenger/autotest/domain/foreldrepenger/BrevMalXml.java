package no.nav.foreldrepenger.autotest.domain.foreldrepenger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public abstract class BrevMalXml {

    Document doc;
    
    public BrevMalXml (Document root) {
        this.doc = root;
    }
    
    protected static <T extends BrevMalXml> T fromString(String text, Class<T> type) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource source = new InputSource();
        source.setCharacterStream(new StringReader(text));
        return type.getDeclaredConstructor(Document.class).newInstance(builder.parse(source));
    }
    
    protected static <T extends BrevMalXml> T fromFile(String path, Class<T> type) throws Exception {
        File file = new File(path);
        System.out.println(file.getAbsolutePath());
        String data = "";
        
        BufferedReader reader = new BufferedReader(new FileReader(file));
        
        String line;
        while((line = reader.readLine()) != null) {
            data += line;
        }
        
        reader.close();
        
        return fromString(data, type); //TODO file read
    }
    
    protected static <T extends BrevMalXml> T fromResource(String path, Class<T> type) throws Exception {
        return fromFile("src/test/resources/docprodXml/" + path, type);
    }
    
    public abstract boolean isComparable(BrevMalXml otherXml);
    
    public boolean isComparableNode(String path, BrevMalXml  otherXml) {
        String text1 = getNodeText(path);
        String text2 = otherXml.getNodeText(path);
        return text1 != null && text2 != null && text1.equals(text2);
    }
    
    public Element getNode(String path) {
        String[] jumps = path.split(" ");
        Element root = doc.getDocumentElement();
        for (String jump : jumps) {
            root = (Element) root.getElementsByTagName(jump).item(0);
            if(root == null) {
                return null;
            }
        }
        return root;
    }
    
    public String getNodeText(String path) {
        Element node = getNode(path);
        if(node == null) {
            return null;
        }
        return node.getTextContent();
    }
    
    @Override
    public String toString() {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            
            StringWriter stringwriter = new StringWriter();
            StreamResult streamResult = new StreamResult(stringwriter);
            transformer.transform(new DOMSource(doc), streamResult);
            return stringwriter.toString();
            
        }catch (Exception e) {
            return "" + e.getMessage();
        }
        
    }
}
