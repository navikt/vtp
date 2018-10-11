package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Behandlingsresultat {

    protected Integer id;
    protected Kode type;
    protected Kode avslagsarsak;
    protected Kode rettenTil;
    protected Kode konsekvensForYtelsen;
    protected String avslagsarsakFritekst;
    protected String overskrift;
    protected String fritekstbrev;
    protected String skjaeringstidspunktForeldrepenger;
    
    @Override
    public String toString() {
        return type.kode;
    }
    
}
