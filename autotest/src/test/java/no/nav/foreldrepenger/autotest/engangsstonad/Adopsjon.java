package no.nav.foreldrepenger.autotest.engangsstonad;

import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.vedtak.felles.xml.soeknad.v1.Soeknad;


public class Adopsjon extends EngangsstonadTestBase{

    
    public void behandleAdopsjonEngangstønadGodkjent() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");
        Soeknad søknad = foreldrepengeSøknadErketyper.adopsjonMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle("Saksbehandler");
        long saksnummer = fordel.sendInnSøknad(søknad, testscenario, DokumenttypeId.FOEDSELSSOKNAD_ENGANGSSTONAD);
        System.out.println("Saksnummer: " + saksnummer);
        
        saksbehandler.erLoggetInnMedRolle("Saksbehandler");
        saksbehandler.hentFagsak(saksnummer);
    }
}
