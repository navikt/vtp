package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

public class Behandlingsresultat {

    protected Integer id;
    protected Kode type;
    protected Kode avslagsarsak;
    protected Kode rettenTil;
    protected Kode konsekvensForYtelsen;
    protected String overskrift;
    protected String fritekstbrev;
    protected String skjaeringstidspunktForeldrepenger;
}
