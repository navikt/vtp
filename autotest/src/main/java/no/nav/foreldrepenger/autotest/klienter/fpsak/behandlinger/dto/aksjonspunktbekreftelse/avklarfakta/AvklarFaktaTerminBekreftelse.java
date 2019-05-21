package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta;

import java.time.LocalDate;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.AksjonspunktBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.BekreftelseKode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;

@BekreftelseKode(kode="5001")
public class AvklarFaktaTerminBekreftelse extends AksjonspunktBekreftelse{
    
    protected int antallBarn;
    protected LocalDate utstedtdato;
    protected LocalDate termindato;
    
    public AvklarFaktaTerminBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }

    public void setAntallBarn(int antallBarn) {
        this.antallBarn = antallBarn;
    }
    public AvklarFaktaTerminBekreftelse antallBarn(int antallBarn) {
        setAntallBarn(antallBarn);
        return this;
    }

    public void setUtstedtdato(LocalDate utstedtdato) {
        this.utstedtdato = utstedtdato;
    }
    
    public AvklarFaktaTerminBekreftelse utstedtdato(LocalDate utstedtdato) {
        setUtstedtdato(utstedtdato);
        return this;
    }

    public void setTermindato(LocalDate termindato) {
        this.termindato = termindato;
    }
    
    public AvklarFaktaTerminBekreftelse termindato(LocalDate termindato) {
        setTermindato(termindato);
        return this;
    }
    
    
}
