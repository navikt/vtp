package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KunYtelseDto {

    protected List<BrukersAndelDto> andeler;
    protected boolean fodendeKvinneMedDP;
    protected boolean erBesteberegning;
}
