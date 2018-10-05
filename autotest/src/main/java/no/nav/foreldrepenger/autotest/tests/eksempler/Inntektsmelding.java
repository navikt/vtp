package no.nav.foreldrepenger.autotest.tests.eksempler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.tests.FpsakTestBase;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioImpl;

public class Inntektsmelding extends FpsakTestBase{

    @Test
    public void oppretteInntektsmeldingUtenFagsak() throws IOException {
        TestscenarioImpl testscenario = opprettScenario("50");
        List<InntektsmeldingBuilder> inntektsmeldinger = inntektsmeldingErketype.makeInntektsmeldingFromTestscenario(testscenario, LocalDate.now());
        InntektsmeldingBuilder inntektsmelding = inntektsmeldinger.get(0);
        inntektsmelding.addGradertperiode();
        
        System.out.println(inntektsmelding.createInntektesmeldingXML());
        
        /*
        fordel.erLoggetInnMedRolle("Saksbehandler");
        fordel.sendInnInntektsmelding(inntektsmelding, testscenario.getPersonopplysninger().getSøker().getAktørIdent());
        */
    }
}
