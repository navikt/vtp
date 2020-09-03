package no.nav.foreldrepenger.vtp.testmodell.repo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TestscenarioHenter {

    private static final String PERSONOPPLYSNING_JSON_FIL_NAVN = "personopplysning.json";
    private static final String INNTEKTYTELSE_SØKER_JSON_FIL_NAVN = "inntektytelse-søker.json";
    private static final String INNTEKTYTELSE_ANNENPART_JSON_FIL_NAVN = "inntektytelse-annenpart.json";
    private static final String ORGANISASJON_JSON_FIL_NAVN = "organisasjon.json";
    private static final String VARS_JSON_FIL_NAVN = "vars.json";

    private static final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, Object> scenarioObjects = new TreeMap<>();

    private static TestscenarioHenter testscenarioHenter;

    public static synchronized TestscenarioHenter getInstance(){
        if(testscenarioHenter == null){
            testscenarioHenter = new TestscenarioHenter();
        }
        return testscenarioHenter;
    }

    public Object hentScenario(String scenarioId) {
        if (scenarioObjects.containsKey(scenarioId)) {
            return scenarioObjects.get(scenarioId);
        }
        return LesOgReturnerScenarioFraJsonfil(scenarioId);
    }

    public String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    private Object LesOgReturnerScenarioFraJsonfil(String scenarioId) {
        String scenarioNavn = hentScenarioFileneSomStarterMed(scenarioId);
        System.out.println("---------\n" + scenarioNavn + "\n------------");
        if (scenarioNavn == null) {
            throw new RuntimeException("Fant ikke scenario med scenario nummer [" + scenarioId + "]");
        }

        final ObjectNode root = mapper.createObjectNode();
        root.set("scenario-navn", mapper.convertValue(scenarioNavn, new TypeReference<>() {}));
        lesFilOgLeggTilIObjectNode(scenarioNavn, root, PERSONOPPLYSNING_JSON_FIL_NAVN, "personopplysninger");
        lesFilOgLeggTilIObjectNode(scenarioNavn, root, INNTEKTYTELSE_SØKER_JSON_FIL_NAVN, "inntektytelse-søker");
        lesFilOgLeggTilIObjectNode(scenarioNavn, root, INNTEKTYTELSE_ANNENPART_JSON_FIL_NAVN,   "inntektytelse-annenpart");
        lesFilOgLeggTilIObjectNode(scenarioNavn, root, ORGANISASJON_JSON_FIL_NAVN, "organisasjon");
        lesFilOgLeggTilIObjectNode(scenarioNavn, root, VARS_JSON_FIL_NAVN, "vars");

        Object obj = mapper.convertValue(root, new TypeReference<>() {});
        scenarioObjects.put(scenarioId, obj);
        return obj;
    }


    private void lesFilOgLeggTilIObjectNode(String navnPåMappen, ObjectNode root, String jsonFilNavn, String navnPåNøkkel) {
        try (InputStream is = TestscenarioHenter.class.getResourceAsStream("/scenario/" + navnPåMappen + "/" + jsonFilNavn)) {
            JsonNode verdiAvNøkkel = mapper.readValue(is, JsonNode.class);
            root.set(navnPåNøkkel, verdiAvNøkkel);
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke lese " + jsonFilNavn + "for scenario", e);
        }
    }


    private String hentScenarioFileneSomStarterMed(String scenarioNummer) {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(TestscenarioHenter.class.getResourceAsStream("/scenario")))) {
            String navnPåMappe;
            while((navnPåMappe = r.readLine()) != null) {
                System.out.println(navnPåMappe);
                if (navnPåMappe.startsWith(scenarioNummer + "-")) {
                    return navnPåMappe;
                }
            }
            return null;
        } catch (IOException e) {
            throw new IllegalArgumentException("Klarte ikke å hente resource {scenarios}", e);
        }
    }

}
