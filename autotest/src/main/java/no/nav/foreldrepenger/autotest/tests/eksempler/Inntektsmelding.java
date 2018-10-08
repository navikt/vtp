package no.nav.foreldrepenger.autotest.tests.eksempler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.autotest.tests.FpsakTestBase;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.TestscenarioImpl;

public class Inntektsmelding extends FpsakTestBase{


    public void oppretteInntektsmeldingUtenFagsak() throws IOException {
        TestscenarioImpl testscenario = opprettScenario("50");
        List<InntektsmeldingBuilder> inntektsmeldinger = inntektsmeldingErketype.makeInntektsmeldingFromTestscenario(testscenario, LocalDate.now());
        InntektsmeldingBuilder inntektsmelding = inntektsmeldinger.get(0);
        inntektsmelding.addGradertperiode(100, InntektsmeldingBuilder.createPeriode(LocalDate.now().plusWeeks(3), LocalDate.now().plusWeeks(5)));
        
        System.out.println(inntektsmelding.createInntektesmeldingXML());
        
        fordel.erLoggetInnMedRolle("Saksbehandler");
        long saksnummer = fordel.sendInnInntektsmelding(inntektsmelding, testscenario);
        
        System.out.println(saksnummer);
    }
}
