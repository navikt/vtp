package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.Fagsak;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@BekreftelseKode(kode="5037")
public class VurderingAvInnsynBekreftelse extends AksjonspunktBekreftelse {

    public LocalDate mottattDato;
    public LocalDate fristDato;
    public List<Object> innsynDokumenter = new ArrayList<>();
    public String innsynResultatType;
    public Boolean sattPaVent; 
    
    public VurderingAvInnsynBekreftelse(Fagsak fagsak, Behandling behandling) {
        super(fagsak, behandling);
    }
    
    public VurderingAvInnsynBekreftelse setMottattDato(LocalDate mottattDato) {
        this.mottattDato = mottattDato;
        this.fristDato = mottattDato.plusDays(4);
        return this;
    }
    
    public VurderingAvInnsynBekreftelse setInnsynResultatType(Kode innsynResultatType) {
        this.innsynResultatType = innsynResultatType.kode;
        return this;
    }
    
    public VurderingAvInnsynBekreftelse skalSetteSakPåVent(boolean settPåVent) {
        this.sattPaVent = settPåVent;
        return this;
    }

}
