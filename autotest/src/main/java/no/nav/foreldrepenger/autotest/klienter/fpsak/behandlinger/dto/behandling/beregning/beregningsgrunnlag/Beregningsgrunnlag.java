package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Beregningsgrunnlag {

    protected List<Kode> aktivitetStatus;
    protected List<BeregningsgrunnlagPeriodeDto> beregningsgrunnlagPeriode;
    protected FaktaOmBeregningDto faktaOmBeregning;
    protected long halvG;
    protected String ledetekstAvkortet;
    protected String ledetekstBrutto;
    protected String ledetekstRedusert;
    protected SammenligningsgrunnlagDto sammenligningsgrunnlag;
    protected LocalDate skjaeringstidspunktBeregning;
    
    public int antallAktivitetStatus() {
        return aktivitetStatus.size();
    }
    
    public Kode getAktivitetStatus(int index) {
        return aktivitetStatus.get(index);
    }
    
    public int antallBeregningsgrunnlagPeriodeDto() {
        return beregningsgrunnlagPeriode.size();
    }
    
    public BeregningsgrunnlagPeriodeDto getBeregningsgrunnlagPeriode(int index) {
        return beregningsgrunnlagPeriode.get(index);
    }

    public LocalDate getSkjaeringstidspunktBeregning() {
        return skjaeringstidspunktBeregning;
    }
}
