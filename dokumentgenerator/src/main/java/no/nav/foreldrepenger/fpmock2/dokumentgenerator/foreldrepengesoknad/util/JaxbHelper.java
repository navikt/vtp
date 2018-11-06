package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

public final class JaxbHelper {
    private static final Map<Class<?>, JAXBContext> CONTEXTS = new ConcurrentHashMap<>(); // NOSONAR
    private static final Map<String, Schema> SCHEMAS = new ConcurrentHashMap<>(); // NOSONAR
    private static final NamespaceContext NO_NAMESPACE_CONTEXT = new NamespaceContext() {
        @Override
        public String getNamespaceURI(String prefix) {
            return null;
        }

        @Override
        public String getPrefix(String namespaceURI) {
            return "";
        }

        @Override
        public Iterator getPrefixes(String namespaceURI) {
            return null;
        }
    };

    private JaxbHelper() {
    }

    public static String marshalAndValidateJaxb(Object jaxbObject, Map.Entry<Class<?>, Schema> schemaAndClass) throws JAXBException {
        return marshalAndValidateJaxb(jaxbObject, schemaAndClass, true);
    }

    public static String marshalAndValidateJaxb(Object jaxbObject, Map.Entry<Class<?>, Schema> schemaAndClass, boolean formatted)
            throws JAXBException {
        if (!CONTEXTS.containsKey(schemaAndClass.getKey())) {
            CONTEXTS.put(schemaAndClass.getKey(), JAXBContext.newInstance(schemaAndClass.getKey()));
        }
        Marshaller marshaller = CONTEXTS.get(schemaAndClass.getKey()).createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formatted);
        marshaller.setSchema(schemaAndClass.getValue());

