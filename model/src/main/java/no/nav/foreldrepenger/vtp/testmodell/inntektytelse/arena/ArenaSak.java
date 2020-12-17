package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena;

import java.util.List;
import java.util.Optional;

public record ArenaSak(String saksnummer,
                       RelatertYtelseTema tema,
                       SakStatus status,
                       List<ArenaVedtak> vedtak) {

    public ArenaSak(String saksnummer, RelatertYtelseTema tema, SakStatus status, List<ArenaVedtak> vedtak) {
        this.saksnummer = saksnummer;
        this.tema = tema;
        this.status = status;
        this.vedtak = Optional.ofNullable(vedtak).orElse(List.of());
    }
}
