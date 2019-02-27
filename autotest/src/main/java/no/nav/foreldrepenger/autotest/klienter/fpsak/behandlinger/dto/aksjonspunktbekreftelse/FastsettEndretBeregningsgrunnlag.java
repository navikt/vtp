package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag.BeregningsgrunnlagPeriodeDto;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag.BeregningsgrunnlagPrStatusOgAndelDto;

class FastsettEndretBeregningsgrunnlag {

    protected List<FastsettEndretBeregningsgrunnlagPeriode> endretBeregningsgrunnlagPerioder = new ArrayList<>();


    public FastsettEndretBeregningsgrunnlag(List<FastsettEndretBeregningsgrunnlagPeriode> endretBeregningsgrunnlagPerioder) {
        this.endretBeregningsgrunnlagPerioder = endretBeregningsgrunnlagPerioder;
    }

    FastsettEndretBeregningsgrunnlag() {
    }

    void leggTilAndelTilPeriode(BeregningsgrunnlagPeriodeDto periode, BeregningsgrunnlagPrStatusOgAndelDto andel, FastsatteVerdier fastsatteVerdier) {
        Optional<FastsettEndretBeregningsgrunnlagPeriode> eksisterendePeriode = endretBeregningsgrunnlagPerioder.stream()
                .filter(p -> p.fom.isEqual(periode.getBeregningsgrunnlagPeriodeFom())).findFirst();
        if (eksisterendePeriode.isPresent()) {
            eksisterendePeriode.get().leggTilAndel(andel, fastsatteVerdier);
        }
        endretBeregningsgrunnlagPerioder.add(new FastsettEndretBeregningsgrunnlagPeriode(andel, fastsatteVerdier,
                periode.getBeregningsgrunnlagPeriodeFom(), periode.getBeregningsgrunnlagPeriodeTom()));
    }

    public List<FastsettEndretBeregningsgrunnlagPeriode> getEndretBeregningsgrunnlagPerioder() {
        return endretBeregningsgrunnlagPerioder;
    }

    public void setEndretBeregningsgrunnlagPerioder(List<FastsettEndretBeregningsgrunnlagPeriode> endretBeregningsgrunnlagPerioder) {
        this.endretBeregningsgrunnlagPerioder = endretBeregningsgrunnlagPerioder;
    }
}
