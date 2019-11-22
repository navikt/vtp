package no.nav.foreldrepenger.vtp.testmodell.repo;

import no.nav.foreldrepenger.vtp.autotest.scenario.repo.TemplateVariable;
import no.nav.foreldrepenger.vtp.autotest.scenario.util.VariabelContainer;

import java.io.IOException;
import java.io.Reader;
import java.util.Set;

/** En template for et test case inklusiv testdata. */
public interface TestscenarioTemplate {

    Reader personopplysningReader() throws IOException;

    Reader inntektopplysningReader(String rolle) throws IOException;

    Reader organisasjonReader() throws IOException;

    VariabelContainer getDefaultVars();

    Set<TemplateVariable> getExpectedVars();

    String getTemplateNavn();

    /** default avleder id fra navn for enkelthets skyld. */
    default String getTemplateKey() {
        return getTemplateNavn().replaceFirst("[-_].+$", "");
    }

}
