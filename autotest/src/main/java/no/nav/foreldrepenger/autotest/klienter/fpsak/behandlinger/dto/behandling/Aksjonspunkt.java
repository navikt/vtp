package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

public class Aksjonspunkt {
    public Kode definisjon;
    public Kode status;
    public String begrunnelse;
    public Kode vilkarType;
    public Kode kategori;
    public Boolean toTrinnsBehandling;
    public Boolean toTrinnsBehandlingGodkjent;
    
    //public transient AksjonspunktBekreftelse bekreftelse;
    
    public boolean isUnconfirmed(){
        return !status.kode.equals("UTFO");
    }
}
