package no.nav.foreldrepenger.vtp.testmodell.repo.impl;

import java.util.Map;
import java.util.UUID;

import no.nav.foreldrepenger.vtp.testmodell.repo.BasisdataProvider;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;

/** Indeks av alle testdata instanser. */
public class TestscenarioRepositoryImpl extends TestscenarioBuilderRepositoryImpl implements TestscenarioRepository {

    private static TestscenarioRepositoryImpl instance;

    public static synchronized TestscenarioRepositoryImpl getInstance(BasisdataProvider basisdata) {
        if(instance == null){
            instance = new TestscenarioRepositoryImpl(basisdata);
        }
        return instance;
    }

    private TestscenarioRepositoryImpl(BasisdataProvider basisdata) {
        super(basisdata);
    }

    @Override
    public TestscenarioImpl opprettTestscenario(String testscenarioJson, Map<String, String> variables) {
        String unikTestscenarioId = UUID.randomUUID().toString();
        TestscenarioFraJsonMapper mapper = new TestscenarioFraJsonMapper(this);
        return mapper.lagTestscenarioFraJsonString(testscenarioJson, unikTestscenarioId, variables);
    }

    @Override
    public TestscenarioImpl oppdaterTestscenario(String id, String testscenarioJson, Map<String, String> variables) {
        TestscenarioFraJsonMapper mapper = new TestscenarioFraJsonMapper(this);
        return mapper.lagTestscenarioFraJsonString(testscenarioJson, id, variables);
    }

}
