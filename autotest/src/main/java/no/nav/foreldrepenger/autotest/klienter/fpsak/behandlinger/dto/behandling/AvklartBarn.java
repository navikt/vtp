package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AvklartBarn {
    protected LocalDate fodselsdato;
    protected LocalDate dodsdato;

    public AvklartBarn(LocalDate fodselsdato, LocalDate dodsdato) {
        this.fodselsdato = fodselsdato;
        this.dodsdato = dodsdato;
    }

    public LocalDate getFodselsdato() {return fodselsdato;}

    public LocalDate getDodsdato() {return dodsdato;}

}
