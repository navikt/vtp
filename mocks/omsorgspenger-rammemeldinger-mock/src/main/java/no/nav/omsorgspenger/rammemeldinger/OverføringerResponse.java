package no.nav.omsorgspenger.rammemeldinger;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public record OverføringerResponse(List<OverføringGitt> gitt, List<OverføringFåttRammemelding> fått) {

    public record OverføringGitt(LocalDate gjennomført,
                                 LocalDate gyldigFraOgMed,
                                 LocalDate gyldigTilOgMed,
                                 Duration lengde,
                                 Person til,
                                 List<Kilde> kilder) {
    }


    public record OverføringFåttRammemelding(LocalDate gjennomført,
                                             LocalDate gyldigFraOgMed,
                                             LocalDate gyldigTilOgMed,
                                             Duration lengde,
                                             Person fra,
                                             List<Kilde> kilder) {
    }

    public record Person(String id,
                         String type,
                         LocalDate fødselsdato) {
    }

    public record Kilde(String id, String type) {
    }
}
