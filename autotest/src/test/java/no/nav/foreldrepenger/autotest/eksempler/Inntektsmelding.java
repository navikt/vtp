package no.nav.foreldrepenger.autotest.eksempler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.FpsakTestBase;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;

@Tag("eksempel")
public class Inntektsmelding extends FpsakTestBase{

    @Test
    public void oppretteInntektsmeldingerBasertPåTestscenarioUtenFagsak() throws IOException {
        TestscenarioDto testscenario = opprettScenario("50");
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, LocalDate.now());
        InntektsmeldingBuilder inntektsmelding = inntektsmeldinger.get(0);
        inntektsmelding.addGradertperiode(100, InntektsmeldingBuilder.createPeriode(LocalDate.now().plusWeeks(3), LocalDate.now().plusWeeks(5)));
        
        System.out.println(inntektsmelding.createInntektesmeldingXML());
        
        fordel.erLoggetInnMedRolle("Saksbehandler");
        long saksnummer = fordel.sendInnInntektsmelding(inntektsmelding, testscenario, null);
        
        System.out.println(saksnummer);
    }


    @Test
    public void opprettInntektsmeldingEgendefinert() throws IOException {
        LocalDate ønsketDato = LocalDate.now().minusDays(2);
        TestscenarioDto testscenario = opprettScenario("50");
        InntektsmeldingBuilder inntektsmeldingBuilder = lagInntektsmeldingBuilderFraInntektsperiode(5000,
                testscenario.getPersonopplysninger().getSøkerIdent(),
                testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr(),
                LocalDate.now().minusDays(3));
        fordel.erLoggetInnMedRolle("Saksbehandler");
        long saksnummer = fordel.sendInnInntektsmelding(inntektsmeldingBuilder, testscenario, null);

        Assert.assertTrue(saksnummer > 0);

    }


}
