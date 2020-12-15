package no.nav.foreldrepenger.vtp.testmodell.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;

import no.nav.foreldrepenger.vtp.testmodell.felles.Prosent;

public class ProsentDeserializer extends StdDeserializer<Prosent> {

    public ProsentDeserializer() {
        this(null);
    }

    public ProsentDeserializer(Class<Prosent> t) {
        super(t);
    }

    @Override
    public Prosent deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        var treeNode = p.getCodec().readTree(p);
        if (treeNode instanceof IntNode intNode) {
            return new Prosent(intNode.asInt());
        }
        throw new IllegalArgumentException(String.format("Ukjent node type [%s]", treeNode.getClass().getSimpleName()));
    }
 }
