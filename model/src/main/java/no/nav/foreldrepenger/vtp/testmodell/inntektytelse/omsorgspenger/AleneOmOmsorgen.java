package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public record AleneOmOmsorgen(LocalDate gjennomført,
                              LocalDate registrert,
                              LocalDate gyldigFraOgMed,
                              LocalDate gyldigTilOgMed,
                              Person barn,
                              List<Kilde> kilder) {

    public AleneOmOmsorgen(LocalDate gjennomført, LocalDate registrert, LocalDate gyldigFraOgMed, LocalDate gyldigTilOgMed, Person barn, List<Kilde> kilder) {
        this.gjennomført = gjennomført;
        this.registrert = registrert;
        this.gyldigFraOgMed = gyldigFraOgMed;
        this.gyldigTilOgMed = gyldigTilOgMed;
        this.barn = barn;
        this.kilder = Optional.ofNullable(kilder).orElse(List.of());
    }
}
