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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import no.nav.foreldrepenger.fpmock2.testmodell.enheter.EnheterIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.enheter.Norg2Modell;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelse;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.AdresseIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.AdresseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.fpmock2.testmodell.util.JsonMapper;
import no.nav.foreldrepenger.fpmock2.testmodell.virksomhet.VirksomhetIndeks;
import no.nav.foreldrepenger.fpmock2.testmodell.virksomhet.VirksomhetModell;

class ScenarioMapper {

    private static VirksomhetIndeks virksomhetIndeks;

    private final Indeks indeks = new Indeks();
    private final JsonMapper jsonMapper = new JsonMapper();
    private final ObjectMapper jsonObjectMapper = jsonMapper.getObjectMapper();

    private final ObjectWriter jsonWriter = jsonObjectMapper.writerWithDefaultPrettyPrinter();

    private EnheterIndeks enheterIndeks;

    private AdresseIndeks adresseIndeks;

    public ScenarioMapper() {
        try {
            init();
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke initialiseres med felles data", e);
        }

    }

    public AdresseIndeks getAdresseIndeks() {
        return adresseIndeks;
    }

    public EnheterIndeks getEnheterIndeks() {
        return enheterIndeks;
    }

    public Indeks getIndeks() {
        return indeks;
    }

    public VirksomhetIndeks getVirksomheter() {
        try {
            return loadVirksomheter();
        } catch (IOException e) {
            throw new IllegalStateException("Kunne ikke laste VirksomhetIndeks", e);
        }
    }

    /** @deprecated ScenarioMapper skal ikke brukes til mer enn et scenario. */
    public void load() {
        load(null);
    }

    /**
     * Inject egne variable.
     * 
     * @deprecated ScenarioMapper skal ikke brukes til mer enn et scenario.
     */
    public void load(Map<String, String> vars) {
        String workDir = new File(".").getAbsolutePath();
        File start = new File(System.getProperty("scenarios.dir", "."));
        File scDir = start;
        File scenariosDir = new File(scDir, "scenarios");
        while (scDir != null && !scenariosDir.exists()) {
            scenariosDir = new File(scDir, "scenarios");
            scDir = scDir.getParentFile();
        }

        if (scDir == null) {
            throw new IllegalArgumentException("Fant ikke scenarios dir, fra property scenarios.dir=" + start + ", workdir="+workDir);
        } else {
            for (File dir : scenariosDir.listFiles()) {
                if(!dir.isDirectory()) {
                    continue;
                }
                try {
                    loadScenario(dir, vars == null ? Collections.emptyMap() : vars);
                } catch (IOException e) {
                    throw new IllegalArgumentException("Kunne ikke lese scenario: " + dir, e);
                }
            }
        }

    }

    public void load(Scenario scenario, Reader reader) {
        load(scenario, reader, Collections.emptyMap());
    }

    public void load(Scenario scenario, Reader reader, Map<String, String> vars) {
        jsonMapper.addVars(vars);
        initJsonMapper(scenario);
        try {
            // detaljer
            Personopplysninger personopplysninger = jsonMapper.getObjectMapper().readValue(reader, Personopplysninger.class);
            scenario.setPersonopplysninger(personopplysninger);

            indeks.indekser(scenario);
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke lese json for scenario:" + scenario.getNavn(), e);
        }
    }

