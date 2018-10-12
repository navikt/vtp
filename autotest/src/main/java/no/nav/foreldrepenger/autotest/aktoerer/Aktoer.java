package no.nav.foreldrepenger.autotest.aktoerer;

import java.io.UnsupportedEncodingException;

import org.apache.http.impl.cookie.BasicClientCookie;

import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.HttpsSession;

public class Aktoer {

	public HttpSession session;
	
	public Aktoer() {
		session = new HttpsSession();
	}
	
	public void erLoggetInnUtenRolle() {
	    String token = "token-som-ikke-betyr-noe";
	    String domain = "localhost";
	    String path  = "/";
	    
            
            BasicClientCookie cookie = new BasicClientCookie("ID_token", token);
            cookie.setDomain(domain);
            cookie.setPath(path);
            session.leggTilCookie(cookie);
	}
	
	public void erLoggetInnMedRolle(String rolle) {
	    erLoggetInnUtenRolle();
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
