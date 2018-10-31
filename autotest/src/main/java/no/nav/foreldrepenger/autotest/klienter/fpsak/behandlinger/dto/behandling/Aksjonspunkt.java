package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.AksjonspunktBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Aksjonspunkt {
    
    @JsonIgnore
    private transient AksjonspunktBekreftelse bekreftelse;
    
    protected Kode definisjon;
    protected Kode status;
    protected String begrunnelse;
    protected Kode vilkarType;
    protected Kode kategori;
    protected Boolean toTrinnsBehandling;
    protected Boolean toTrinnsBehandlingGodkjent;
    
    public Kode getDefinisjon() {
        return definisjon;
    }
    
    public boolean erUbekreftet(){
        return !status.kode.equals("UTFO");
    }
    
    public boolean skalTilToTrinnsBehandling() {
        return toTrinnsBehandling;
    }

    public AksjonspunktBekreftelse getBekreftelse() {
        return bekreftelse;
    }

    public Kode getStatus() { return status;}

    public void setBekreftelse(AksjonspunktBekreftelse bekreftelse) {
        this.bekreftelse = bekreftelse;
    }
}
