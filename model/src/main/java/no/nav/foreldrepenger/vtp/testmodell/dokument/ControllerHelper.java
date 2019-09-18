package no.nav.foreldrepenger.vtp.testmodell.dokument;

import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Behandlingstema;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.DokumenttypeId;

public class ControllerHelper {

    public static Behandlingstema translateSøknadDokumenttypeToBehandlingstema(DokumenttypeId dokumenttypeId) throws Exception {

        //TODO: refaktorer til switch.

        if (dokumenttypeId == DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER) {
            return Behandlingstema.FORELDREPENGER_FOEDSEL;
        } else if (dokumenttypeId == DokumenttypeId.ADOPSJONSSOKNAD_FORELDREPENGER) {
            return Behandlingstema.FORELDREPENGER_ADOPSJON;
        } else if (dokumenttypeId == DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD) {
            return Behandlingstema.ENGANGSSTONAD_FOEDSEL;
        } else if (dokumenttypeId == DokumenttypeId.ADOPSJONSSOKNAD_ENGANGSSTONAD) {
            return Behandlingstema.ENGANGSSTONAD_ADOPSJON;
        } else if (dokumenttypeId == DokumenttypeId.FORELDREPENGER_ENDRING_SØKNAD) {
            return Behandlingstema.FORELDREPENGER;
        } else if (dokumenttypeId == DokumenttypeId.SØKNAD_SVANGERSKAPSPENGER){
            return Behandlingstema.SVANGERSKAPSPENGER;
        } else {
            throw new Exception("Kunne ikke matche på dokumenttype.");
        }
    }
}
