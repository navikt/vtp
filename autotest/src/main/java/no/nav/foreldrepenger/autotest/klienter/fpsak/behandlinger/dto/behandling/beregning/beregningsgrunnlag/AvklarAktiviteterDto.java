package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AvklarAktiviteterDto {

    protected VentelønnVartpengerDto ventelonnVartpenger;

    public VentelønnVartpengerDto getVentelonnVartpenger() {
        return ventelonnVartpenger;
    }
}
