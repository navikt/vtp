package no.nav.foreldrepenger.autotest.tests;

import org.junit.jupiter.api.BeforeEach;

import no.nav.foreldrepenger.autotest.aktoerer.fordel.Fordel;
import no.nav.foreldrepenger.autotest.aktoerer.saksbehandler.Saksbehandler;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengesoknadXmlErketyper;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.TestscenarioRepositoryImpl;

public class FpsakTestBase extends TestBase{

    /*
     * Aktører
     */
    protected Fordel fordel;
    protected Saksbehandler saksbehandler;
    
    /*
     * VTP
     */
    protected TestscenarioRepositoryImpl testscenarioRepository;
    protected ForeldrepengesoknadXmlErketyper foreldrepengeSøknadErketyper;
    
    @BeforeEach
    void setUp() throws Exception{
        fordel = new Fordel();
        saksbehandler = new Saksbehandler();
        
        testscenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
        foreldrepengeSøknadErketyper = new ForeldrepengesoknadXmlErketyper();
    }
    
}
