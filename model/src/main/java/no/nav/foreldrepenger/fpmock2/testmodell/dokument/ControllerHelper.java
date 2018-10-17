package no.nav.foreldrepenger.fpmock2.testmodell.dokument;

import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.Behandlingstema;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

import static no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId.*;

public class ControllerHelper {

    public static Behandlingstema translateSøknadDokumenttypeToBehandlingstema(DokumenttypeId dokumenttypeId) throws Exception {

        if (dokumenttypeId == FOEDSELSSOKNAD_FORELDREPENGER) {
            return Behandlingstema.FORELDREPENGER_FOEDSEL;
        } else if (dokumenttypeId == ADOPSJONSSOKNAD_FORELDREPENGER) {
            return Behandlingstema.FORELDREPENGER_ADOPSJON;
        } else if (dokumenttypeId ==FOEDSELSSOKNAD_ENGANGSSTONAD) {
            return Behandlingstema.ENGANGSSTONAD_FOEDSEL;
        } else if (dokumenttypeId == ADOPSJONSSOKNAD_ENGANGSSTONAD) {
            return Behandlingstema.ENGANGSSTONAD_ADOPSJON;
        } else {
            throw new Exception("Kunne ikke matche på dokumenttype.");
        }
    }
}
