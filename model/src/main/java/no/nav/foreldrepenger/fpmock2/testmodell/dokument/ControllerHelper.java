package no.nav.foreldrepenger.fpmock2.testmodell.dokument;

import static no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId.ADOPSJONSSOKNAD_ENGANGSSTONAD;
import static no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId.ADOPSJONSSOKNAD_FORELDREPENGER;
import static no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD;
import static no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER;
import static no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId.FORELDREPENGER_ENDRING_SØKNAD;
import static no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId.SØKNAD_SVANGERSKAPSPENGER;

import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.Behandlingstema;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

public class ControllerHelper {

    public static Behandlingstema translateSøknadDokumenttypeToBehandlingstema(DokumenttypeId dokumenttypeId) throws Exception {

        //TODO: refaktorer til switch.

        if (dokumenttypeId == FOEDSELSSOKNAD_FORELDREPENGER) {
            return Behandlingstema.FORELDREPENGER_FOEDSEL;
        } else if (dokumenttypeId == ADOPSJONSSOKNAD_FORELDREPENGER) {
            return Behandlingstema.FORELDREPENGER_ADOPSJON;
        } else if (dokumenttypeId ==FOEDSELSSOKNAD_ENGANGSSTONAD) {
            return Behandlingstema.ENGANGSSTONAD_FOEDSEL;
        } else if (dokumenttypeId == ADOPSJONSSOKNAD_ENGANGSSTONAD) {
            return Behandlingstema.ENGANGSSTONAD_ADOPSJON;
        } else if (dokumenttypeId == FORELDREPENGER_ENDRING_SØKNAD) {
            return Behandlingstema.FORELDREPENGER;
        } else if (dokumenttypeId == SØKNAD_SVANGERSKAPSPENGER){
            return Behandlingstema.SVANGERSKAPSPENGER;
        } else {
            throw new Exception("Kunne ikke matche på dokumenttype.");
        }
    }
}
