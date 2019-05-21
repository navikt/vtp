package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SammenligningsgrunnlagDto {

    protected LocalDate sammenligningsgrunnlagFom;
    protected LocalDate sammenligningsgrunnlagTom;
    protected Double rapportertPrAar;
    protected int avvikPromille;
}
