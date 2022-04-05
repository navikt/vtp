package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex;

import com.fasterxml.jackson.annotation.JsonAlias;

import no.nav.foreldrepenger.vtp.testmodell.felles.Orgnummer;

public record Vedtak(Periode periode,
                     Integer utbetalingsgrad,
                     @JsonAlias("arbeidsgiverOrgnr") Orgnummer orgnr,
                     Boolean erRefusjon,
                     Integer dagsats) {

}
