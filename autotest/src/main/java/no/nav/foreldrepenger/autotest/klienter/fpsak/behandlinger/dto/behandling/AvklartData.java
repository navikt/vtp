package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AvklartData {
    public LocalDate fodselsdato;
    public int antallBarnFodsel;
    public LocalDate termindato;
    public int antallBarnTermin;
    public LocalDate utstedtdato;
}
