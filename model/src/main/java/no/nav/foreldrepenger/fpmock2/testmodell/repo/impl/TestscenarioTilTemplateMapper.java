package no.nav.foreldrepenger.fpmock2.testmodell.repo.impl;

import java.io.IOException;
import java.io.OutputStream;

import com.fasterxml.jackson.databind.ObjectWriter;

import no.nav.foreldrepenger.fpmock2.testmodell.repo.Testscenario;
import no.nav.foreldrepenger.fpmock2.testmodell.util.JsonMapper;

public class TestscenarioTilTemplateMapper {


    private final JsonMapper jsonMapper = new JsonMapper();
    private final ObjectWriter jsonWriter = jsonMapper.getObjectMapper().writerWithDefaultPrettyPrinter();
    
    public TestscenarioTilTemplateMapper() {
    }

    public void skrivInntektYtelse(OutputStream out, Testscenario scenario, boolean canonical) {
        try {
            if (!canonical) {
                jsonWriter.writeValue(out, scenario.getInntektYtelse());
            } else {
                jsonMapper.canonicalObjectMapper().writerWithDefaultPrettyPrinter().writeValue(out, scenario.getInntektYtelse());
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke skrive json for scenario: " + scenario, e);
        }
    }

    public void skrivPersonopplysninger(OutputStream out, Testscenario scenario, boolean canonical) {
        try {
            if (!canonical) {
                jsonWriter.writeValue(out, scenario.getPersonopplysninger());
            } else {
                jsonMapper.canonicalObjectMapper().writerWithDefaultPrettyPrinter().writeValue(out, scenario.getPersonopplysninger());
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke skrive json for scenario: " + scenario, e);
        }
    }


}
