package no.nav.foreldrepenger.autotest;

import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.qameta.allure.Step;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.util.konfigurasjon.MiljoKonfigurasjon;
import no.nav.foreldrepenger.fpmock2.felles.PropertiesUtils;

public abstract class TestBase {
    
    //Logger for testruns
    protected Logger log;
    
    public TestBase() {
        log = LoggerFactory.getLogger(this.getClass());
    }
    
    /*
     * Global setup
     */
    @BeforeAll
    protected static void setUpAll() {
        String propertiesDir = System.getProperty("application.root");
        PropertiesUtils.initProperties(propertiesDir == null ? ".." : propertiesDir);
        MiljoKonfigurasjon.initProperties();
    }
    
    /*
     * Verifisering
     */
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

    @Step("Verifiserer likhet på {verdiNavn} mellom gjeldene {verdiGjeldende} og forventet {verdiForventet}")
    protected void verifiserLikhet(Object verdiGjeldende, Object verdiForventet, String verdiNavn) {
        verifiser(verdiGjeldende.equals(verdiForventet), String.format("%s har uventet verdi. forventet %s, var %s", verdiNavn, verdiForventet, verdiGjeldende));
    }

    protected void verifiser(boolean statement) {
        verifiser(statement, "ingen melding");
    }
    
    protected void verifiser(boolean statement, String message) {
        if(!statement) {
            throw new RuntimeException("Verifisering feilet: " + message);
        }
    }

    @Step("Henter aksjonspunkter: {aksjonspunkter}")
    public void debugListUtAksjonspunkter(String aksjonspunkter){ }

    @Step("Informasjon om behandling: {behandling}")
    public void debugListUtBehandling(Behandling behandling){ }

}
