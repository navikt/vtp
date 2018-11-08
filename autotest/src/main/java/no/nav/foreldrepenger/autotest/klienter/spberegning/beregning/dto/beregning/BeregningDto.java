package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.spberegning.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BeregningDto {
    Integer id;
    BeregningsgrunnlagDto beregningsgrunnlag;
    String saksnummer;
    Kode tema;
    
    public BeregningDto() {
    }

    public BeregningsgrunnlagDto getBeregningsgrunnlag() {
        return beregningsgrunnlag;
    }

    public String getSaksnummer() {
        return saksnummer;
    }

    public Kode getTema() {
        return tema;
    }
}
