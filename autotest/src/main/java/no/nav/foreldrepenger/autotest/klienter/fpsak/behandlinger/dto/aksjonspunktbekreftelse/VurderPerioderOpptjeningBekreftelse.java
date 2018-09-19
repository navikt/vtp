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
        if(behandling.opptjening.opptjeningAktivitetList == null) {
            return;
        }
        
        for (OpptjeningAktivitet opptjeningAktivitet : behandling.opptjening.opptjeningAktivitetList) {
            
            opptjeningAktivitet.originalFom = opptjeningAktivitet.opptjeningFom;
            opptjeningAktivitet.originalTom = opptjeningAktivitet.opptjeningTom;
            
            opptjeningAktivitetList.add(opptjeningAktivitet);
        }
    }
    
    public void godkjennOpptjening(OpptjeningAktivitet aktivitet) {
        aktivitet.erGodkjent = true;
        aktivitet.begrunnelse = "Godkjent";
        aktivitet.erManueltOpprettet = false;
    }
    
    public void avvisOpptjening(OpptjeningAktivitet aktivitet) {
        aktivitet.erGodkjent = false;
        aktivitet.begrunnelse = "Avvist";
        aktivitet.erManueltOpprettet = false;
    }
    
    public void leggTilOpptjening(OpptjeningAktivitet aktivitet) {
        aktivitet.erManueltOpprettet = true;
        opptjeningAktivitetList.add(aktivitet);
    }
    
    
    
}
