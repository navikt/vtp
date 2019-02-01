package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VurderMottarYtelseDto {
    
    protected boolean erFrilans;
    protected boolean frilansMottarYtelse;
    protected double frilansInntektPrMnd;
    protected List<ArbeidstakerUtenInntektsmeldingAndelDto> arbeidstakerAndelerUtenIM;
}
