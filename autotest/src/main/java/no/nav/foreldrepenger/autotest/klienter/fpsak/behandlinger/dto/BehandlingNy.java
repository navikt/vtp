package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BehandlingNy {
    protected Long saksnummer;
    protected Kode behandlingType;
    protected Kode behandlingArsakType = null;
    protected Boolean nyBehandlingEtterKlage = null;
    
    public BehandlingNy(Long saksnummer, Kode behandlingType) {
        super();
        this.saksnummer = saksnummer;
        this.behandlingType = behandlingType;
    }
}
