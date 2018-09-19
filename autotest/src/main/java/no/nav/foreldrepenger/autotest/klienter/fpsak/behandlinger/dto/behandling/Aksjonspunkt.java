package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import com.fasterxml.jackson.annotation.JsonIgnore;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.AksjonspunktBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

public class Aksjonspunkt {
    
    @JsonIgnore
    public transient AksjonspunktBekreftelse bekreftelse;
    
    public Kode definisjon;
    public Kode status;
    public String begrunnelse;
    public Kode vilkarType;
    public Kode kategori;
    public Boolean toTrinnsBehandling;
    public Boolean toTrinnsBehandlingGodkjent;
    
    public boolean isUnconfirmed(){
        return !status.kode.equals("UTFO");
    }
}
