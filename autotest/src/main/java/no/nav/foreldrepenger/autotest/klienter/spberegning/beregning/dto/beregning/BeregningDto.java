package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning;

import no.nav.foreldrepenger.autotest.klienter.spberegning.kodeverk.dto.Kode;

public class BeregningDto {
    Integer id;
    BeregningsgrunnlagDto beregningsgrunnlag;
    String saksnummer;
    Kode tema;
    
    public BeregningDto() {
    }
}
