package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InntektPrAndel {

    protected Integer inntekt;
    protected Long andelsnr;

    public InntektPrAndel(Integer inntekt, Long andelsnr) {
        super();
        this.inntekt = inntekt;
        this.andelsnr = andelsnr;
    }

    public Integer getInntekt() {
        return inntekt;
    }

    public Long getAndelsnr() {
        return andelsnr;
    }
}