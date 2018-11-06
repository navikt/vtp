package no.nav.foreldrepenger.autotest.spberegning;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.BeregningKlient;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;

@Tag("spberegning")
public class VerdikjedeTest extends SpberegningTestBase {
    BeregningKlient klient;

    @Test
    public void grunnleggendeSykepenger() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");

        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, LocalDate.now());
        InntektsmeldingBuilder inntektsmeldingsBuilder = inntektsmeldinger.get(0);
        inntektsmeldingsBuilder.addGradertperiode(100, InntektsmeldingBuilder.createPeriode(LocalDate.now().plusWeeks(3), LocalDate.now().plusWeeks(5)));

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String saksnummer = fordel.opprettSak(testscenario,"SYK");
        fordel.journalførInnektsmelding(inntektsmeldingsBuilder,testscenario,Long.parseLong(saksnummer));


        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.foreslåBeregning(testscenario, saksnummer);

        verifiser(saksbehandler.beregning.getTema().kode.equals("SYK"));
    }

}