        StringWriter writer = new StringWriter();
        marshaller.marshal(jaxbObject, writer);
        return writer.toString();
    }

    public static String marshalAndValidateJaxb(Class<?> clazz, Object jaxbObject, String xsdLocation) throws JAXBException, SAXException {
        if (!CONTEXTS.containsKey(clazz)) {
            CONTEXTS.put(clazz, JAXBContext.newInstance(clazz));
        }
        Marshaller marshaller = CONTEXTS.get(clazz).createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        Schema schema = getSchema(xsdLocation);
        marshaller.setSchema(schema);

        StringWriter writer = new StringWriter();
        marshaller.marshal(jaxbObject, writer);
        return writer.toString();
    }

    public static String marshalAndValidateJaxbWithSchema(Class<?> clazz, Object jaxbObject, Schema schema) throws JAXBException {
        if (!CONTEXTS.containsKey(clazz)) {
            CONTEXTS.put(clazz, JAXBContext.newInstance(clazz));
        }
        Marshaller marshaller = CONTEXTS.get(clazz).createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        if (schema != null) {
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setSchema(schema);
        }

        StringWriter writer = new StringWriter();
        marshaller.marshal(jaxbObject, writer);
        return writer.toString();
    }

    /**
     * Generates xml without namespaces
     * <p>
     * Ex: Method marshalAndValidateJaxb() generates
     * <ns2:brevdata>
     * <ns2:fag>
     * <p>
     * </ns2:fag>
     * </ns2:brevdata>
     * This method does not add any Namespace
     * <brevdata>
     * <fag>
     * <p>
     * </fag>
     * </brevdata>
     *
     * @param xsdLocation XSDLocation to validate marshal operations against or null to disable validation
     */
    public static String marshalNoNamespaceXML(Class<?> clazz, Object jaxbObject, String xsdLocation) throws XMLStreamException, JAXBException, SAXException {
        if (!CONTEXTS.containsKey(clazz)) {
            CONTEXTS.put(clazz, JAXBContext.newInstance(clazz));
        }
        Marshaller marshaller = CONTEXTS.get(clazz).createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        if (xsdLocation != null) {
            Schema schema = getSchema(xsdLocation);
            marshaller.setSchema(schema);
        }

        StringWriter stringWriter = new StringWriter();
        XMLStreamWriter xmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);
        xmlStreamWriter.setNamespaceContext(NO_NAMESPACE_CONTEXT);
        marshaller.marshal(jaxbObject, xmlStreamWriter);
        xmlStreamWriter.flush();
        xmlStreamWriter.close();

        return stringWriter.toString();
    }

    public static String marshalJaxb(Class<?> clazz, Object jaxbObject) throws JAXBException {
        return marshalAndValidateJaxbWithSchema(clazz, jaxbObject, null);
    }

    public static <T> T unmarshalXMLWithStAX(Class<T> clazz, String xml) throws JAXBException, XMLStreamException, SAXException {
        return unmarshalAndValidateXMLWithStAX(clazz, xml, (String) null);
    }

    public static <T> T unmarshalAndValidateXMLWithStAX(Class<T> clazz, String xml, String xsdLocation) throws JAXBException, XMLStreamException, SAXException {
        if (!CONTEXTS.containsKey(clazz)) {
            CONTEXTS.put(clazz, JAXBContext.newInstance(clazz));
        }

        Schema schema = null;
        if (xsdLocation != null) {
            schema = getSchema(xsdLocation);
        }

        return unmarshalAndValidateXMLWithStAXProvidingSchema(clazz, new StreamSource(new StringReader(xml)), schema);
    }

    public static <T> T unmarshalAndValidateXMLWithStAX(Class<T> clazz, String xml, String mainXsdLocation, String[] xsdLocations, Class<?>... classes) throws JAXBException, XMLStreamException, SAXException {
        if (!CONTEXTS.containsKey(clazz)) {
            classes = addIdentifierToBoundClasses(clazz, classes);
            CONTEXTS.put(clazz, JAXBContext.newInstance(classes));
        }

        Schema schema = null;
        if (mainXsdLocation != null && xsdLocations != null) {
            schema = getSchema(mainXsdLocation, xsdLocations);
        }

        return unmarshalAndValidateXMLWithStAXProvidingSchema(clazz, new StreamSource(new StringReader(xml)), schema);
    }

    private static Schema getSchema(String mainXsdLocation, String[] xsdLocations) throws SAXException {
        if (!SCHEMAS.containsKey(mainXsdLocation)) {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            StreamSource[] xsdArray = new StreamSource[xsdLocations.length + 1];
            for (int i = 0; i < xsdLocations.length; i++) {
                final String systemId = JaxbHelper.class.getClassLoader().getResource(xsdLocations[i]).toExternalForm();
                final StreamSource source = new StreamSource(systemId);
                xsdArray[i] = source;
            }
            final String systemId = JaxbHelper.class.getClassLoader().getResource(mainXsdLocation).toExternalForm();
            final StreamSource source = new StreamSource(systemId);
            xsdArray[xsdLocations.length] = source;
            SCHEMAS.putIfAbsent(mainXsdLocation, schemaFactory.newSchema(xsdArray));
        }
        return SCHEMAS.get(mainXsdLocation);
    }

    public static <T> T unmarshalAndValidateXMLWithStAX(Class<T> clazz, String xml, Schema schema)
            throws JAXBException, XMLStreamException {
        if (!CONTEXTS.containsKey(clazz)) {
            CONTEXTS.put(clazz, JAXBContext.newInstance(clazz));
        }

        return unmarshalAndValidateXMLWithStAXProvidingSchema(clazz, new StreamSource(new StringReader(xml)), schema);
    }

    public static <T> T unmarshalAndValidateXMLWithStAXProvidingSchema(Class<T> clazz, String xml, String xsdLocation) throws SAXException, JAXBException, XMLStreamException {
        Schema schema = getSchema(xsdLocation);
        return unmarshalAndValidateXMLWithStAXProvidingSchema(clazz, new StreamSource(new StringReader(xml)), schema);
    }

    public static <T> T unmarshalAndValidateXMLWithStAXProvidingSchema(Class<T> clazz, Source source, Schema schema)
            throws JAXBException, XMLStreamException {
        if (!CONTEXTS.containsKey(clazz)) {
            CONTEXTS.put(clazz, JAXBContext.newInstance(clazz));
        }
        Unmarshaller unmarshaller = CONTEXTS.get(clazz).createUnmarshaller();

        if (schema != null) {
            unmarshaller.setSchema(schema);
        }

        XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
        xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(source);

        JAXBElement<T> root = unmarshaller.unmarshal(xmlStreamReader, clazz);

        return root.getValue();
    }

    private static Schema getSchema(String xsdLocation) throws SAXException {
        if (!SCHEMAS.containsKey(xsdLocation)) {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            final String systemId = JaxbHelper.class.getClassLoader().getResource(xsdLocation).toExternalForm();
            final StreamSource source = new StreamSource(systemId);
            SCHEMAS.putIfAbsent(xsdLocation, schemaFactory.newSchema(source));
        }
        return SCHEMAS.get(xsdLocation);
    }

    public static void clear() {
        CONTEXTS.clear();
        SCHEMAS.clear();
    }

    public static String marshalAndValidateJaxb(Class<?> clazz, Object jaxbObject, String xsdLocation, Class<?>... classes) throws JAXBException, SAXException {
        if (!CONTEXTS.containsKey(clazz)) {
            classes = addIdentifierToBoundClasses(clazz, classes);
            CONTEXTS.put(clazz, JAXBContext.newInstance(classes));
        }

        Schema schema = null;
        if (xsdLocation != null) {
            schema = getSchema(xsdLocation);
        }

        return marshalAndValidateJaxbWithSchema(clazz, jaxbObject, schema);
    }

    public static String marshalAndValidateJaxb(Class<?> clazz, Object jaxbObject, String xsdLocation, String[] additionalXsds, Class<?>... classes) throws JAXBException, SAXException {
        if (!CONTEXTS.containsKey(clazz)) {
            classes = addIdentifierToBoundClasses(clazz, classes);
            CONTEXTS.put(clazz, JAXBContext.newInstance(classes));
        }

        Schema schema = null;
        if (xsdLocation != null && additionalXsds != null) {
            schema = getSchema(xsdLocation, additionalXsds);
        }

        return marshalAndValidateJaxbWithSchema(clazz, jaxbObject, schema);
    }

    public static String marshalJaxb(Class<?> clazz, Object jaxbObject, Class<?>... classes) throws JAXBException, SAXException {
        return marshalAndValidateJaxb(clazz, jaxbObject, null, classes);
    }

    public static <T> T unmarshalXMLWithStAX(Class<T> clazz, Source source, Class<?>... classes) throws JAXBException, XMLStreamException {
        if (!CONTEXTS.containsKey(clazz)) {
            classes = addIdentifierToBoundClasses(clazz, classes);
            CONTEXTS.put(clazz, JAXBContext.newInstance(classes));
        }
        return unmarshalAndValidateXMLWithStAXProvidingSchema(clazz, source, null);
    }

    public static <T> T unmarshalXMLWithStAX(Class<T> clazz, String xml, Class<?>... classes) throws JAXBException, XMLStreamException {
        return unmarshalXMLWithStAX(clazz, new StreamSource(new StringReader(xml)), classes);
    }

    private static Class<?>[] addIdentifierToBoundClasses(Class<?> clazz, Class<?>... classes) {
        Set<Class<?>> classSet = new HashSet<>(Arrays.asList(classes));
        classSet.add(clazz);
        return classSet.toArray(new Class<?>[classSet.size()]);
    }
}