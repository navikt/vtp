package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BeregningsgrunnlagDto {
    LocalDate skjaeringstidspunktBeregning;
    List<AktivitetStatusDto> aktivitetStatus;
    List<BeregningsgrunnlagPeriodeDto> beregningsgrunnlagPeriode;
    SammenligningsgrunnlagDto sammenligningsgrunnlag;
    String ledetekstBrutto;
    String oppgaveBeskrivelse;
    Long id;
    
    public BeregningsgrunnlagDto() {
    }
}
