package no.nav.foreldrepenger.vtp.kontrakter.person.arbeidsforhold;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = OrganisasjonDto.class, name = "organisasjon"),
        @JsonSubTypes.Type(value = PrivatArbeidsgiverDto.class, name = "privatArbeidsgiver")
})
public sealed interface ArbeidsgiverDto permits OrganisasjonDto, PrivatArbeidsgiverDto {
    String identifikator();
}

