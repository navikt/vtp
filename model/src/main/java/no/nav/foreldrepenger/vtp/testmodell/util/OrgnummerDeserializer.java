package no.nav.foreldrepenger.vtp.testmodell.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.TextNode;

import no.nav.foreldrepenger.vtp.testmodell.felles.Orgnummer;

public class OrgnummerDeserializer extends StdDeserializer<Orgnummer> {

    public OrgnummerDeserializer() {
         this(null);
     }

    public OrgnummerDeserializer(Class<Orgnummer> t) {
        super(t);
    }

    @Override
    public Orgnummer deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        var treeNode = p.getCodec().readTree(p);
        if (treeNode instanceof TextNode textNode) {
            return new Orgnummer(textNode.asText());
        }
        if (treeNode instanceof IntNode intNode) {
            return new Orgnummer(intNode.asText());
        }
        throw new IllegalArgumentException(String.format("Ukjent node type [%s]", treeNode.getClass().getSimpleName()));
    }
 }
