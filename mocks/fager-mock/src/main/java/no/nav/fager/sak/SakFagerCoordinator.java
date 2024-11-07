package no.nav.fager.sak;

import no.nav.fager.HardDeleteSakVellykket;
import no.nav.fager.NySakResultat;
import no.nav.fager.NyStatusSakVellykket;
import no.nav.fager.TilleggsinformasjonSakVellykket;
import no.nav.foreldrepenger.vtp.testmodell.arbeidsgiver.SakModell;

public interface SakFagerCoordinator {

    NySakResultat opprettSak(String grupperingsid,
                             String merkelapp,
                             String virksomhetsnummer,
                             String tittel,
                             String lenke,
                             SakStatus initiellStatus,
                             String overstyrStatustekstMed);

    NyStatusSakVellykket nyStatusSak(String id,
                                     SakModell.SakStatus status,
                                     String overstyrStatustekstMed);

    TilleggsinformasjonSakVellykket tilleggsinformasjonSak(String id, String tilleggsinformasjon);

    HardDeleteSakVellykket hardDeleteSak(String id);

    enum SakStatus {
        MOTTATT,
        UNDER_BEHANDLING,
        FERDIG
    }
}
