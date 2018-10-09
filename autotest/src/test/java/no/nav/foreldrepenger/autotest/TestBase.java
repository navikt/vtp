package no.nav.foreldrepenger.autotest;

import java.util.List;
import org.junit.jupiter.api.BeforeAll;

import no.nav.foreldrepenger.autotest.util.konfigurasjon.MiljoKonfigurasjon;

public abstract class TestBase {
    
    @BeforeAll
    protected static void setUpAll() {
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
	
	protected void verifiserLikhet(Object verdiGjeldende, Object verdiForventet) {
		verifiserLikhet(verdiGjeldende, verdiForventet, "Object");
	}
	
	protected void verifiserLikhet(Object verdiGjeldende, Object verdiForventet, String verdiNavn) {
	    verifiser(verdiGjeldende.equals(verdiNavn), String.format("%s har uventet verdi. forventet %s, var %s", verdiNavn, verdiForventet, verdiGjeldende));
	}
	
	protected void verifiser(boolean statement) {
		verifiser(statement, "ingen melding");
	}
	
	protected void verifiser(boolean statement, String message) {
		if(!statement) {
			throw new RuntimeException("Verifisering feilet: " + message);
		}
	}
}
