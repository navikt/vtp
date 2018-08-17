package no.nav.foreldrepenger.fpmock2.testmodell;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import no.nav.foreldrepenger.fpmock2.testmodell.enheter.EnheterIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.enheter.Norg2Modell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.AdresseIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.AdresseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Identer;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.fpmock2.testmodell.util.JsonMapper;

class ScenarioMapper {

    private final Indeks indeks = new Indeks();

    private final JsonMapper jsonMapper = new JsonMapper();

    private EnheterIndeks enheterIndeks;

    private AdresseIndeks adresseIndeks;

    public AdresseIndeks getAdresseIndeks() {
        return adresseIndeks;
    }

    public EnheterIndeks getEnheterIndeks() {
        return enheterIndeks;
    }
    
    public Indeks getIndeks() {
        return indeks;
    }

    public void load() {
        try {
            init();
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke initialiseres med felles data", e);
        }

        File start = new File("scenarios");
        File scDir = start;
        while (scDir != null && !scDir.exists()) {
            scDir = scDir.getParentFile();
        }

        if (scDir == null) {
            throw new IllegalArgumentException("Fant ikke scenarios, fra start dir " + start);
        } else {
            for (File dir : scDir.listFiles()) {
                try {
                    loadScenario(dir);
                } catch (IOException e) {
                    throw new IllegalArgumentException("Kunne ikke lese scenario: " + dir, e);
                }
            }
        }

    }

    public void load(Scenario scenario, File dir) throws IOException {
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("Er ikke directory: " + dir);
        }

        Map<String, String> vars = initLoad(scenario, dir);

        File persFile = new File(dir, "personopplysning.json");
        if (persFile.exists()) {
            try (InputStream is = new FileInputStream(persFile)) {
                load(scenario, is, vars);
            }
        }
    }

    public void load(Scenario scenario, InputStream is) {
        load(scenario, is, Collections.emptyMap());
    }

    public void load(Scenario scenario, InputStream is, Map<String, String> vars) {
        initJsonMapper(scenario, vars);
        try {
            Personopplysninger personopplysninger = jsonMapper.getObjectMapper().readValue(is, Personopplysninger.class);
            scenario.setPersonopplysninger(personopplysninger);
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke lese json for scenario:" + scenario.getNavn(), e);
        }
    }

    public void load(Scenario scenario, Reader reader) {
        load(scenario, reader, Collections.emptyMap());
    }

    public void load(Scenario scenario, Reader reader, Map<String, String> vars) {
        initJsonMapper(scenario, vars);
        try {
            // detaljer
            Personopplysninger personopplysninger = jsonMapper.getObjectMapper().readValue(reader, Personopplysninger.class);
            scenario.setPersonopplysninger(personopplysninger);

            indeks.indekser(scenario);
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke lese json for scenario:" + scenario.getNavn(), e);
        }
    }

    public Collection<Scenario> scenarios() {
        return Collections.unmodifiableCollection(indeks.getScenarios());
    }

    public void write(Scenario scenario, File dir) throws IOException {
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("Er ikke directory: " + dir);
        }
        File søkerFile = new File(dir, "personopplysning.json");
        try (OutputStream out = new FileOutputStream(søkerFile)) {
            writeTo(out, scenario);
        }
    }

    public void writeTo(OutputStream out, Scenario scenario) {
        try {
            jsonMapper.getObjectMapper().writerWithDefaultPrettyPrinter().writeValue(out, scenario.getPersonopplysninger());
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke skrive json for scenario: " + scenario.getNavn(), e);
        }
    }

    private void addScenario(Scenario scenario) {
        indeks.indekser(scenario);
    }

    private void init() throws IOException {
        this.enheterIndeks = loadEnheter();
        this.adresseIndeks = loadAdresser();
    }

    private void initJsonMapper(Scenario scenario, Map<String, String> vars) {
        jsonMapper.addVars(vars);
        jsonMapper.addInjectable(Identer.class, scenario.getIdenter());
        jsonMapper.addInjectable(AdresseIndeks.class, scenario.getAdresseIndeks());
    }

    private Map<String, String> initLoad(Scenario scenario, File dir) throws IOException {
        // last vars først
        Map<String, String> vars = loadVars(scenario, dir);
        jsonMapper.addVars(vars);

        // adresse maler
        scenario.setAdresseIndeks(this.adresseIndeks);
        return vars;
    }

    private AdresseIndeks loadAdresser() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/basedata/adresse-maler.json")) {
            TypeReference<List<AdresseModell>> typeRef = new TypeReference<List<AdresseModell>>() {
            };
            List<AdresseModell> adresser = jsonMapper.getObjectMapper().readValue(is, typeRef);
            AdresseIndeks ai = new AdresseIndeks();
            adresser.forEach(a -> ai.leggTil(a));
            return ai;
        }
    }

    private EnheterIndeks loadEnheter() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/basedata/enheter.json")) {
            TypeReference<List<Norg2Modell>> typeRef = new TypeReference<List<Norg2Modell>>() {
            };
            List<Norg2Modell> adresser = jsonMapper.getObjectMapper().readValue(is, typeRef);
            EnheterIndeks ai = new EnheterIndeks();
            ai.leggTil(adresser);
            return ai;
        }
    }

    private void loadScenario(File dir) throws IOException {
        String scenarioNavn = dir.getName();
        Scenario scenario = new Scenario(scenarioNavn, indeks.getIdenter(scenarioNavn));
        load(scenario, dir);
        addScenario(scenario);
    }

    private Map<String, String> loadVars(Scenario scenario, File dir) throws IOException {
        File varsFile = new File(dir, "vars.json");
        if (varsFile.exists()) {
            try (InputStream is = new FileInputStream(varsFile)) {
                TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {
                };
                Map<String, String> map = jsonMapper.getObjectMapper().readValue(is, typeRef);
                scenario.setVars(map);
                return map;
            }
        } else {
            return Collections.emptyMap();
        }
    }

}
