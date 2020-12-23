package no.nav.foreldrepenger.vtp.testmodell.repo.impl;

import java.io.IOException;
import java.util.Collections;
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
import no.nav.foreldrepenger.vtp.testmodell.util.JacksonWrapperTestscenario;
import no.nav.foreldrepenger.vtp.testmodell.util.JacksonWrapperTestscenarioUtvider;
import no.nav.foreldrepenger.vtp.testmodell.virksomhet.ScenarioVirksomheter;

public class TestscenarioFraJsonMapper {

    private final TestscenarioRepositoryImpl testScenarioRepository;
    private static final ObjectMapper mapper = JacksonWrapperTestscenario.getObjectMapper();

    public TestscenarioFraJsonMapper(TestscenarioRepositoryImpl testScenarioRepository) {
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
            node = mapper.readValue(testscenarioJson, ObjectNode.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke converte JSON streng til ObjectNode", e);
        }
        return node;
    }

    private String hentTemplateNavnFraJsonString(ObjectNode node) {
        if (node.has("scenario-navn")) {
            JsonNode scenarioNavn = node.get("scenario-navn");
            return mapper.convertValue(scenarioNavn, String.class);
        } else {
            return null;
        }
    }

    private void loadTestscenarioFraJsonString(TestscenarioImpl testscenario, ObjectNode node, Map<String, String> overrideVars) {
        ObjectMapper objectMapper = lagScenariospesifikkObjectMapper(testscenario, node, overrideVars);

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

    private Map<String,String> hentScenariospesifikkeVariabler(ObjectNode node) {
        if (!node.has("vars")) {
            return Collections.emptyMap();
        }
        JsonNode vars = node.get("vars");
        return mapper.convertValue(vars, new TypeReference<>(){});
    }


    private ObjectMapper lagScenariospesifikkObjectMapper(TestscenarioImpl testscenario, ObjectNode node, Map<String, String> overrideVars){
        JacksonWrapperTestscenarioUtvider jsonMapper = new JacksonWrapperTestscenarioUtvider(testscenario.getVariabelContainer());

        /* Legger til egendefinerte variabler */
        Map<String, String> scenarioVars = hentScenariospesifikkeVariabler(node);
        jsonMapper.addVars(scenarioVars);
        jsonMapper.addVars(overrideVars);

        /* Setter opp indekser som kan injiseres i modellen. */
        AdresseIndeks adresseIndeks = this.getTestscenarioRepository().getBasisdata().getAdresseIndeks();
        testscenario.setAdresseIndeks(adresseIndeks);
        jsonMapper.addInjectable(AdresseIndeks.class, adresseIndeks);
        jsonMapper.addInjectable(LokalIdentIndeks.class, testscenario.getIdenter());
        jsonMapper.addInjectable(ScenarioVirksomheter.class, testscenario.getVirksomheter());

        return jsonMapper.lagCopyAvObjectMapperOgUtvideMedVars();
    }

}
