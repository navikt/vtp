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
	    erLoggetInnMedRolle("Saksbehandler");
	}
	
	public void erLoggetInnMedRolle(String rolle) throws IOException {
	    OpenamKlient klient = new OpenamKlient(session);
	    klient.logInnMedRolle(rolle);
	}
}
