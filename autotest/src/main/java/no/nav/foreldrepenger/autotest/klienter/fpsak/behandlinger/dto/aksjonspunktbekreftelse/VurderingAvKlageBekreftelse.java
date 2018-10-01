package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import java.time.LocalDate;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5035")
public class VurderingAvKlageBekreftelse extends AksjonspunktBekreftelse {

    private static final String VURDERING_STADFEST = "STADFESTE_YTELSESVEDTAK";
    private static final String VURDERING_AVVIS = "AVVIS_KLAGE";
    private static final String VURDERING_MEDHOLD = "MEDHOLD_I_KLAGE";
    private static final String VURDERING_OPPHEVE = "OPPHEVE_YTELSESVEDTAK";
    
    protected String klageVurdering;
    protected String klageMedholdArsak;
    protected String klageAvvistArsak;
    protected LocalDate vedtaksdatoPaklagdBehandling;
    
    public VurderingAvKlageBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
        vedtaksdatoPaklagdBehandling = LocalDate.now();
    }

    public void bekreftMedhold(String årsak) {
        klageVurdering  = VURDERING_MEDHOLD;
        klageMedholdArsak = årsak;
    }
    
    public void bekreftAvvist(String årsak) {
        klageVurdering  = VURDERING_AVVIS;
        klageMedholdArsak = årsak;
    }
    
    public void bekreftStadfestet() {
        klageVurdering  = VURDERING_STADFEST;
    }
    
    public void bekreftOpphevet() {
        klageVurdering  = VURDERING_OPPHEVE;
    }
    
    public void setVedtaksdatoPaklagdBehandling(LocalDate dato) {
        vedtaksdatoPaklagdBehandling = dato;
    }
}
