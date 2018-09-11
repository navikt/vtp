package no.nav.foreldrepenger.fpmock2.testmodell.repo.impl;

import java.io.IOException;
import java.io.OutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.Testscenario;

public class TestscenarioTilTemplateMapper {

    public TestscenarioTilTemplateMapper() {
    }

    public void skrivInntektYtelse(ObjectMapper objectMapper, OutputStream out, Testscenario scenario, InntektYtelseModell inntektYtelse) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(out, inntektYtelse);
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke skrive json for scenario: " + scenario, e);
        }
    }

    public void skrivPersonopplysninger(ObjectMapper objectMapper, OutputStream out, Testscenario scenario) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(out, scenario.getPersonopplysninger());
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke skrive json for scenario: " + scenario, e);
        }
    }

}
