package no.nav.foreldrepenger.fpmock2.testmodell.repo.impl;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioTemplate;

public class StringTestscenarioTemplate implements TestscenarioTemplate {

    private Map<String, String> vars = new TreeMap<>();
    private String personopplysningTemplate;
    private String inntektopplysningTemplate;

    public StringTestscenarioTemplate(String personopplysningTemplate, String inntektopplysningTemplate) {
        this(personopplysningTemplate, inntektopplysningTemplate, Collections.emptyMap());
    }

    public StringTestscenarioTemplate(String personopplysningTemplate, String inntektopplysningTemplate, Map<String, String> vars) {
        this.personopplysningTemplate = personopplysningTemplate;
        this.inntektopplysningTemplate = inntektopplysningTemplate;
        this.vars.putAll(vars);
    }

    @Override
    public String getTemplateNavn() {
        return null;
    }

    @Override
    public Map<String, String> getDefaultVars() {
        return vars;
    }

    @Override
    public Reader personopplysningReader() {
        return personopplysningTemplate == null ? null : new StringReader(personopplysningTemplate);
    }

    @Override
    public Reader inntektopplysningReader() {
        return inntektopplysningTemplate == null ? null : new StringReader(inntektopplysningTemplate);
    }

}