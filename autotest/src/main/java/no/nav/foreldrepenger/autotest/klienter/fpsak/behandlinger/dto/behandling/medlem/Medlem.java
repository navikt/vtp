package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.medlem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.BekreftetForelder;

public class Medlem {
    public List<BekreftetForelder> bekrefteteForeldre = new ArrayList<>();
    public Object bosattVurdering;
    public Object erEosBorger;
    public List<Object> inntekt;
    public Object lovligOppholdVurdering;
    public Object medlemskapManuellVurderingType;
    public List<Medlemskapsperiode> medlemskapPerioder;
    public Object oppholdsrettVurdering;
    public LocalDate skjearingstidspunkt;
}
