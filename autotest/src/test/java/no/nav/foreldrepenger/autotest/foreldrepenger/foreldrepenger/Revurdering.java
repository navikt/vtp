package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import static no.nav.foreldrepenger.autotest.util.AllureHelper.debugFritekst;
import static no.nav.foreldrepenger.autotest.util.AllureHelper.debugLoggBehandling;
import static no.nav.foreldrepenger.autotest.util.AllureHelper.debugLoggBehandlingsliste;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.qameta.allure.Description;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FastsettUttaksperioderManueltBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FatterVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.ForesloVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.KontrollerManueltOpprettetRevurdering;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.KontrollerRevuderingsbehandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderBeregnetInntektsAvvikBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarBrukerBosattBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaStartdatoForForeldrepengerBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaUttakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.overstyr.OverstyrMedlemskapsvilkaaret;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Aksjonspunkt;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.autotest.util.AllureHelper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.Fordeling;


@Tag("smoke")
@Tag("foreldrepenger")
public class Revurdering extends ForeldrepengerTestBase {

    @Test
    @DisplayName("Revurdering opprettet manuelt av saksbehandler.")
    @Description("Førstegangsbehandling til positivt vedtak. Saksbehandler oppretter revurdering. Overstyrer medlemskap. Vedtaket opphører.")
    public void opprettRevurderingManuelt() throws Exception {

        TestscenarioDto testscenario = opprettScenario("50");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, fpStartdato);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.ikkeVentPåStatus = true;
        saksbehandler.hentFagsak(saksnummer);
        AllureHelper.debugLoggBehandlingsliste(saksbehandler.behandlinger);
        saksbehandler.velgBehandling("Førstegangsbehandling");
        saksbehandler.ventTilØkonomioppdragFerdigstilles();
        saksbehandler.opprettBehandlingRevurdering("RE-MDL");

