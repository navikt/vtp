package no.nav.foreldrepenger.fpmock2.testmodell.feed;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class HendelseSerializer extends JsonSerializer<PersonHendelse> {


    @Override
    public void serialize(PersonHendelse personHendelse, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {




    }
}
