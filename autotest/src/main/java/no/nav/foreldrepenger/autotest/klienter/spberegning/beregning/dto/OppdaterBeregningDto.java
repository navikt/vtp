package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OppdaterBeregningDto {

    protected LocalDate skjæringstidspunkt;
    protected int beregningId;
    protected String aktivitetStatusKode;
    protected Boolean overstyrInntektsmeldinger;
    protected List<Integer> inntektsmeldinger;
    
    public OppdaterBeregningDto(int beregningId) {
        this.beregningId = beregningId;
    }

    public void setSkjæringstidspunkt(LocalDate skjæringstidspunkt) {
        this.skjæringstidspunkt = skjæringstidspunkt;
    }

    public void setBeregningId(int beregningId) {
        this.beregningId = beregningId;
    }

    public void setAktivitetStatusKode(String aktivitetStatusKode) {
        this.aktivitetStatusKode = aktivitetStatusKode;
    }

    public void setOverstyrInntektsmeldinger(Boolean overstyrInntektsmeldinger) {
        this.overstyrInntektsmeldinger = overstyrInntektsmeldinger;
    }

    public void setInntektsmeldinger(List<Integer> inntektsmeldinger) {
        this.inntektsmeldinger = inntektsmeldinger;
    }
    
    
}
