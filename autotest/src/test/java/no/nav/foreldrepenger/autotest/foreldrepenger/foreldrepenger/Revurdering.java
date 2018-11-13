package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

@Tag("foreldrepenger")
public class Revurdering extends ForeldrepengerTestBase {

    @Test
    public void opprettRevurderingManuelt() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");

        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, LocalDate.now().minusMonths(1));
        InntektsmeldingBuilder inntektsmeldingsBuilder = inntektsmeldinger.get(0);
        inntektsmeldingsBuilder.addGradertperiode(100, InntektsmeldingBuilder.createPeriode(LocalDate.now().plusWeeks(3), LocalDate.now().plusWeeks(5)));
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(testscenario.getPersonopplysninger().getSøkerAktørIdent());

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        fordel.sendInnInntektsmelding(inntektsmeldingsBuilder, testscenario, saksnummer);
        System.out.println("Saksnummer: " + saksnummer);
        opprettForstegangsbehandling(saksnummer);

    }

    private void opprettForstegangsbehandling(long saksnummer) throws Exception {
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        verifiser(saksbehandler.valgtBehandling != null);
        // saken havner på vent. Sjekk datoer.
    }
}
