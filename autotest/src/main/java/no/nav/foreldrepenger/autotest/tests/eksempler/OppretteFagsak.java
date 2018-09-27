package no.nav.foreldrepenger.autotest.tests.eksempler;

import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.tests.FpsakTestBase;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioImpl;
import no.nav.vedtak.felles.xml.soeknad.v1.Soeknad;

@Tag("eksempel")
public class OppretteFagsak extends FpsakTestBase{

    public void oppretteTerminsøknad() throws Exception {
        TestscenarioImpl testscenario = opprettScenario("50");
        Soeknad søknad = foreldrepengeSøknadErketyper.termindatoUttakKunMor(testscenario.getPersonopplysninger().getSøker().getAktørIdent());
        
        fordel.erLoggetInnMedRolle("Saksbehandler");
        long saksnummer = fordel.sendInnSøknad(søknad, testscenario);
    }
}
