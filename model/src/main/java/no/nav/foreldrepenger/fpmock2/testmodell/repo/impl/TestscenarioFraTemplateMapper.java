package no.nav.foreldrepenger.fpmock2.testmodell.repo.impl;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import no.nav.foreldrepenger.fpmock2.testmodell.identer.LokalIdentIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelse;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.AdresseIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioTemplate;
import no.nav.foreldrepenger.fpmock2.testmodell.util.JsonMapper;
import no.nav.foreldrepenger.fpmock2.testmodell.virksomhet.ScenarioVirksomheter;

public class TestscenarioFraTemplateMapper {

    private final TestscenarioRepositoryImpl testScenarioRepository;

    public TestscenarioFraTemplateMapper(TestscenarioRepositoryImpl testScenarioRepository) {
        Objects.requireNonNull(testScenarioRepository, "testScenarioRepository");
        this.testScenarioRepository = testScenarioRepository;
    }

    public TestscenarioRepositoryImpl getTestscenarioRepository() {
        return testScenarioRepository;
    }

    public Testscenario lagTestscenario(TestscenarioTemplate template) {
        String uuid = UUID.randomUUID().toString();
        Map<String, String> emptyMap = Collections.emptyMap();
        return lagTestscenario(template, uuid, emptyMap);
    }

    public Testscenario lagTestscenario(TestscenarioTemplate template, String unikTestscenarioId, Map<String, String> vars) {
        TestscenarioImpl testScenario = new TestscenarioImpl(template.getTemplateNavn(), unikTestscenarioId, testScenarioRepository);
        load(testScenario, template, vars);
        return testScenario;
    }

    private void load(TestscenarioImpl scenario, TestscenarioTemplate template, Map<String, String> vars) {
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.addVars(template.getDefaultVars());
        jsonMapper.addVars(vars);
        initJsonMapper(jsonMapper, scenario);
        try (Reader reader = template.personopplysningReader()) {
            if (reader != null) {
                Personopplysninger personopplysninger = jsonMapper.getObjectMapper().readValue(reader, Personopplysninger.class);
                scenario.setPersonopplysninger(personopplysninger);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke lese personopplysning.json for scenario:" + scenario, e);
        }

        try (Reader reader = template.inntektopplysningReader()) {
            // detaljer
            if (reader != null) {
                InntektYtelse inntektYtelse = jsonMapper.getObjectMapper().readValue(reader, InntektYtelse.class);
                for (InntektYtelseModell iym : inntektYtelse.getModeller()) {
                    scenario.leggTil(iym);
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke lese inntektytelser.json for scenario:" + scenario, e);
        }

        testScenarioRepository.indekser(scenario);
    }

    /** Setter opp indekser som kan injiseres i modellen. */
    private void initJsonMapper(JsonMapper jsonMapper, TestscenarioImpl scenario) {
        // adresse maler
        AdresseIndeks adresseIndeks = this.getTestscenarioRepository().getBasisdata().getAdresseIndeks();
        scenario.setAdresseIndeks(adresseIndeks);

        jsonMapper.addInjectable(LokalIdentIndeks.class, scenario.getIdenter());
        jsonMapper.addInjectable(AdresseIndeks.class, adresseIndeks);
        jsonMapper.addInjectable(ScenarioVirksomheter.class, scenario.getVirksomheter());

    }

}
