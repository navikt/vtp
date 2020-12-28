package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arena;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;

import no.nav.foreldrepenger.vtp.testmodell.Feilkode;

public record ArenaModell(Feilkode feilkode,
                          List<ArenaSak> saker) {

    public ArenaModell() {
        this(null, null);
    }

    @JsonCreator
    public ArenaModell(Feilkode feilkode, List<ArenaSak> saker) {
        this.feilkode = feilkode;
        this.saker = Optional.ofNullable(saker).orElse(List.of());
    }
}
