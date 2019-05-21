package no.nav.foreldrepenger.fpmock2.kontrakter;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = PersonhendelseDeserializer.class)
public interface PersonhendelseDto {

    String getType();
}
