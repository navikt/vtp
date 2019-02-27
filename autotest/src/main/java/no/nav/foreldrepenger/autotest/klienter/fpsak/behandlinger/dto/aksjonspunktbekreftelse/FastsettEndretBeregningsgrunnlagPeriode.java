package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag.BeregningsgrunnlagPrStatusOgAndelDto;

class FastsettEndretBeregningsgrunnlagPeriode {

    protected List<FastsettEndretBeregningsgrunnlagAndel> andeler = new ArrayList<>();
    protected LocalDate fom;
    protected LocalDate tom;

    public FastsettEndretBeregningsgrunnlagPeriode(List<FastsettEndretBeregningsgrunnlagAndel> andeler, LocalDate fom, LocalDate tom) {
        this.andeler = andeler;
        this.fom = fom;
        this.tom = tom;
    }

    public FastsettEndretBeregningsgrunnlagPeriode(BeregningsgrunnlagPrStatusOgAndelDto andel,
                                                   FastsatteVerdier fastsatteVerdier,
                                                   LocalDate fom, LocalDate tom) {
        leggTilAndel(andel, fastsatteVerdier);
        this.fom = fom;
        this.tom = tom;
    }

    void leggTilAndel(BeregningsgrunnlagPrStatusOgAndelDto andel, FastsatteVerdier fastsatteVerdier) {
        if (andeler.stream().anyMatch(a -> a.andelsnr == andel.getAndelsnr())) {
            RedigerbarAndel andelInfo = new RedigerbarAndel("Andelsinfo", andel.getAndelsnr(),
                    andel.getArbeidsforhold().getArbeidsforholdId(), true, true);
            andeler.add(new FastsettEndretBeregningsgrunnlagAndel(andelInfo, fastsatteVerdier));
        } else {
            RedigerbarAndel andelInfo = new RedigerbarAndel("Andelsinfo", andel.getAndelsnr(),
                    andel.getArbeidsforhold().getArbeidsforholdId(), false, false);
            andeler.add(new FastsettEndretBeregningsgrunnlagAndel(andelInfo, fastsatteVerdier));
        }
    }

    public List<FastsettEndretBeregningsgrunnlagAndel> getAndeler() {
        return andeler;
    }

    public void setAndeler(List<FastsettEndretBeregningsgrunnlagAndel> andeler) {
        this.andeler = andeler;
    }

    public LocalDate getFom() {
        return fom;
    }

    public void setFom(LocalDate fom) {
        this.fom = fom;
    }

    public LocalDate getTom() {
        return tom;
    }

    public void setTom(LocalDate tom) {
        this.tom = tom;
    }
}
