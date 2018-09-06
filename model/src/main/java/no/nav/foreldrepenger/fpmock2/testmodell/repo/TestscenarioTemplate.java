package no.nav.foreldrepenger.fpmock2.testmodell.repo;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

/** En template for et test case inklusiv testdata. */
public interface TestscenarioTemplate {

    Reader personopplysningReader() throws IOException;

    Reader inntektopplysningReader() throws IOException;

    Map<String, String> getDefaultVars();

    String getTemplateNavn();

}