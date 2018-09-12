package no.nav.foreldrepenger.autotest.aktoerer;

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
	
	public void erLoggetInnMedRolle(String rolle) {
		Bruker user = TestKonfigurasjon.hentBruker(rolle);
		
		try {
			OpenAMKlient openAMHelper = new OpenAMKlient(session, TestKonfigurasjon.hentOICDUrl());
			openAMHelper.loginSession(user.brukernavn, 
					user.passord,
                    TestKonfigurasjon.hentOICDSystemBruker(),
                    TestKonfigurasjon.hentOICDSystemPassord());
		}
		catch (Exception e) {
			throw new RuntimeException("Login Failed for role: " + rolle + " - " + e.getMessage());
		}
	}
}
