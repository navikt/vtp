package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Vilkar {
    protected Kode vilkarType;
    protected Kode vilkarStatus;

    public Kode getVilkarType() {
        return vilkarType;
    }

    public Kode getVilkarStatus() {
        return vilkarStatus;
    }
}
