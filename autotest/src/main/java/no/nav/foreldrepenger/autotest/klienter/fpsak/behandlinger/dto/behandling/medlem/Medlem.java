package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.medlem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.BekreftetForelder;

@JsonIgnoreProperties(ignoreUnknown = true)
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
