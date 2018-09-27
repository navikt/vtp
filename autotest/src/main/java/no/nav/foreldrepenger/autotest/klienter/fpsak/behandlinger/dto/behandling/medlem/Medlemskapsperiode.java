package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.medlem;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Medlemskapsperiode {
    protected Object beslutningsdato;
    protected Kode dekningType;
    protected LocalDate fom;
    protected LocalDate tom;
    protected Kode kildeType;
    protected Kode medlemskapType;
}
