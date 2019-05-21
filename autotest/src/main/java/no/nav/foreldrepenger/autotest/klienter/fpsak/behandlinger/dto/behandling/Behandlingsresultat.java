package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Behandlingsresultat {

    protected Integer id;
    protected Kode type;
    protected Kode avslagsarsak;
    protected Kode rettenTil;
    protected List<Kode> konsekvenserForYtelsen;
    protected String avslagsarsakFritekst;
    protected String overskrift;
    protected String fritekstbrev;
    protected String skjaeringstidspunktForeldrepenger;

    @Override
    public String toString() {
        return type.kode;
    }

    public List<Kode> getKonsekvenserForYtelsen() {return konsekvenserForYtelsen;}

    public Kode getAvslagsarsak() { return avslagsarsak;}

    public String getSkjaeringstidspunktForeldrepenger() {return skjaeringstidspunktForeldrepenger;}

}
