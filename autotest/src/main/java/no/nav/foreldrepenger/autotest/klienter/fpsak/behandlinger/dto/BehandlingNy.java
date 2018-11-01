package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BehandlingNy {
    protected Long saksnummer;
    protected String behandlingType;
    protected String behandlingArsakType = null;
    protected Boolean nyBehandlingEtterKlage = null;
    
    public BehandlingNy(Long saksnummer, String behandlingType, String behandlingArsakType) {
        super();
        this.saksnummer = saksnummer;
        this.behandlingType = behandlingType;
        this.behandlingArsakType = behandlingArsakType;
    }
}
