package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BeregningsgrunnlagDto {
    protected LocalDate skjaeringstidspunktBeregning;
    protected List<AktivitetStatusDto> aktivitetStatus;
    protected List<BeregningsgrunnlagPeriodeDto> beregningsgrunnlagPeriode;
    protected SammenligningsgrunnlagDto sammenligningsgrunnlag;
    protected String ledetekstBrutto;
    protected String oppgaveBeskrivelse;
    protected Long id;
    protected String opprettet;
    protected Boolean sjømann;
    
    public BeregningsgrunnlagDto() {
    }

    public List<AktivitetStatusDto> getAktivitetStatus() {
        return aktivitetStatus;
    }

    public List<BeregningsgrunnlagPeriodeDto> getBeregningsgrunnlagPeriode() {
        return beregningsgrunnlagPeriode;
    }

    public SammenligningsgrunnlagDto getSammenligningsgrunnlag() {
        return sammenligningsgrunnlag;
    }

    public Boolean getSjømann() {
        return sjømann;
    }

    public String getLedetekstBrutto() {
        return ledetekstBrutto;
    }

    public String getOppgaveBeskrivelse() {
        return oppgaveBeskrivelse;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getSkjaeringstidspunktBeregning() {

        return skjaeringstidspunktBeregning;
    }


}
