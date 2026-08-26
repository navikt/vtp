package no.nav.foreldrepenger.vtp.kontrakter.person.v2;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonValue;

public record OrganisasjonDto(Orgnummer orgnummer, Detaljer detaljer) implements ArbeidsgiverDto {

    public record Orgnummer(@JsonValue String value) {
    }

    public record Detaljer(String navn, LocalDate registreringsdato) {
    }
}
