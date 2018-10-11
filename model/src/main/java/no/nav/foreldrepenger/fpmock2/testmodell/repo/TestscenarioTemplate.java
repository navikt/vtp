package no.nav.foreldrepenger.fpmock2.testmodell.repo;

import java.io.IOException;
import java.io.Reader;
import java.util.Set;

import no.nav.foreldrepenger.fpmock2.testmodell.util.VariabelContainer;

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
