package no.nav.foreldrepenger.fpmock2.kontrakter;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class PersonhendelseDeserializer extends JsonDeserializer<PersonhendelseDto> {

    private String FØDSELS_TYPE = "fødselshendelse";
    private String DØDS_TYPE = "dødshendelse";

    @Override
    public PersonhendelseDto deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        ObjectNode root = mapper.readTree(jp);
        PersonhendelseDto dto = null;

        if(root.has("type") && root.get("type").textValue().equals(FØDSELS_TYPE)){
            dto = mapper.readValue(root.toString(),FødselshendelseDto.class);
        }
        if(root.has("type") && root.get("type").textValue().equals(DØDS_TYPE)){
            dto =  mapper.readValue(root.toString(),DødshendelseDto.class);
        }
        return dto;
    }
}
