package no.nav.foreldrepenger.vtp.testmodell.repo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TestscenarioHenter {

    private static final String PATH_TIL_SCENARIO = "scenarios/";
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
        String scenarioNavn = henterNavnPåScenarioMappe(scenarioId);
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
        try (InputStream is = TestscenarioHenter.class.getResourceAsStream("/" + PATH_TIL_SCENARIO + navnPåMappen + "/" + jsonFilNavn)) {
            if (is != null) {
                JsonNode verdiAvNøkkel = mapper.readValue(is, JsonNode.class);
                root.set(navnPåNøkkel, verdiAvNøkkel);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke lese " + jsonFilNavn + "for scenario", e);
        }
    }

    private String henterNavnPåScenarioMappe(String scenarioNummer) {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(PATH_TIL_SCENARIO))))) {
            String navnPåMappe;
            while((navnPåMappe = r.readLine()) != null) {
                if (navnPåMappe.startsWith(scenarioNummer + "-")) {
                    return navnPåMappe;
                }
            }
            return henterNavnPåScenarioMappeFraJARressurs(scenarioNummer);
        } catch (IOException e) {
            throw new IllegalArgumentException("Klarte ikke å hente resource {scenarios}", e);
        }
    }

    private String henterNavnPåScenarioMappeFraJARressurs(String scenarioNummer) {
        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        if(jarFile.isFile()) {
            try (JarFile jar = new JarFile(jarFile)) {
                final Enumeration<JarEntry> entries = jar.entries();
                while(entries.hasMoreElements()) {
                    final String name = entries.nextElement().getName();
                    if (name.startsWith(PATH_TIL_SCENARIO + scenarioNummer)) { // Finner mappe med korrekt scenarioID
                        int startIndeks = PATH_TIL_SCENARIO.length();
                        int sluttIndeks = name.indexOf("/", startIndeks);
                        return name.substring(startIndeks, sluttIndeks);
                    }
                }
            } catch (IOException e) {
                throw new IllegalArgumentException("Klarte ikke å åpne JAR-fil", e);
            }
        }
        return null;
    }
}
