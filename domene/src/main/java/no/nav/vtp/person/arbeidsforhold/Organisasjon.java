package no.nav.vtp.person.arbeidsforhold;

import java.time.LocalDate;

import no.nav.vtp.person.ident.Orgnummer;

public record Organisasjon(Orgnummer orgnummer, String arbeidsforholdId, Detaljer informasjon) implements Arbeidsgiver {

    @Override
    public String identifikator() {
        return orgnummer.value();
    }

    public record Detaljer(String navn, LocalDate registreringsdato) {
    }
}
