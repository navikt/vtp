package no.nav.omsorgspenger.rammemeldinger;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger.OverføringGitt;

public record OverføringerResponse(List<OverføringGitt> gitt, List<OverføringFåttRammemelding> fått) {

    public record OverføringFåttRammemelding(LocalDate gjennomført,
                                             LocalDate gyldigFraOgMed,
                                             LocalDate gyldigTilOgMed,
                                             Duration lengde,
                                             Person fra,
                                             List<Kilde> kilder) {


        public record Kilde(String id, String type) {
        }

        public record Person(String id,
                             String type,
                             LocalDate fødselsdato) {
        }
    }
}
