package no.nav.foreldrepenger.vtp.testmodell.personopplysning;

/** NAV kodeverk: http://nav.no/kodeverk/Kodeverk/Diskresjonskoder. */
public enum Diskresjonskoder {
    KLIE, MILI, PEND, SPFO(7), SPSF(6), SVAL, UFB, URIK, UDEF;
    int kode;

    Diskresjonskoder() {
    }

    Diskresjonskoder(int kode) {
        this.kode = kode;
    }

}
