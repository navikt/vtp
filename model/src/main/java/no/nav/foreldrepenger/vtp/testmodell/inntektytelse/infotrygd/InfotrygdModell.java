package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;

import no.nav.foreldrepenger.vtp.testmodell.Feilkode;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdBeregningsgrunnlag;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.ytelse.InfotrygdYtelse;

public record InfotrygdModell(Feilkode feilkode,
                              List<InfotrygdYtelse> ytelser,
                              List<InfotrygdBeregningsgrunnlag> grunnlag) {
    public InfotrygdModell() {
        this(null, null, null);
    }

    @JsonCreator
    public InfotrygdModell(Feilkode feilkode, List<InfotrygdYtelse> ytelser, List<InfotrygdBeregningsgrunnlag> grunnlag) {
        this.feilkode = feilkode;
        this.ytelser = Optional.ofNullable(ytelser).orElse(List.of());
        this.grunnlag = Optional.ofNullable(grunnlag).orElse(List.of());
    }
}
