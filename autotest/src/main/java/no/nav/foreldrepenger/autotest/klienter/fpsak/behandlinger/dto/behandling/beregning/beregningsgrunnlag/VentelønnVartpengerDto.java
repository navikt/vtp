package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VentelønnVartpengerDto {

    protected Boolean inkludert;

    public Boolean getInkludert() {
        return inkludert;
    }
}
