package no.nav.foreldrepenger.vtp.kontrakter.v2;


import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = OrganisasjonDto.class, name = "organisasjon"),
        @JsonSubTypes.Type(value = PrivatArbeidsgiver.class, name = "privateArbeidsgiver")
})
public interface Arbeidsgiver {
}
