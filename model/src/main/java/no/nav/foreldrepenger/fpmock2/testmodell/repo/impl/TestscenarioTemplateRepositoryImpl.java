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

/**
 * TestscenarioRepository av alle test templates.
 */
public class TestscenarioTemplateRepositoryImpl implements TestscenarioTemplateRepository {

    public static final String SCENARIOS_DIR = "scenarios";
    public static final String SCENARIOS_DIR_PROPERTY = "scenarios.dir";

    private final Map<String, TestscenarioTemplate> testTemplates = new TreeMap<>();

    private final JsonMapper jsonMapper = new JsonMapper();

    private final File rootDir;

    public TestscenarioTemplateRepositoryImpl(File rootDir) {
        Objects.requireNonNull(rootDir, "rootDir");
        if (!rootDir.exists()) {
            throw new IllegalArgumentException("Finner ikke scenarios root : " + rootDir);
        } else if (rootDir.isDirectory()) {
            throw new IllegalArgumentException("Er ikke directory : " + rootDir);
        }
        this.rootDir = rootDir;
    }

    public TestscenarioTemplateRepositoryImpl() {
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
                testTemplates.put(dir.getName(), new FileTestscenarioTemplate(dir, inputVariable));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public File getRootDir() {
        return rootDir;
    }

    private Map<String, String> loadTemplateVars(File varsFile) throws IOException {
        if (varsFile.exists()) {
            try (InputStream is = new FileInputStream(varsFile)) {
                TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {
                };
                return jsonMapper.getObjectMapper().readValue(is, typeRef);
            }
        } else {
            return Collections.emptyMap();
        }
    }

}
