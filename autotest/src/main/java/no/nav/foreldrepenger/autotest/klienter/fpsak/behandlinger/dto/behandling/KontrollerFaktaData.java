package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KontrollerFaktaData {

    private List<KontrollerFaktaPeriode> perioder = null;

    public List<KontrollerFaktaPeriode> getPerioder() {
        return perioder;
    }

    public void setPerioder(List<KontrollerFaktaPeriode> perioder) {
        this.perioder = perioder;
    }
}
