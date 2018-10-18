package no.nav.foreldrepenger.autotest.engangsstonad;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.vedtak.felles.xml.soeknad.v1.Soeknad;


public class Adopsjon extends EngangsstonadTestBase{

    @Tag("utvikling")
    @Test
    public void behandleAdopsjonMorGodkjent() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.adopsjonMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());
        
        fordel.erLoggetInnMedRolle("Saksbehandler");
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.ADOPSJONSSOKNAD_ENGANGSSTONAD);
        
        saksbehandler.erLoggetInnMedRolle("Saksbehandler");
        saksbehandler.hentFagsak(saksnummer);
    }
    
    public void behandleAdopsjonMorAvvist() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.adopsjonMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());
        
        fordel.erLoggetInnMedRolle("Saksbehandler");
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.ADOPSJONSSOKNAD_ENGANGSSTONAD);
        
        saksbehandler.erLoggetInnMedRolle("Saksbehandler");
        saksbehandler.hentFagsak(saksnummer);
    }
    
    public void behandleAdopsjonMorOverstyrt() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.adopsjonMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());
        
        fordel.erLoggetInnMedRolle("Saksbehandler");
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.ADOPSJONSSOKNAD_ENGANGSSTONAD);
        
        saksbehandler.erLoggetInnMedRolle("Saksbehandler");
        saksbehandler.hentFagsak(saksnummer);
    }
    
    public void behandleAdopsjonFarGodkjent() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.adopsjonMorEngangstonad(testscenario.getPersonopplysninger().getSøkerAktørIdent());
        
        fordel.erLoggetInnMedRolle("Saksbehandler");
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.ADOPSJONSSOKNAD_ENGANGSSTONAD);
        
        saksbehandler.erLoggetInnMedRolle("Saksbehandler");
        saksbehandler.hentFagsak(saksnummer);
    }
}
