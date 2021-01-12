package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.omsorgspenger;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public record KoronaOverføringFått(LocalDate gjennomført,
                             LocalDate gyldigFraOgMed,
                             LocalDate gyldigTilOgMed,
                             Duration lengde,
                             Person fra,
                             List<Kilde> kilder) {

    public KoronaOverføringFått(LocalDate gjennomført, LocalDate gyldigFraOgMed, LocalDate gyldigTilOgMed, Duration lengde, Person fra, List<Kilde> kilder) {
        this.gjennomført = gjennomført;
        this.gyldigFraOgMed = gyldigFraOgMed;
        this.gyldigTilOgMed = gyldigTilOgMed;
        this.lengde = lengde;
        this.fra = fra;
        this.kilder = Optional.ofNullable(kilder).orElse(List.of());
    }
}
