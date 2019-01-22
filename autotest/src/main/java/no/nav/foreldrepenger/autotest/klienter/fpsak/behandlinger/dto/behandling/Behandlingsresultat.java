package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

import java.util.List;

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
    
}
