package no.nav.foreldrepenger.vtp.testmodell.repo.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import no.nav.foreldrepenger.vtp.testmodell.identer.LokalIdentIndeks;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModeller;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.AdresseIndeks;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioTemplate;
import no.nav.foreldrepenger.vtp.testmodell.util.JsonMapper;
import no.nav.foreldrepenger.vtp.testmodell.virksomhet.ScenarioVirksomheter;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

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

    public TestscenarioImpl lagTestscenarioFraJsonString(String testscenarioJson, String unikTestscenarioId){
        TestscenarioImpl testscenario = new TestscenarioImpl("Midlertidig-templatenavn", unikTestscenarioId, testScenarioRepository);
        loadFraJsonString(testscenario, testscenarioJson);
        return testscenario;
    }

    private void loadFraJsonString(TestscenarioImpl testscenario, String testscenarioJson) {
        Map<String,String> defaultVars = null;
        try {
            ObjectNode node = new ObjectMapper().readValue(testscenarioJson, ObjectNode.class);
            if (node.has("vars")) {
                JsonNode vars = node.get("vars");
                defaultVars = new ObjectMapper().convertValue(vars, new TypeReference<Map<String,String>>(){});
            }

            JsonMapper jsonMapper = new JsonMapper(testscenario.getVariabelContainer());
            jsonMapper.addVars(defaultVars);
            initJsonMapper(jsonMapper, testscenario);
            ObjectMapper objectMapper = jsonMapper.lagObjectMapper();

            if(node.has("organisasjon")){
                JsonNode organisasjon = node.get("organisasjon");
                OrganisasjonModeller organisasjonModeller = objectMapper.convertValue(organisasjon, OrganisasjonModeller.class);
                for (OrganisasjonModell organisasjonModell : organisasjonModeller.getModeller()) {
                    testscenario.leggTil(organisasjonModell);
                }
            }

            if(node.has("inntektytelse-søker")){
                JsonNode inntektytelse_søker = node.get("inntektytelse-søker");
                InntektYtelseModell søkerInntektYtelse = objectMapper.convertValue(inntektytelse_søker, InntektYtelseModell.class);
                testscenario.setSøkerInntektYtelse(søkerInntektYtelse);
            }

            if(node.has("inntektytelse-annenpart")){
                JsonNode inntektytelse_annenpart = node.get("inntektytelse-annenpart");
                InntektYtelseModell annenpartInntektYtelse = objectMapper.convertValue(inntektytelse_annenpart, InntektYtelseModell.class);
                testscenario.setSøkerInntektYtelse(annenpartInntektYtelse);
            }

            if(node.has("personopplysninger")){
                JsonNode personopplysningerResult = node.get("personopplysninger");
                Personopplysninger personopplysninger = objectMapper.convertValue(personopplysningerResult, Personopplysninger.class);
                testscenario.setPersonopplysninger(personopplysninger);
            }

        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke converte JSON tekst til object", e);
        }
        testScenarioRepository.indekser(testscenario);
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
                OrganisasjonModeller organisasjonModeller = objectMapper.readValue(reader, OrganisasjonModeller.class);
                for (OrganisasjonModell organisasjonModell : organisasjonModeller.getModeller()) {
                    scenario.leggTil(organisasjonModell);
                }
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
