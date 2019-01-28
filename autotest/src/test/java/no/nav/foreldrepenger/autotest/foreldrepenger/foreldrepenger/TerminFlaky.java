package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import io.qameta.allure.Description;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static no.nav.foreldrepenger.autotest.util.AllureHelper.debugLoggBehandling;
import static no.nav.foreldrepenger.autotest.util.AllureHelper.debugLoggHistorikkinnslag;

@Tag("flaky")
public class TerminFlaky extends ForeldrepengerTestBase {

    @Test
    @DisplayName("Mor søker med ett arbeidsforhold")
    @Description("Mor søkner med ett arbeidsforhold. Forventer at vedtak blir fattet og brev blir sendt")
    public void MorSøkerMedEttArbeidsforhold() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
        LocalDate termindato = LocalDate.now().plusWeeks(3);
        LocalDate startDatoForeldrepenger = termindato.minusWeeks(3);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.termindatoUttakKunMor(testscenario.getPersonopplysninger().getSøkerAktørIdent(), termindato);

        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, startDatoForeldrepenger);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);

        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.velgBehandling(saksbehandler.kodeverk.BehandlingType.getKode("Førstegangsbehandling"));
        debugLoggBehandling(saksbehandler.valgtBehandling);
        debugLoggHistorikkinnslag(saksbehandler.historikkInnslag);
        saksbehandler.ventTilHistorikkinnslag("Vedtak fattet");
        saksbehandler.ventTilHistorikkinnslag("Brev sendt");
        //TODO (OL): Byttet verifisering på historikkinnslag til asynk venting. Feilet med at behandlingen ikke hadde historikkinnslag
        //verifiser(saksbehandler.harHistorikkinnslag("Vedtak fattet"), "behandling har ikke historikkinslag 'Vedtak fattet'");
        //verifiser(saksbehandler.harHistorikkinnslag("Brev sendt"), "behandling har ikke historikkinslag 'Brev sendt'");
    }

}
