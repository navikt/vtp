package no.nav.foreldrepenger.fpmock2.testmodell.repo.impl;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelse;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TemplateVariable;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioTemplate;
import no.nav.foreldrepenger.fpmock2.testmodell.util.FindTemplateVariables;
import no.nav.foreldrepenger.fpmock2.testmodell.util.VariabelContainer;

public class StringTestscenarioTemplate implements TestscenarioTemplate {

    private final VariabelContainer vars = new VariabelContainer();
    private final String personopplysningTemplate;
    private final String inntektopplysningTemplate;
    private final String templateNavn;

    public StringTestscenarioTemplate(String templateNavn, String personopplysningTemplate, String inntektopplysningTemplate) {
        this(templateNavn, personopplysningTemplate, inntektopplysningTemplate, Collections.emptyMap());
    }

    public StringTestscenarioTemplate(String templateNavn, String personopplysningTemplate, String inntektopplysningTemplate, Map<String, String> vars) {
        this.templateNavn = templateNavn;
        this.personopplysningTemplate = personopplysningTemplate;
        this.inntektopplysningTemplate = inntektopplysningTemplate;
        this.vars.putAll(vars);
    }

    @Override
    public String getTemplateNavn() {
        return templateNavn;
    }

    @Override
    public VariabelContainer getDefaultVars() {
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

    @Override
    public Set<TemplateVariable> getExpectedVars() {
        FindTemplateVariables finder = new FindTemplateVariables();
        finder.scanForVariables(Personopplysninger.class, personopplysningReader());
        finder.scanForVariables(InntektYtelse.class, personopplysningReader());
        return finder.getDiscoveredVariables();
    }

}