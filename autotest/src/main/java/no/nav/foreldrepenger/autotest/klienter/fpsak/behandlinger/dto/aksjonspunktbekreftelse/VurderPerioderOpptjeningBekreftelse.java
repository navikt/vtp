package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.opptjening.OpptjeningAktivitet;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5051")
public class VurderPerioderOpptjeningBekreftelse extends AksjonspunktBekreftelse {

    protected List<OpptjeningAktivitet> opptjeningAktivitetList = new ArrayList<>();
    
    public VurderPerioderOpptjeningBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
        if(behandling.opptjening.getOpptjeningAktivitetList() == null) {
            return;
        }
        
        for (OpptjeningAktivitet opptjeningAktivitet : behandling.opptjening.getOpptjeningAktivitetList()) {
            
            opptjeningAktivitet.setOriginalFom(opptjeningAktivitet.getOpptjeningFom());
            opptjeningAktivitet.setOriginalTom(opptjeningAktivitet.getOpptjeningTom());
            
            opptjeningAktivitetList.add(opptjeningAktivitet);
        }
    }
    
    public void godkjennOpptjening(OpptjeningAktivitet aktivitet) {
        aktivitet.vurder(true, "Godkjent", false);
    }
    
    public void avvisOpptjening(OpptjeningAktivitet aktivitet) {
        aktivitet.vurder(false, "Avvist", false);
    }
    
    public void leggTilOpptjening(OpptjeningAktivitet aktivitet) {
        aktivitet.setErManueltOpprettet(true);
        opptjeningAktivitetList.add(aktivitet);
    }
    
    
    
}
