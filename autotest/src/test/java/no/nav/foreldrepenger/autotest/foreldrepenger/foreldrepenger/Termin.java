package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import static no.nav.foreldrepenger.autotest.util.AllureHelper.debugLoggBehandling;
import static no.nav.foreldrepenger.autotest.util.AllureHelper.debugLoggHistorikkinnslag;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.qameta.allure.Description;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.base.ForeldrepengerTestBase;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.ForesloVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarArbeidsforholdBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

@Tag("fpsak")
@Tag("foreldrepenger")
public class Termin extends ForeldrepengerTestBase {

    @Test
    @DisplayName("Mor søker med ett arbeidsforhold. Inntektmelding innsendt før søknad")
    @Description("Mor med ett arbeidsforhold sender inn inntektsmelding før søknad. Forventer at vedtak bli fattet og brev blir sendt")
    public void MorSøkerMedEttArbeidsforholdInntektsmeldingFørSøknad() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
        LocalDate termindato = LocalDate.now().plusWeeks(3);
        LocalDate startDatoForeldrepenger = termindato.minusWeeks(3);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, startDatoForeldrepenger);
        Long saksnummer = fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.termindatoUttakKunMor(testscenario.getPersonopplysninger().getSøkerAktørIdent(), termindato);
        fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER, saksnummer);


        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);

        //TODO (OL): Flyttet til ventende sjekk - OK?
        saksbehandler.hentFagsak(saksnummer);
        debugLoggHistorikkinnslag(saksbehandler.historikkInnslag);
        debugLoggBehandling(saksbehandler.valgtBehandling);

        saksbehandler.ventTilHistorikkinnslag("Vedtak fattet");
        saksbehandler.ventTilHistorikkinnslag("Brev sendt");
    }

    @Test
    @DisplayName("Mor søker sak behandlet før inntektsmelding mottatt")
    @Description("Mor søker og saken  blir behandlet før inntektsmelding er mottat, så blir inntektsmeldingen mottatt")
    public void MorSøkerMedEttArbeidsforholdInntektsmeldingPåGjennopptattSøknad() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
        LocalDate termindato = LocalDate.now().plusWeeks(3);
        LocalDate startDatoForeldrepenger = termindato.minusWeeks(3);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.termindatoUttakKunMor(testscenario.getPersonopplysninger().getSøkerAktørIdent(), termindato);
        Long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        verifiser(saksbehandler.valgtBehandling.erSattPåVent(), "Behandling er ikke satt på vent etter uten inntektsmelding");

        saksbehandler.gjenopptaBehandling();


        saksbehandler.hentAksjonspunktbekreftelse(AvklarArbeidsforholdBekreftelse.class)
                .bekreftArbeidsforholdErRelevant("ACANDO AS", true);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarArbeidsforholdBekreftelse.class);


        verifiser(saksbehandler.harAksjonspunkt(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN), "Mangler aksonspunkt for vurdering av fakta arbeid frilands (5058)");

        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, startDatoForeldrepenger);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        saksbehandler.refreshBehandling();
        verifiser(!saksbehandler.harAksjonspunkt(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN), "Har uventet aksonspunkt - vurdering av fakta arbeid frilands (5058)");

        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
    }

    @Tag("pending")
    @Test
    @Disabled
    public void MorSøkerMedEttArbeidsforholdOvergangFraYtelse() throws Exception {
        //TODO
    }
}
