package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex;

import com.fasterxml.jackson.annotation.JsonAlias;

public record Arbeidsforhold(@JsonAlias("arbeidsgiverOrgnr") Orgnummer orgnr,
                             @JsonAlias("inntektForPerioden") Integer inntekt,
                             Inntektsperiode inntektsperiode,
                             Boolean refusjon) {
}
