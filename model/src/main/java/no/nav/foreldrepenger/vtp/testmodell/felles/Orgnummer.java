package no.nav.foreldrepenger.vtp.testmodell.felles;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import no.nav.foreldrepenger.vtp.testmodell.util.OrgnummerDeserializer;

@JsonDeserialize(using = OrgnummerDeserializer.class)
public record Orgnummer(String orgnummer) {

    @JsonValue
    public String orgnummer() {
        return orgnummer;
    }
}
