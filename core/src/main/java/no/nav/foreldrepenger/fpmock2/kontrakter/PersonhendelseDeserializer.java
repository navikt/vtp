package no.nav.foreldrepenger.fpmock2.kontrakter;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class PersonhendelseDeserializer extends JsonDeserializer<PersonhendelseDto> {

    private String FØDSELS_TYPE = "fødselshendelse";
    private String DØDS_TYPE = "dødshendelse";
    private String DØDFØDSEL_TYPE = "dødfødselhendelse";

    @Override
    public PersonhendelseDto deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectCodec oc = jp.getCodec();
        ObjectNode root = oc.readTree(jp);
        PersonhendelseDto dto = null;

        ObjectMapper mapper = (ObjectMapper) oc;
        if (root.has("type") && root.get("type").textValue().equals(FØDSELS_TYPE)) {
            dto = mapper.readValue(root.toString(), FødselshendelseDto.class);
        }
        if (root.has("type") && root.get("type").textValue().equals(DØDS_TYPE)) {
            dto =  mapper.readValue(root.toString(), DødshendelseDto.class);
        }
        if (root.has("type") && root.get("type").textValue().equals(DØDFØDSEL_TYPE)) {
            dto =  mapper.readValue(root.toString(), DødfødselhendelseDto.class);
        }
        return dto;
    }
}
