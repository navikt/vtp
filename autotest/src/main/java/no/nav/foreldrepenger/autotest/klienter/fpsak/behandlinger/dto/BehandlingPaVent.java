package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto;

import java.time.LocalDate;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

public class BehandlingPaVent {
    int behandlingId;
    int behandlingVersjon;
    LocalDate frist;
    Kode ventearsak;
    
    
    public BehandlingPaVent(int behandlingId, int behandlingVersjon, LocalDate frist, Kode ventearsak) {
        super();
        this.behandlingId = behandlingId;
        this.behandlingVersjon = behandlingVersjon;
        this.frist = frist;
        this.ventearsak = ventearsak;
    }
    
    public BehandlingPaVent(Behandling behandling, LocalDate frist, Kode ventearsak) {
        this(behandling.behandlingId, behandling.behandlingVersjon, frist, ventearsak);
    }
    
    
}
