package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BehandlingPaVent extends BehandlingIdPost{
    
    protected LocalDate frist;
    protected Kode ventearsak;
    
    
    public BehandlingPaVent(int behandlingId, int behandlingVersjon, LocalDate frist, Kode ventearsak) {
        super(behandlingId, behandlingVersjon);
        this.frist = frist;
        this.ventearsak = ventearsak;
    }
    
    public BehandlingPaVent(Behandling behandling, LocalDate frist, Kode ventearsak) {
        this(behandling.id, behandling.versjon, frist, ventearsak);
    }
}
