package no.nav.foreldrepenger.fpmock2.testmodell.repo.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.fpmock2.testmodell.enheter.EnheterIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.identer.LokalIdentIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelse;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelseIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.PersonIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.BasisdataProvider;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioTemplate;

/** Indeks av alle testdata instanser. */
public class TestscenarioRepositoryImpl implements TestscenarioRepository {
    private static final Logger log = LoggerFactory.getLogger(TestscenarioRepositoryImpl.class);
    private final List<TestscenarioImpl> scenarios = new ArrayList<>();
    private final Map<String, LokalIdentIndeks> identer = new HashMap<>();

    private PersonIndeks personIndeks = new PersonIndeks();

    private InntektYtelseIndeks inntektYtelseIndeks = new InntektYtelseIndeks();
    private BasisdataProvider basisdata;

    public TestscenarioRepositoryImpl() throws IOException {
        this(new BasisdataProviderFileImpl());
    }

    public TestscenarioRepositoryImpl(BasisdataProvider basisdata) {
        this.basisdata = basisdata;
    }

    @Override
    public BasisdataProvider getBasisdata() {
        return basisdata;
    }

    @Override
    public EnheterIndeks getEnheterIndeks() {
        return getBasisdata().getEnheterIndeks();
    }
    
    public void indekser(TestscenarioImpl testScenario) {
        scenarios.add(testScenario);
        Personopplysninger personopplysninger = testScenario.getPersonopplysninger();
        if (personopplysninger == null) {
            log.warn("TestscenarioImpl mangler innhold:" + testScenario);
        } else {
            personIndeks.leggTil(personopplysninger.getSøker());
            personIndeks.leggTil(personopplysninger.getAnnenPart());
            personIndeks.indekserFamilierelasjonBrukere(personopplysninger.getFamilierelasjoner());

            personIndeks.indekserPersonopplysningerByIdent(personopplysninger);
        }

        InntektYtelse inntektYtelse = testScenario.getInntektYtelse();
        inntektYtelse.getModeller().forEach(iy -> inntektYtelseIndeks.leggTil(iy));
    }

    @Override
    public LokalIdentIndeks getIdenter(String unikScenarioId) {
        return identer.computeIfAbsent(unikScenarioId, n -> new LokalIdentIndeks(n, basisdata.getIdentGenerator()));
    }

    @Override
    public Collection<Testscenario> getTestscenarios() {
        return Collections.unmodifiableCollection(scenarios);
    }

    @Override
    public PersonIndeks getPersonIndeks() {
        return personIndeks;
    }

    @Override
    public Optional<InntektYtelseModell> getInntektYtelseModell(String ident) {
        return inntektYtelseIndeks.getModellForIdent(ident);
    }

    public Testscenario lagTestscenario(TestscenarioTemplate testScenarioTemplate) {
        TestscenarioFraTemplateMapper mapper = new TestscenarioFraTemplateMapper(this);
        return mapper.lagTestscenario(testScenarioTemplate);
    }

}
