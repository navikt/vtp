package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AvklartData {
    protected LocalDate fodselsdato;
    protected int antallBarnFodsel;
    protected LocalDate termindato;
    protected int antallBarnTermin;
    protected LocalDate utstedtdato;
}
