package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex;

import com.fasterxml.jackson.annotation.JsonAlias;

import no.nav.foreldrepenger.vtp.testmodell.felles.Orgnummer;

public record Arbeidsforhold(@JsonAlias("orgnr") Orgnummer orgnr,
                             @JsonAlias("inntektForPerioden") Integer inntekt,
                             Inntektsperiode inntektsperiode,
                             Boolean refusjon) {
}