    public void loadInntektytelse(Scenario scenario, InputStream is, Map<String, String> vars) {
        jsonMapper.addVars(vars);
        initJsonMapper(scenario);
        try {
            InntektYtelse inntektYtelse = jsonMapper.getObjectMapper().readValue(is, InntektYtelse.class);
            for (InntektYtelseModell iym : inntektYtelse.getModeller()) {
                scenario.leggTil(iym);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke lese json for scenario:" + scenario.getNavn(), e);
        }
    }

    public void loadPersonopplysninger(Scenario scenario, InputStream is, Map<String, String> vars) {
        jsonMapper.addVars(vars);
        initJsonMapper(scenario);
        try {
            Personopplysninger personopplysninger = jsonMapper.getObjectMapper().readValue(is, Personopplysninger.class);
            scenario.setPersonopplysninger(personopplysninger);
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke lese json for scenario:" + scenario.getNavn(), e);
        }
    }

    public void loadScenario(File scenarioDir, Map<String, String> vars) throws IOException {
        String scenarioNavn = scenarioDir.getName();
        Scenario scenario = new Scenario(scenarioNavn, indeks.getIdenter(scenarioNavn), getVirksomheter());
        load(scenario, scenarioDir, vars);
        addScenario(scenario);
    }

    public Collection<Scenario> scenarios() {
        return Collections.unmodifiableCollection(indeks.getScenarios());
    }

    public void skrivInntektYtelse(OutputStream out, Scenario scenario, boolean canonical) {
        try {
            if (!canonical) {
                jsonWriter.writeValue(out, scenario.getInntektYtelse());
            } else {
                jsonMapper.canonicalObjectMapper().writerWithDefaultPrettyPrinter().writeValue(out, scenario.getInntektYtelse());
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke skrive json for scenario: " + scenario.getNavn(), e);
        }
    }

    public void skrivPersonopplysninger(OutputStream out, Scenario scenario, boolean canonical) {
        try {
            if (!canonical) {
                jsonWriter.writeValue(out, scenario.getPersonopplysninger());
            } else {
                jsonMapper.canonicalObjectMapper().writerWithDefaultPrettyPrinter().writeValue(out, scenario.getPersonopplysninger());
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke skrive json for scenario: " + scenario.getNavn(), e);
        }
    }

    public void write(Scenario scenario, File dir, boolean canonical) throws IOException {
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("Er ikke directory: " + dir);
        }

        File søkerFile = new File(dir, "personopplysning.json");
        try (OutputStream out = new FileOutputStream(søkerFile)) {
            skrivPersonopplysninger(out, scenario, canonical);
        }

        File inntektYtelseFile = new File(dir, "inntektytelse.json");
        try (OutputStream out = new FileOutputStream(inntektYtelseFile)) {
            skrivInntektYtelse(out, scenario, canonical);
        }
    }

    private void addScenario(Scenario scenario) {
        indeks.indekser(scenario);
    }

    private void init() throws IOException {
        if (enheterIndeks == null) {
            this.enheterIndeks = loadEnheter();
        }
        if (adresseIndeks == null) {
            this.adresseIndeks = loadAdresser();
        }
    }

    /** Setter opp indekser som kan injiseres i modellen. */
    private void initJsonMapper(Scenario scenario) {
        jsonMapper.addInjectable(ScenarioIdenter.class, scenario.getIdenter());
        jsonMapper.addInjectable(AdresseIndeks.class, scenario.getAdresseIndeks());
        jsonMapper.addInjectable(ScenarioVirksomheter.class, scenario.getVirksomheter());
    }

    private Map<String, String> initLoad(Scenario scenario, File dir) throws IOException {
        // last vars først
        Map<String, String> vars = loadVars(scenario, dir);
        jsonMapper.addVars(vars);

        // adresse maler
        scenario.setAdresseIndeks(this.adresseIndeks);
        return vars;
    }

    private void load(Scenario scenario, File dir, Map<String, String> vars) throws IOException {
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("Er ikke directory: " + dir);
        }

        initLoad(scenario, dir);
        jsonMapper.addVars(vars);

        File persFile = new File(dir, "personopplysning.json");
        if (persFile.exists()) {
            try (InputStream is = new FileInputStream(persFile)) {
                loadPersonopplysninger(scenario, is, vars);
            }
        }

        File inntektYtelseFile = new File(dir, "inntektytelse.json");
        if (inntektYtelseFile.exists()) {
            try (InputStream is = new FileInputStream(inntektYtelseFile)) {
                loadInntektytelse(scenario, is, vars);
            }
        }

    }

    private AdresseIndeks loadAdresser() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/basedata/adresse-maler.json")) {
            TypeReference<List<AdresseModell>> typeRef = new TypeReference<List<AdresseModell>>() {
            };
            List<AdresseModell> adresser = jsonObjectMapper.readValue(is, typeRef);
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

    private VirksomhetIndeks loadVirksomheter() throws IOException {
        if (virksomhetIndeks == null) {
            try (InputStream is = getClass().getResourceAsStream("/basedata/virksomheter.json")) {
                TypeReference<List<VirksomhetModell>> typeRef = new TypeReference<List<VirksomhetModell>>() {
                };
                List<VirksomhetModell> virksomheter = jsonMapper.getObjectMapper().readValue(is, typeRef);
                VirksomhetIndeks ai = new VirksomhetIndeks();
                ai.leggTil(virksomheter);
                return ai;
            }
        }
        return virksomhetIndeks;
    }

}
