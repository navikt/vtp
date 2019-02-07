package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VurderMottarYtelseDto {
    
    protected boolean erFrilans;
    protected boolean frilansMottarYtelse;
    protected double frilansInntektPrMnd;
    protected List<ArbeidstakerUtenInntektsmeldingAndelDto> arbeidstakerAndelerUtenIM;


    public boolean isErFrilans() {
        return erFrilans;
    }

    public boolean isFrilansMottarYtelse() {
        return frilansMottarYtelse;
    }

    public double getFrilansInntektPrMnd() {
        return frilansInntektPrMnd;
    }

    public List<ArbeidstakerUtenInntektsmeldingAndelDto> getArbeidstakerAndelerUtenIM() {
        return arbeidstakerAndelerUtenIM;
    }
}
