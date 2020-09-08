package no.nav.foreldrepenger.vtp.testmodell.repo.impl;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

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
import no.nav.foreldrepenger.vtp.testmodell.util.JsonMapper;
import no.nav.foreldrepenger.vtp.testmodell.virksomhet.ScenarioVirksomheter;

public class TestscenarioFraTemplateMapper {

    private final TestscenarioRepositoryImpl testScenarioRepository;

    public TestscenarioFraTemplateMapper(TestscenarioRepositoryImpl testScenarioRepository) {
        Objects.requireNonNull(testScenarioRepository, "testScenarioRepository");
        this.testScenarioRepository = testScenarioRepository;
    }

    public TestscenarioBuilderRepositoryImpl getTestscenarioRepository() {
        return testScenarioRepository;
    }

    public TestscenarioImpl lagTestscenarioFraJsonString(String testscenarioJson, String unikTestscenarioId, Map<String, String> vars) {
        ObjectNode node = hentObjectNodeForTestscenario(testscenarioJson);
        String templateNavn = hentTemplateNavnFraJsonString(node);
        TestscenarioImpl testscenarioImpl = new TestscenarioImpl(templateNavn, unikTestscenarioId, testScenarioRepository);
        loadTestscenarioFraJsonString(testscenarioImpl, node, vars);
        return testscenarioImpl;
    }

    private ObjectNode hentObjectNodeForTestscenario(String testscenarioJson) {
        ObjectNode node;
        try {
            node = new ObjectMapper().readValue(testscenarioJson, ObjectNode.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke converte JSON streng til ObjectNode", e);
        }
        return node;
    }

    private String hentTemplateNavnFraJsonString(ObjectNode node) {
        if (node.has("scenario-navn")) {
            JsonNode scenarioNavn = node.get("scenario-navn");
            return new ObjectMapper().convertValue(scenarioNavn, String.class);
        } else {
            return null;
        }
    }


    private void loadTestscenarioFraJsonString(TestscenarioImpl testscenario, ObjectNode node, Map<String, String> overrideVars) {
        JsonMapper jsonMapper = new JsonMapper(testscenario.getVariabelContainer());
        if (node.has("vars")) {
            JsonNode vars = node.get("vars");
            Map<String,String> defaultVars = new ObjectMapper().convertValue(vars, new TypeReference<>() {});
            jsonMapper.addVars(defaultVars);
        }

        jsonMapper.addVars(overrideVars);
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
            JsonNode inntektytelseSøker = node.get("inntektytelse-søker");
            InntektYtelseModell søkerInntektYtelse = objectMapper.convertValue(inntektytelseSøker, InntektYtelseModell.class);
            testscenario.setSøkerInntektYtelse(søkerInntektYtelse);
        }

        if(node.has("inntektytelse-annenpart")){
            JsonNode inntektytelseAnnenpart = node.get("inntektytelse-annenpart");
            InntektYtelseModell annenpartInntektYtelse = objectMapper.convertValue(inntektytelseAnnenpart, InntektYtelseModell.class);
            testscenario.setAnnenpartInntektYtelse(annenpartInntektYtelse);
        }

        if(node.has("personopplysninger")){
            JsonNode personopplysningerResult = node.get("personopplysninger");
            Personopplysninger personopplysninger = objectMapper.convertValue(personopplysningerResult, Personopplysninger.class);
            testscenario.setPersonopplysninger(personopplysninger);
        }

        testScenarioRepository.indekser(testscenario);
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
