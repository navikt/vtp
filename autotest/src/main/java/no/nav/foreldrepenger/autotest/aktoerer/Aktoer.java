package no.nav.foreldrepenger.autotest.aktoerer;

import java.io.UnsupportedEncodingException;

import no.nav.foreldrepenger.autotest.klienter.openam.OpenAMKlient;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.HttpsSession;
import no.nav.foreldrepenger.autotest.util.konfigurasjon.TestKonfigurasjon;
import no.nav.foreldrepenger.autotest.util.konfigurasjon.TestKonfigurasjon.Bruker;

public class Aktoer {

	public HttpSession session;
	
	public Aktoer() {
		session = new HttpsSession();
	}
	
	public void erLoggetInnUtenRolle() throws UnsupportedEncodingException {
	    OpenAMKlient openAMHelper = new OpenAMKlient(session, "https://localhost:8063/isso/oauth2");
	    openAMHelper.loginMock(TestKonfigurasjon.hentOIDCMockIssuer());
	}
	
	public void erLoggetInnMedRolle(String rolle) {
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
	}
}
