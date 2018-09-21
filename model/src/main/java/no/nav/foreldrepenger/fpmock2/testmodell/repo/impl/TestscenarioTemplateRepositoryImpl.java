package no.nav.foreldrepenger.fpmock2.testmodell.repo.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import com.fasterxml.jackson.core.type.TypeReference;

import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioTemplate;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioTemplateRepository;
import no.nav.foreldrepenger.fpmock2.testmodell.util.JsonMapper;
import no.nav.foreldrepenger.fpmock2.testmodell.util.VariabelContainer;

/**
 * TestscenarioRepository av alle test templates.
 */
public class TestscenarioTemplateRepositoryImpl implements TestscenarioTemplateRepository {

    public static final String SCENARIOS_DIR = "scenarios";
    public static final String SCENARIOS_DIR_PROPERTY = "scenarios.dir";

    private final Map<String, TestscenarioTemplate> testTemplates = new TreeMap<>();

    private final JsonMapper jsonMapper = new JsonMapper();

    private final File rootDir;

    private static TestscenarioTemplateRepositoryImpl testscenarioTemplateRepository;

    public TestscenarioTemplateRepositoryImpl(File rootDir) {
        Objects.requireNonNull(rootDir, "rootDir");
        if (!rootDir.exists()) {
            throw new IllegalArgumentException("Finner ikke scenarios root : " + rootDir);
        } else if (rootDir.isDirectory()) {
            throw new IllegalArgumentException("Er ikke directory : " + rootDir);
        }
        this.rootDir = rootDir;
    }

    public static synchronized TestscenarioTemplateRepositoryImpl getInstance(){
        if(testscenarioTemplateRepository == null){
            testscenarioTemplateRepository = new TestscenarioTemplateRepositoryImpl();
        }
        return testscenarioTemplateRepository;
    }




    private TestscenarioTemplateRepositoryImpl() {
        File start = new File(System.getProperty(SCENARIOS_DIR_PROPERTY, "."));
        File scDir = start;
        File scenariosDir = new File(scDir, SCENARIOS_DIR);
        while (scDir != null && !scenariosDir.exists()) {
            scenariosDir = new File(scDir, SCENARIOS_DIR);
            scDir = scDir.getParentFile();
        }

        if (scDir == null) {
            String workDir = new File(".").getAbsolutePath();
            throw new IllegalArgumentException("Fant ikke scenarios dir, fra property scenarios.dir=" + start + ", workdir=" + workDir);
        } else {
            this.rootDir = scenariosDir;
        }
    }

    @Override
    public Collection<TestscenarioTemplate> getTemplates() {
        return testTemplates.values();
    }

    public void load() {
        load(null);
    }

    /**
     * Overloaded - Inject egne variable.
     */
    public void load(Map<String, String> vars) {

        for (File dir : rootDir.listFiles()) {
            if (!dir.isDirectory()) {
                continue;
            }

            try {
                Map<String, String> scenarioDefaultVars = loadTemplateVars(new File(dir, FileTestscenarioTemplate.VARS_JSON_FILE));
                Map<String, String> inputVariable = new TreeMap<>(scenarioDefaultVars);
                if (vars != null) {
                    // overstyr med angitte vars
                    inputVariable.putAll(vars);
                }
                VariabelContainer variables = new VariabelContainer(inputVariable);
                FileTestscenarioTemplate template = new FileTestscenarioTemplate(dir, variables);
                testTemplates.put(template.getTemplateKey(), template);
            } catch (IOException e) {
                throw new IllegalStateException("Kunne ikke laste template fra dir:" + dir.getAbsolutePath(), e);
            }
        }

    }

    @Override
    public TestscenarioTemplate finn(String templateKey) {
        return testTemplates.get(templateKey);
    }

    public File getRootDir() {
        return rootDir;
    }

    private Map<String, String> loadTemplateVars(File varsFile) throws IOException {
        if (varsFile.exists()) {
            try (InputStream is = new FileInputStream(varsFile)) {
                TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {
                };
                return jsonMapper.lagObjectMapper().readValue(is, typeRef);
            }
        } else {
            return Collections.emptyMap();
        }
    }

}
