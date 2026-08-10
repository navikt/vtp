package no.nav.foreldrepenger.vtp.kontrakter.person.v2;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/** Polymorf arbeidsgiver-referanse — enten en organisasjon eller en privatperson (fra samme scenario). */
@JsonTypeInfo(use = NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = OrganisasjonDto.class, name = "organisasjon"),
        @JsonSubTypes.Type(value = PrivatArbeidsgiverDto.class, name = "privatArbeidsgiver")
})
public sealed interface ArbeidsgiverDto permits OrganisasjonDto, PrivatArbeidsgiverDto {
}

