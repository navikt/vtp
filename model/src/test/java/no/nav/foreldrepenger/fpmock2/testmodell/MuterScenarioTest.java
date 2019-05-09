package no.nav.foreldrepenger.fpmock2.testmodell;

import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioTemplate;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.TestscenarioTemplateRepositoryImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

public class MuterScenarioTest {

    @Test
    public void slettScenarioTest() throws Exception{
        TestscenarioTemplateRepositoryImpl templateRepository = TestscenarioTemplateRepositoryImpl.getInstance();
        templateRepository.load();
        Collection<TestscenarioTemplate> scenarioTemplates = templateRepository.getTemplates();
        TestscenarioRepositoryImpl testScenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());

        TestscenarioImpl testScenario = testScenarioRepository.opprettTestscenario(scenarioTemplates.stream().findFirst().get());

        Assert.assertTrue(testScenarioRepository.getTestscenarios().size() > 0);
        testScenarioRepository.slettScenario(testScenario.getId());

        Assert.assertTrue(testScenarioRepository.getTestscenarios().stream().filter(ts -> (ts.getId() == testScenario.getId())).count() == 0);
    }
}
