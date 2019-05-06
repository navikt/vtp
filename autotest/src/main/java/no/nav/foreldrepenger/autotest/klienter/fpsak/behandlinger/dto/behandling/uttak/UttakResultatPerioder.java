package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UttakResultatPerioder {
    protected List<UttakResultatPeriode> perioderAnnenpart = List.of();
    protected List<UttakResultatPeriode> perioderSøker = List.of();

    public List<UttakResultatPeriode> getPerioderForSøker() {
        return perioderSøker;
    }
}