        overstyrer.erLoggetInnMedRolle(Rolle.OVERSTYRER);
        overstyrer.hentFagsak(saksnummer);
        verifiser(saksbehandler.harBehandling("Revurdering"), "Saken har ikke fått revurdering.");
        overstyrer.velgBehandling("Revurdering");
        OverstyrMedlemskapsvilkaaret overstyrMedlemskapsvilkaaret = new OverstyrMedlemskapsvilkaaret(overstyrer.valgtFagsak,overstyrer.valgtBehandling);
        overstyrMedlemskapsvilkaaret.avvis(hentKodeverk().Avslagsårsak.get("FP_VK_2").getKode("Søker er ikke medlem"));
        overstyrMedlemskapsvilkaaret.setBegrunnelse("avvist");
        overstyrer.overstyr(overstyrMedlemskapsvilkaaret);
        overstyrer.bekreftAksjonspunktBekreftelse(KontrollerManueltOpprettetRevurdering.class);
        overstyrer.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        verifiserLikhet(overstyrer.valgtBehandling.behandlingsresultat.toString(), "OPPHØR", "Behandlingsresultat");

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);
        beslutter.velgBehandling("Revurdering");
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.OVERSTYRING_AV_MEDLEMSKAPSVILKÅRET));
        beslutter.fattVedtakOgGodkjennØkonomioppdrag();
        verifiserLikhet(beslutter.valgtBehandling.behandlingsresultat.toString(), "OPPHØR", "Behandlingsresultat");
        verifiserLikhet(beslutter.valgtBehandling.behandlingsresultat.getAvslagsarsak().kode, "1020", "Avslagsårsak");
        verifiserLikhet(beslutter.valgtBehandling.status.kode, "AVSLU", "Behandlingsstatus");
        beslutter.ventTilFagsakstatus("Avsluttet");
        System.out.println(beslutter.valgtFagsak.hentStatus().kode);
    }

    @Test
    @DisplayName("Endringssøknad sendes inn, revurdering opprettes.")
    @Description("Førstegangsbehandling til positivt vedtak. Søker sender inn endringsøknad. Endring i uttak. Vedtak fortsatt løpende.")
    public void endringssøknad() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");

        // Førstegangssøknad
        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        String søkerIdent = testscenario.getPersonopplysninger().getSøkerIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        Long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, fpStartdato);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.ikkeVentPåStatus = true;
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.velgBehandling("Førstegangsbehandling");
        saksbehandler.ventTilHistorikkinnslag("Vedlegg mottatt");
        debugLoggBehandling(saksbehandler.valgtBehandling);
        saksbehandler.ventTilØkonomioppdragFerdigstilles();
        verifiserLikhet(saksbehandler.behandlinger.size(), 1, "Antall behandlinger"); //revurdering opprettes ved flere arbeidsforhold for nå i autotest
        verifiserLikhet(saksbehandler.valgtBehandling.type.kode, "BT-002", "Behandlingstype");
        saksbehandler.ventTilBehandlingsstatus("AVSLU");
        debugFritekst("Ferdig med første behandling");

        // Endringssøknad
        ForeldrepengesoknadBuilder søknadE = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEndring(søkerAktørIdent, fpStartdato, saksnummer.toString());
        fordel. erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        Long saksnummerE = fordel.sendInnSøknad(søknadE.buildEndring(), søkerAktørIdent, søkerIdent, DokumenttypeId.FORELDREPENGER_ENDRING_SØKNAD, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerE);
        saksbehandler.ventTilSakHarBehandling("Revurdering");
        AllureHelper.debugLoggBehandlingsliste(saksbehandler.behandlinger);
        verifiser(saksbehandler.harBehandling("Revurdering"), "Det er ikke opprettet revurdering.");
        saksbehandler.velgBehandling("Revurdering");
        saksbehandler.ventTilBehandlingsstatus("AVSLU");
        verifiser(saksbehandler.valgtBehandling.behandlingsresultat.toString().equals("FORELDREPENGER_ENDRET"));
        verifiserLikhet(saksbehandler.valgtBehandling.behandlingsresultat.getKonsekvenserForYtelsen().get(0).kode, "ENDRING_I_UTTAK", "konsekvensForYtelsen");
        saksbehandler.hentFagsak(saksnummerE);
        verifiser(saksbehandler.valgtFagsak.hentStatus().kode.equals("LOP"), "Status på fagsaken er ikke løpende.");

    }
    
    @Test
    @DisplayName("Revurdering og ny IM når behandling er hos beslutter.")
    @Description("Førstegangsbehandling til positivt vedtak. Revurdering, og ny IM kommer når behandling er hos beslutter. Vedtak fortsatt løpende.")
    public void nyInntektsmeldingUnderÅpenRevurdering() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, fpStartdato);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        verifiserLikhet(saksbehandler.valgtBehandling.status.kode, "AVSLU", "Behandlingsstatus");

        // Sender inn ny IM
        List<InntektsmeldingBuilder> inntektsmeldingerEndret = makeInntektsmeldingFromTestscenario(testscenario, fpStartdato.plusWeeks(1));
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerEndret, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilSakHarBehandling("Revurdering");
        verifiser(saksbehandler.harBehandling("Revurdering"), "Revurdering er ikke opprettet.");
        saksbehandler.velgBehandling("Revurdering");
        saksbehandler.hentAksjonspunktbekreftelse(AvklarFaktaStartdatoForForeldrepengerBekreftelse.class)
                .setStartdatoFraSoknad(fpStartdato.plusWeeks(1))
                .setBegrunnelse("Endret startdato for fp.");
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaStartdatoForForeldrepengerBekreftelse.class);
        saksbehandler.hentAksjonspunktbekreftelse(AvklarFaktaUttakBekreftelse.AvklarFaktaUttakFørsteUttakDato.class)
                .delvisGodkjennPeriode(fpStartdato, fpStartdato.plusWeeks(3).minusDays(1), fpStartdato.plusWeeks(1), fpStartdato.plusWeeks(3).minusDays(1), hentKodeverk().UttakPeriodeVurderingType.getKode("PERIODE_OK"));
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaUttakBekreftelse.AvklarFaktaUttakFørsteUttakDato.class);
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        // Sender inn ny IM når revurdering ligger hos beslutter
        inntektsmeldingerEndret = makeInntektsmeldingFromTestscenario(testscenario, fpStartdato.plusDays(2));
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerEndret, testscenario, saksnummer);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        verifiser(saksbehandler.behandlinger.size() == 2, "Fagsaken har mer enn én revurdering.");
        saksbehandler.velgBehandling("Revurdering");
        saksbehandler.ventTilHistorikkinnslag("Behandlingen er flyttet");
        verifiser(saksbehandler.harHistorikkinnslag("Behandlingen er flyttet"), "Mangler historikkinnslag om at behandlingen er flyttet.");
        verifiser(saksbehandler.harAksjonspunkt("5045"), "Behandling hopper ikke tilbake til 'Avklar startdato for foreldrepengeperioden'.");
        saksbehandler.hentAksjonspunktbekreftelse(AvklarFaktaStartdatoForForeldrepengerBekreftelse.class)
                .setStartdatoFraSoknad(fpStartdato.plusDays(2))
                .setBegrunnelse("Endret startdato for fp.");
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaStartdatoForForeldrepengerBekreftelse.class);
        saksbehandler.hentAksjonspunktbekreftelse(AvklarFaktaUttakBekreftelse.AvklarFaktaUttakFørsteUttakDato.class)
                .delvisGodkjennPeriode(fpStartdato, fpStartdato.plusWeeks(3).minusDays(1), fpStartdato.plusDays(2), fpStartdato.plusWeeks(3).minusDays(1), hentKodeverk().UttakPeriodeVurderingType.getKode("PERIODE_OK"));
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaUttakBekreftelse.AvklarFaktaUttakFørsteUttakDato.class);
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);
        beslutter.velgBehandling("Revurdering");
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.AVKLAR_FØRSTE_UTTAKSDATO));
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);
        verifiserLikhet(beslutter.valgtBehandling.behandlingsresultat.toString(), "FORELDREPENGER_ENDRET", "Behandlingsresultat");
        verifiserLikhet(beslutter.valgtBehandling.behandlingsresultat.getKonsekvenserForYtelsen().get(0).kode, "ENDRING_I_UTTAK", "konsekvensForYtelsen");

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.velgBehandling("Revurdering");
        verifiserLikhet(saksbehandler.valgtBehandling.status.kode, "AVSLU", "Behandlingsstatus");
        verifiser(saksbehandler.valgtFagsak.hentStatus().kode.equals("LOP"), "Fagsaken er ikke løpende.");


    }
}
