package no.nav.foreldrepenger.fpmock2.testmodell.repo.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Set;

import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TemplateVariable;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioTemplate;
import no.nav.foreldrepenger.fpmock2.testmodell.util.FindTemplateVariables;
import no.nav.foreldrepenger.fpmock2.testmodell.util.VariabelContainer;

public class FileTestscenarioTemplate implements TestscenarioTemplate {

    public static final String PERSONOPPLYSNING_JSON_FILE = "personopplysning.json";
    public static final String VARS_JSON_FILE = "vars.json";

    private VariabelContainer vars = new VariabelContainer();
    private File templateDir;

    public FileTestscenarioTemplate(File templateDir, VariabelContainer vars) {
        this.templateDir = templateDir;
        this.vars.putAll(vars);
    }

    @Override
    public String getTemplateNavn() {
        return templateDir.getName();
    }

    @Override
    public VariabelContainer getDefaultVars() {
        return vars;
    }

    @Override
    public Set<TemplateVariable> getExpectedVars() {
        try (Reader personopplysningReader = personopplysningReader();
                Reader søkerInntektopplysningReader = inntektopplysningReader("søker");
                Reader annenpartInntektopplysningReader = inntektopplysningReader("annenpart");) {
            FindTemplateVariables finder = new FindTemplateVariables();

            finder.scanForVariables(Personopplysninger.class, personopplysningReader);
            finder.scanForVariables(InntektYtelseModell.class, søkerInntektopplysningReader);
            finder.scanForVariables(InntektYtelseModell.class, annenpartInntektopplysningReader);

            return finder.getDiscoveredVariables();

        } catch (IOException e) {
            throw new IllegalStateException("Kunne ikke parse json", e);
        }
    }

    @Override
    public Reader personopplysningReader() throws FileNotFoundException {
        File file = new File(templateDir, PERSONOPPLYSNING_JSON_FILE);
        return file.exists() ? new FileReader(file) : null;
    }

    @Override
    public Reader inntektopplysningReader(String rolle) throws FileNotFoundException {
        File file = new File(templateDir, String.format("inntektytelse-%s.json", rolle));
        return file.exists() ? new FileReader(file) : null;
    }

}