package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak;

import java.time.LocalDate;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Saldoer {

    protected LocalDate maksDatoUttak;

    protected Map<String, Stonadskontoer> stonadskontoer;

    public LocalDate getMaksDatoUttak() {return this.maksDatoUttak;}

    public Map<String, Stonadskontoer> getStonadskontoer() {return this.stonadskontoer;}
}
