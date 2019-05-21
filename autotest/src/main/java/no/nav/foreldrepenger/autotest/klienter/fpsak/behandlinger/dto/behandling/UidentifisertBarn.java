package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import java.time.LocalDate;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UidentifisertBarn {
    protected LocalDate fodselsdato;
    protected LocalDate dodsdato;

    UidentifisertBarn() {}

    public UidentifisertBarn(LocalDate fodselsdato, LocalDate dodsdato) {
        this.fodselsdato = fodselsdato;
        this.dodsdato = dodsdato;
    }

    public LocalDate getFodselsdato() {return fodselsdato;}

    public Optional<LocalDate> getDodsdato() {return Optional.ofNullable(dodsdato);}
}
