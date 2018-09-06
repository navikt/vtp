package no.nav.foreldrepenger.fpmock2.testmodell.repo.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Map;
import java.util.TreeMap;

import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioTemplate;

public class FileTestscenarioTemplate implements TestscenarioTemplate {

    public static final String INNTEKTYTELSE_JSON_FILE = "inntektytelse.json";
    public static final String PERSONOPPLYSNING_JSON_FILE = "personopplysning.json";
    public static final String VARS_JSON_FILE = "vars.json";
    
    private Map<String, String> vars = new TreeMap<>();
    private File templateDir;

    public FileTestscenarioTemplate(File templateDir, Map<String, String> vars) {
        this.templateDir = templateDir;
        this.vars.putAll(vars);
    }

    @Override
    public String getTemplateNavn() {
        return templateDir.getName();
    }

    @Override
    public Map<String, String> getDefaultVars() {
        return vars;
    }

    @Override
    public Reader personopplysningReader() throws FileNotFoundException {
        return new FileReader(new File(templateDir, PERSONOPPLYSNING_JSON_FILE));
    }

    @Override
    public Reader inntektopplysningReader() throws FileNotFoundException {
        return new FileReader(new File(templateDir, INNTEKTYTELSE_JSON_FILE));
    }

}