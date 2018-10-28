package no.nav.foreldrepenger.fpmock2.testmodell.repo.impl;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.fpmock2.testmodell.identer.LokalIdentIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.organisasjon.OrganisasjonModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.AdresseIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Personopplysninger;
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

    public TestscenarioBuilderRepositoryImpl getTestscenarioRepository() {
        return testScenarioRepository;
    }

    public TestscenarioImpl lagTestscenario(TestscenarioTemplate template) {
        String uuid = UUID.randomUUID().toString();
        Map<String, String> emptyMap = Collections.emptyMap();
        return lagTestscenario(template, uuid, emptyMap);
    }

    public TestscenarioImpl lagTestscenario(TestscenarioTemplate template, String unikTestscenarioId, Map<String, String> vars) {
        TestscenarioImpl testScenario = new TestscenarioImpl(template.getTemplateNavn(), unikTestscenarioId, testScenarioRepository);
        load(testScenario, template, vars);
        return testScenario;
    }

    private void load(TestscenarioImpl scenario, TestscenarioTemplate template, Map<String, String> overrideVars) {
        JsonMapper jsonMapper = new JsonMapper(scenario.getVariabelContainer());
        jsonMapper.addVars(template.getDefaultVars());
        jsonMapper.addVars(overrideVars);
        initJsonMapper(jsonMapper, scenario);
        ObjectMapper objectMapper = jsonMapper.lagObjectMapper();
        try (Reader reader = template.personopplysningReader()) {
            if (reader != null) {
                Personopplysninger personopplysninger = objectMapper.readValue(reader, Personopplysninger.class);
                scenario.setPersonopplysninger(personopplysninger);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke lese personopplysning.json for scenario:" + scenario, e);
        }

        try (Reader reader = template.inntektopplysningReader("søker")) {
            // detaljer
            if (reader != null) {
                InntektYtelseModell inntektYtelse = objectMapper.readValue(reader, InntektYtelseModell.class);
                scenario.setSøkerInntektYtelse(inntektYtelse);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke lese inntektytelser-søker.json for scenario:" + scenario, e);
        }

        if (scenario.getPersonopplysninger().getAnnenPart() != null) {
            try (Reader reader = template.inntektopplysningReader("annenpart")) {
                // detaljer
                if (reader != null) {
                    InntektYtelseModell inntektYtelse = objectMapper.readValue(reader, InntektYtelseModell.class);
                    scenario.setAnnenpartInntektYtelse(inntektYtelse);
                }
            } catch (IOException e) {
                throw new IllegalArgumentException("Kunne ikke lese inntektytelser-annenpart.json for scenario:" + scenario, e);
            }
        }

        try (Reader reader = template.organisasjonReader()) {
            // detaljer
            if (reader != null) {
                OrganisasjonModell organisasjonModell = objectMapper.readValue(reader, OrganisasjonModell.class);
                scenario.leggTil(organisasjonModell);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke lese organisasjon.json for scenario:" + scenario, e);
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
