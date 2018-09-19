package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.medlem;

import java.time.LocalDate;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

public class Medlemskapsperiode {
    public Object beslutningsdato;
    public Kode dekningType;
    public LocalDate fom;
    public LocalDate tom;
    public Kode kildeType;
    public Kode medlemskapType;
}
