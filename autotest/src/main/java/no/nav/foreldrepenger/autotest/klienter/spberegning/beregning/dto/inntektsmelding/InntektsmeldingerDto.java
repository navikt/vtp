package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.inntektsmelding;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InntektsmeldingerDto {

    protected List<InntektsmeldingDto> inntektsmeldinger;
    
    public InntektsmeldingerDto() {
    }
}
