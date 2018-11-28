package no.nav.foreldrepenger.autotest.foreldrepenger.eksempler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.foreldrepenger.FpsakTestBase;
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
        
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnInntektsmelding(inntektsmelding, testscenario, null);
        
        System.out.println(saksnummer);
    }


    @Test
    public void opprettInntektsmeldingEgendefinert() throws IOException {
        TestscenarioDto testscenario = opprettScenario("50");
        String orgNr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        String fnr = testscenario.getPersonopplysninger().getSøkerIdent();
        LocalDate fpStartdato = LocalDate.now().minusDays(3);
        InntektsmeldingBuilder inntektsmeldingBuilder = lagInntektsmeldingBuilder(60000, fnr, fpStartdato,
                orgNr, Optional.empty(), Optional.empty());
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnInntektsmelding(inntektsmeldingBuilder, testscenario, null);
        System.out.println(inntektsmeldingBuilder.createInntektesmeldingXML());
        Assert.assertTrue(saksnummer > 0);
    }


}
