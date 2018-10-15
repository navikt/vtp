package no.nav.foreldrepenger.autotest.aktoerer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.impl.cookie.BasicClientCookie;

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
            klient.bypass(rolle);
	    /*
		Bruker user = TestKonfigurasjon.hentBruker(rolle);
		
		try {
			OpenAMKlient openAMHelper = new OpenAMKlient(session, TestKonfigurasjon.hentOIDCUrl());
			openAMHelper.loginSession(user.brukernavn, 
					user.passord,
                    TestKonfigurasjon.hentOIDCSystemBruker(),
                    TestKonfigurasjon.hentOIDCSystemPassord());
		}
		catch (Exception e) {
			throw new RuntimeException("Login Failed for role: " + rolle + " - " + e.getMessage());
		}
	     */
	}
}
