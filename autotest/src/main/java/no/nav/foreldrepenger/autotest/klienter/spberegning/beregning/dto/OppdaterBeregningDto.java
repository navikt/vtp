package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OppdaterBeregningDto {

    protected String skjæringstidspunkt;
    protected int beregningId;
    protected String aktivitetStatusKode;
    protected Boolean overstyrInntektsmeldinger;
    protected List<Integer> inntektsmeldinger;
    
    public OppdaterBeregningDto() {
    }
}
