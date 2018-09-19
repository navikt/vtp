package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta;

import java.time.LocalDate;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.AksjonspunktBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.BekreftelseKode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5030")
public class AvklarVergeBekreftelse extends AksjonspunktBekreftelse{

    protected String fnr;
    protected LocalDate gyldigFom;
    protected LocalDate gyldigTom;
    protected String mandatTekst;
    protected String navn;
    protected Boolean sokerErKontaktPerson;
    protected Boolean sokerErUnderTvungenForvaltning;
    protected Boolean vergeErKontaktPerson;
    protected String vergeType;
    
    public AvklarVergeBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }
    
    public void setVerge(String fnr) {
        this.fnr = fnr;
        
        //Defaults
        gyldigFom = LocalDate.now().minusYears(1);
        gyldigTom = LocalDate.now().plusYears(2);
        mandatTekst = "Hva er dette";
        navn = "Verge vergesen";
        vergeType = "BARN";
    }
    
    public void setVergePeriodeStart(LocalDate fom) {
        gyldigFom = fom;
    }
    
    public void setVergePeriodeSlutt(LocalDate tom) {
        gyldigTom = tom;
    }
    
    public void bekreftSøkerErKontaktperson() {
        sokerErKontaktPerson = true;
    }
    
    public void bekreftSøkerErIkkeKontaktperson() {
        sokerErKontaktPerson = false;
    }
    
    public void bekreftSøkerErUnderTvungenForvaltning() {
        sokerErUnderTvungenForvaltning = true;
    }
    
    public void bekreftSøkerErIkkeUnderTvungenForvaltning() {
        sokerErUnderTvungenForvaltning = false;
    }
    
    public void bekreftVergeErKontaktPerson() {
        vergeErKontaktPerson = true;
    }
    
    public void bekreftVergeErIkkeKontaktPerson() {
        vergeErKontaktPerson = false;
    }
    
    
}
