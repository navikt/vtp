package no.nav.foreldrepenger.autotest.tests.eksempler;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.tests.FpsakTestBase;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioImpl;
import no.nav.vedtak.felles.xml.soeknad.v1.Soeknad;

@Tag("eksempel")
public class OppretteFagsak extends FpsakTestBase{

    
    public void oppretteTerminsøknad() throws Exception {
        //Opprett scenario og søknad
        TestscenarioImpl testscenario = opprettScenario("50");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.termindatoUttakKunMor(testscenario.getPersonopplysninger().getSøker().getAktørIdent());
        
        //Send inn søknad
        fordel.erLoggetInnMedRolle("Saksbehandler");
        long saksnummer = fordel.sendInnSøknad(søknad, testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        
        //Behandle sak
        saksbehandler.erLoggetInnMedRolle("Saksbehandler");
        saksbehandler.hentFagsak(saksnummer);
        
        verifiserLikhet(saksbehandler.valgtFagsak.hentStatus().kode, "Test", "Status");
    }
}
