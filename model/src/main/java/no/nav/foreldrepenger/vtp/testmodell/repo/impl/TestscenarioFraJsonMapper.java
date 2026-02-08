package no.nav.foreldrepenger.vtp.testmodell.repo.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import no.nav.foreldrepenger.vtp.testmodell.identer.LokalIdentIndeks;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModeller;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.AdresseIndeks;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioImpl;
import no.nav.foreldrepenger.vtp.testmodell.util.JacksonObjectMapperTestscenario;
import no.nav.foreldrepenger.vtp.testmodell.util.JacksonObjectMapperTestscenarioUtvider;
import no.nav.foreldrepenger.vtp.testmodell.virksomhet.ScenarioVirksomheter;

public class TestscenarioFraJsonMapper {

    private final TestscenarioRepositoryImpl testScenarioRepository;
    private static final JsonMapper JSON_MAPPER = JacksonObjectMapperTestscenario.getJsonMapper();

    public TestscenarioFraJsonMapper(TestscenarioRepositoryImpl testScenarioRepository) {
        Objects.requireNonNull(testScenarioRepository, "testScenarioRepository");
        this.testScenarioRepository = testScenarioRepository;
    }

    public TestscenarioBuilderRepositoryImpl getTestscenarioRepository() {
        return testScenarioRepository;
    }

    public TestscenarioImpl lagTestscenarioFraJsonString(String testscenarioJson, String unikTestscenarioId, Map<String, String> vars) {
        var node = hentObjectNodeForTestscenario(testscenarioJson);
        var templateNavn = hentTemplateNavnFraJsonString(node);
        var testscenarioImpl = new TestscenarioImpl(templateNavn, unikTestscenarioId, testScenarioRepository);
        loadTestscenarioFraJsonString(testscenarioImpl, node, vars);
        return testscenarioImpl;
    }

    private ObjectNode hentObjectNodeForTestscenario(String testscenarioJson) {
        ObjectNode node;
        try {
            node = JSON_MAPPER.readValue(testscenarioJson, ObjectNode.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke converte JSON streng til ObjectNode", e);
        }
        return node;
    }

    private String hentTemplateNavnFraJsonString(ObjectNode node) {
        if (node.has("scenario-navn")) {
            var scenarioNavn = node.get("scenario-navn");
            return JSON_MAPPER.convertValue(scenarioNavn, String.class);
        } else {
            return null;
        }
    }

    private void loadTestscenarioFraJsonString(TestscenarioImpl testscenario, ObjectNode node, Map<String, String> overrideVars) {
        var objectMapper = lagScenariospesifikkObjectMapper(testscenario, node, overrideVars);

        if(node.has("organisasjon")){
            var organisasjon = node.get("organisasjon");
            var organisasjonModeller = objectMapper.convertValue(organisasjon, OrganisasjonModeller.class);
            for (OrganisasjonModell organisasjonModell : organisasjonModeller.getModeller()) {
                testscenario.leggTil(organisasjonModell);
            }
        }

        if(node.has("inntektytelse-søker")){
            var inntektytelseSøker = node.get("inntektytelse-søker");
            var søkerInntektYtelse = objectMapper.convertValue(inntektytelseSøker, InntektYtelseModell.class);
            testscenario.setSøkerInntektYtelse(søkerInntektYtelse);
        }

        if(node.has("inntektytelse-annenpart")){
            var inntektytelseAnnenpart = node.get("inntektytelse-annenpart");
            var annenpartInntektYtelse = objectMapper.convertValue(inntektytelseAnnenpart, InntektYtelseModell.class);
            testscenario.setAnnenpartInntektYtelse(annenpartInntektYtelse);
        }

        if(node.has("personopplysninger")){
            var personopplysningerResult = node.get("personopplysninger");
            var personopplysninger = objectMapper.convertValue(personopplysningerResult, Personopplysninger.class);
            testscenario.setPersonopplysninger(personopplysninger);
        }

        testScenarioRepository.indekser(testscenario);
    }

    private Map<String,String> hentScenariospesifikkeVariabler(ObjectNode node) {
        if (!node.has("vars")) {
            return Collections.emptyMap();
        }
        var vars = node.get("vars");
        return JSON_MAPPER.convertValue(vars, new TypeReference<>(){});
    }


    private JsonMapper lagScenariospesifikkObjectMapper(TestscenarioImpl testscenario, ObjectNode node, Map<String, String> overrideVars){
        var jacksonWrapper = new JacksonObjectMapperTestscenarioUtvider(testscenario.getVariabelContainer());

        /* Legger til egendefinerte variabler */
        var scenarioVars = hentScenariospesifikkeVariabler(node);
        jacksonWrapper.addVars(scenarioVars);
        jacksonWrapper.addVars(overrideVars);

        /* Setter opp indekser som kan injiseres i modellen. */
        var adresseIndeks = this.getTestscenarioRepository().getBasisdata().getAdresseIndeks();
        testscenario.setAdresseIndeks(adresseIndeks);
        jacksonWrapper.addInjectable(AdresseIndeks.class, adresseIndeks);
        jacksonWrapper.addInjectable(LokalIdentIndeks.class, testscenario.getIdenter());
        jacksonWrapper.addInjectable(ScenarioVirksomheter.class, testscenario.getVirksomheter());

        return jacksonWrapper.lagCopyAvObjectMapperOgUtvideMedVars();
    }

}
