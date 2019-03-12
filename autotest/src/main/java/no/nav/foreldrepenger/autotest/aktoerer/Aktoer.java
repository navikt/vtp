package no.nav.foreldrepenger.autotest.aktoerer;

import java.io.IOException;

import io.qameta.allure.Step;
import no.nav.foreldrepenger.autotest.klienter.vtp.openam.OpenamKlient;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.SecureHttpsSession;

public class Aktoer {

    public HttpSession session;

    public Aktoer() {
        session = SecureHttpsSession.session();
    }

    public void erLoggetInnUtenRolle() throws IOException {
        erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
    }

    @Step("Logger inn med rolle: {rolle}")
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
