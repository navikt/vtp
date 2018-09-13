package no.nav.foreldrepenger.autotest;

import java.util.List;

import no.nav.foreldrepenger.fpmock2.felles.PropertiesUtils;
import org.junit.jupiter.api.BeforeAll;

import no.nav.foreldrepenger.autotest.util.konfigurasjon.MiljoKonfigurasjon;
import no.nav.foreldrepenger.autotest.util.konfigurasjon.TestKonfigurasjon;

public abstract class TestBase {
    
    @BeforeAll
    protected static void setUpAll() {
        PropertiesUtils.initProperties(hentPropertyDir());
        new MiljoKonfigurasjon();
    }
	protected void verifiserListeInneholder(List<Object> liste, Object object1) {
		for (Object object2 : liste) {
			if(object1.equals(object2)) {
				return;
			}
		}
		verifiser(false, "Listen: " + liste.toString() + " inneholdt ikke: " + object1.toString());
	}
	
	protected void verifiserLikhet(Object verdi1, Object verdi2) {
		verifiser(verdi1.equals(verdi2), verdi1 + " != " + verdi2);
	}
	
	protected void verifiser(boolean statement) {
		verifiser(statement, "ingen melding");
	}
	
	protected void verifiser(boolean statement, String message) {
		if(!statement) {
			throw new RuntimeException("Verifisering feilet: " + message);
		}
	}
	
	private static String hentPropertyDir() {
	    String propertyDir = System.getProperty("application.root");
	    if(null == propertyDir) {
	        throw new RuntimeException("System property 'application.root' er ikke satt");
	    }
	    return propertyDir;
	}
}
