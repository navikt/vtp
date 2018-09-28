package no.nav.foreldrepenger.autotest.tests;

import org.junit.jupiter.api.BeforeEach;

import no.nav.foreldrepenger.autotest.aktoerer.fordel.Fordel;
import no.nav.foreldrepenger.autotest.aktoerer.saksbehandler.Saksbehandler;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengesoknadXmlErketyper;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.TestscenarioTemplateRepositoryImpl;

public class FpsakTestBase extends TestBase{

    /*
     * Aktører
     */
    protected Fordel fordel;
    protected Saksbehandler saksbehandler;
    protected Saksbehandler overstyrer;
    protected Saksbehandler beslutter;
    
    /*
     * VTP
     */
    protected TestscenarioRepositoryImpl testscenarioRepository;
    protected TestscenarioTemplateRepositoryImpl testscenarioTemplates;
    protected ForeldrepengesoknadXmlErketyper foreldrepengeSøknadErketyper;
    
    @BeforeEach
    void setUp() throws Exception{
        fordel = new Fordel();
        saksbehandler = new Saksbehandler();
        overstyrer = new Saksbehandler();
        beslutter = new Saksbehandler();
        
        testscenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
        testscenarioTemplates = TestscenarioTemplateRepositoryImpl.getInstance();
        foreldrepengeSøknadErketyper = new ForeldrepengesoknadXmlErketyper();
    }
    
    protected TestscenarioImpl opprettScenario(String id) {
        return testscenarioRepository.opprettTestscenario(testscenarioTemplates.finn("50"));
    }
    
}
