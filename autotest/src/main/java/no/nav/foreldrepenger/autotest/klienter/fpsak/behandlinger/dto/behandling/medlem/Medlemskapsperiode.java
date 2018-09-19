package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.medlem;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Medlemskapsperiode {
    public Object beslutningsdato;
    public Kode dekningType;
    public LocalDate fom;
    public LocalDate tom;
    public Kode kildeType;
    public Kode medlemskapType;
}
