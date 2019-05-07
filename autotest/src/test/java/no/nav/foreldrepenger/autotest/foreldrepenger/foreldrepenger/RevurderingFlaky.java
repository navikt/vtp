package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import static no.nav.foreldrepenger.autotest.util.AllureHelper.debugFritekst;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.qameta.allure.Description;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.autotest.base.ForeldrepengerTestBase;
import no.nav.foreldrepenger.autotest.util.AllureHelper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;


@Tag("flaky")
@Tag("fluoritt")
public class RevurderingFlaky extends ForeldrepengerTestBase {

    @Test
    @DisplayName("Revurdering via Inntektsmelding")
    @Description("Førstegangsbehandling til positivt vedtak. Sender inn IM uten endring. Så ny IM med endring i inntekt. Vedtak fortsatt løpende.")
    public void revurderingViaInntektsmelding() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        String søkerIdent = testscenario.getPersonopplysninger().getSøkerIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);
        String arbeidsforholdId = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsforholdId();
        String orgNr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, fpStartdato);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);
        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        AllureHelper.debugLoggBehandlingsliste(saksbehandler.behandlinger);
        saksbehandler.ventTilAvsluttetBehandling();
        saksbehandler.ventTilBehandlingsstatus("AVSLU");
        debugFritekst("Ferdig med første behandling");

        // Inntektsmelding - ingen endring
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        verifiser(saksbehandler.harRevurderingBehandling(), "Saken har ikke opprettet revurdering.");
        saksbehandler.velgRevurderingBehandling();
        AllureHelper.debugLoggBehandlingsliste(saksbehandler.behandlinger);
        verifiserLikhet(saksbehandler.valgtBehandling.getBehandlingsresultat().toString(), "INGEN_ENDRING", "Behandlingsresultat");
        verifiserLikhet(saksbehandler.valgtBehandling.getBehandlingsresultat().getKonsekvenserForYtelsen().get(0).kode, "INGEN_ENDRING", "konsekvensForYtelsen");
        verifiserLikhet(saksbehandler.valgtBehandling.status.kode, "AVSLU", "Behandlingsstatus");
        debugFritekst("Ferdig med andre behandling (revurdering nr 1)");

        // Inntektsmelding - endring i inntekt
        InntektsmeldingBuilder builder = lagInntektsmeldingBuilder(50000, søkerIdent, fpStartdato, orgNr, Optional.of(arbeidsforholdId), Optional.empty(), Optional.empty());
        builder.createInntektsmelding();
        List<InntektsmeldingBuilder> inntektsmeldingerEndret = new ArrayList<>();
        inntektsmeldingerEndret.add(builder);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerEndret, søkerAktørIdent, søkerIdent, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        AllureHelper.debugLoggBehandlingsliste(saksbehandler.behandlinger);

        // TODO (MV): havner i feil aksjonspunkt... Safir må fikse i fpsak før testen kan skrives ferdig.
        // Avklar aksjonspunkt på arbeidsforhold som skal benyttes i behandlingen. Men er samme arbeidsforhold, kun diff på inntekt.
        /*
        saksbehandler.velgBehandling(saksbehandler.behandlinger.get(2));
        debugLoggBehandlingsliste(saksbehandler.behandlinger);
        saksbehandler.hentAksjonspunktbekreftelse(VurderBeregnetInntektsAvvikBekreftelse.class)
                .leggTilInntekt(480000, 1L)
                .setBegrunnelse("Endret inntekt.");
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderBeregnetInntektsAvvikBekreftelse.class);
        AllureHelper.debugLoggBehandlingsliste(saksbehandler.behandlinger);
        saksbehandler.bekreftAksjonspunktBekreftelse(KontrollerRevuderingsbehandling.class);
        saksbehandler.hentAksjonspunktbekreftelse(ForesloVedtakBekreftelse.class)
                .setBegrunnelse("Fritektst til brev.");
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Aktoer.Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);
        beslutter.velgBehandling(beslutter.behandlinger.get(2));
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.FASTSETT_BEREGNINGSGRUNNLAG_ARBEIDSTAKER_FRILANS));
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);

        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.velgBehandling(saksbehandler.behandlinger.get(2));
        verifiserLikhet(saksbehandler.valgtBehandling.behandlingsresultat.toString(), "FORELDREPENGER_ENDRET", "Behandlingsresultat");
        verifiserLikhet(saksbehandler.valgtBehandling.behandlingsresultat.getKonsekvenserForYtelsen().get(0).kode, "ENDRING_I_BEREGNING", "konsekvensForYtelsen");
        verifiserLikhet(saksbehandler.valgtBehandling.status.kode, "AVSLU", "Behandlingsstatus");
        saksbehandler.hentFagsak(saksnummer);
        verifiser(saksbehandler.valgtFagsak.hentStatus().kode.equals("LOP"), "Fagsaken er ikke løpende.");
        */
    }
}
