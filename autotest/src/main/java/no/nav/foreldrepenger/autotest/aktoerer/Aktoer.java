package no.nav.foreldrepenger.autotest.aktoerer;

import java.io.IOException;

import no.nav.foreldrepenger.autotest.klienter.vtp.openam.OpenamKlient;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.HttpsSession;

public class Aktoer {

    public HttpSession session;

    public Aktoer() {
        session = new HttpsSession();
    }

    public void erLoggetInnUtenRolle() throws IOException {
        erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
    }

    public void erLoggetInnMedRolle(Rolle rolle) throws IOException {
        OpenamKlient klient = new OpenamKlient(session);
        klient.logInnMedRolle(rolle.getKode());
    }

    public enum Rolle {
        SAKSBEHANDLER("saksbeh"),
        SAKSBEHANDLER_KODE_6("saksbeh6"),
        SAKSBEHANDLER_KODE_7("saksbeh7"),
        BESLUTTER("beslut"),
        OVERSTYRER("oversty"),
        KLAGEBEHANDLER("klageb"),
        VEILEDER("veil");

        String kode;

        private Rolle(String kode) {
            this.kode = kode;
        }

        public String getKode() {
            return kode;
        }
    }
}
