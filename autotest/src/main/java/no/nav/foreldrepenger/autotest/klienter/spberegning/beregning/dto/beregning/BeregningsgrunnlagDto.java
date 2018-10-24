package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning;

import java.util.List;

public class BeregningsgrunnlagDto {
    String skjaeringstidspunktBeregning;
    List<AktivitetStatusDto> aktivitetStatus;
    List<BeregningsgrunnlagPeriodeDto> beregningsgrunnlagPeriode ;
    SammenligningsgrunnlagDto sammenligningsgrunnlag;
    String ledetekstBrutto;
    String oppgaveBeskrivelse;
    Integer id;
    
    public BeregningsgrunnlagDto() {
    }
}
