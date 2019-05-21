package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta;

import java.time.LocalDate;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.AksjonspunktBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.BekreftelseKode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5030")
public class AvklarFaktaVergeBekreftelse extends AksjonspunktBekreftelse{

    protected String fnr;
    protected LocalDate gyldigFom;
    protected LocalDate gyldigTom;
    protected String mandatTekst;
    protected String navn;
    protected Boolean sokerErKontaktPerson;
    protected Boolean sokerErUnderTvungenForvaltning;
    protected Boolean vergeErKontaktPerson;
    protected String vergeType;
    
    public AvklarFaktaVergeBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }
    
    public AvklarFaktaVergeBekreftelse setVerge(String fnr) {
        this.fnr = fnr;
        
        //Defaults
        gyldigFom = LocalDate.now().minusYears(1);
        gyldigTom = LocalDate.now().plusYears(2);
        mandatTekst = "Hva er dette";
        navn = "Verge vergesen";
        vergeType = "BARN";
        return this;
    }
    
    public AvklarFaktaVergeBekreftelse setVergePeriodeStart(LocalDate fom) {
        gyldigFom = fom;
        return this;
    }
    
    public AvklarFaktaVergeBekreftelse setVergePeriodeSlutt(LocalDate tom) {
        gyldigTom = tom;
        return this;
    }
    
    public AvklarFaktaVergeBekreftelse bekreftSøkerErKontaktperson() {
        sokerErKontaktPerson = true;
        return this;
    }
    
    public AvklarFaktaVergeBekreftelse bekreftSøkerErIkkeKontaktperson() {
        sokerErKontaktPerson = false;
        return this;
    }
    
    public AvklarFaktaVergeBekreftelse bekreftSøkerErUnderTvungenForvaltning() {
        sokerErUnderTvungenForvaltning = true;
        return this;
    }
    
    public AvklarFaktaVergeBekreftelse bekreftSøkerErIkkeUnderTvungenForvaltning() {
        sokerErUnderTvungenForvaltning = false;
        return this;
    }
    
    public AvklarFaktaVergeBekreftelse bekreftVergeErKontaktPerson() {
        vergeErKontaktPerson = true;
        return this;
    }
    
    public AvklarFaktaVergeBekreftelse bekreftVergeErIkkeKontaktPerson() {
        vergeErKontaktPerson = false;
        return this;
    }
    
    
}
