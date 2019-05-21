package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.medlem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.BekreftetForelder;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Medlem {
    protected List<BekreftetForelder> bekrefteteForeldre = new ArrayList<>();
    protected Object bosattVurdering;
    protected Object erEosBorger;
    protected List<Object> inntekt;
    protected Object lovligOppholdVurdering;
    protected Object medlemskapManuellVurderingType;
    protected List<Medlemskapsperiode> medlemskapPerioder;
    protected Object oppholdsrettVurdering;
    protected LocalDate skjearingstidspunkt;
    
    public List<Medlemskapsperiode> getMedlemskapPerioder() {
        return medlemskapPerioder;
    }
    
    public void setMedlemskapPerioder(List<Medlemskapsperiode> medlemskapPerioder) {
        this.medlemskapPerioder = medlemskapPerioder;
    }
}
