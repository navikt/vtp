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
        saksbehandler.velgBehandling(saksbehandler.kodeverk.BehandlingType.getKode("Førstegangsbehandling"));
        saksbehandler.ventTilØkonomioppdragFerdigstilles();
        saksbehandler.opprettBehandlingRevurdering(saksbehandler.kodeverk.BehandlingÅrsakType.getKode("RE-MDL"));

        overstyrer.erLoggetInnMedRolle(Rolle.OVERSTYRER);
        overstyrer.hentFagsak(saksnummer);
        verifiser(saksbehandler.harBehandling(hentKodeverk().BehandlingType.getKode("Revurdering")), "Saken har ikke fått revurdering.");
        overstyrer.velgBehandling(hentKodeverk().BehandlingType.getKode("Revurdering"));
        OverstyrMedlemskapsvilkaaret overstyrMedlemskapsvilkaaret = new OverstyrMedlemskapsvilkaaret(overstyrer.valgtFagsak,overstyrer.valgtBehandling);
        overstyrMedlemskapsvilkaaret.avvis(hentKodeverk().Avslagsårsak.get("FP_VK_2").getKode("Søker er ikke medlem"));
        overstyrMedlemskapsvilkaaret.setBegrunnelse("avvist");
        overstyrer.overstyr(overstyrMedlemskapsvilkaaret);
        overstyrer.bekreftAksjonspunktBekreftelse(KontrollerManueltOpprettetRevurdering.class);
        overstyrer.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        verifiserLikhet(overstyrer.valgtBehandling.behandlingsresultat.toString(), "OPPHØR", "Behandlingsresultat");

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);
        beslutter.velgBehandling(hentKodeverk().BehandlingType.getKode("Revurdering"));
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
        saksbehandler.velgBehandling(saksbehandler.kodeverk.BehandlingType.getKode("Førstegangsbehandling"));
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
        verifiser(saksbehandler.harBehandling(saksbehandler.kodeverk.BehandlingType.getKode("Revurdering")), "Det er ikke opprettet revurdering.");

        saksbehandler.velgBehandling(hentKodeverk().BehandlingType.getKode("Revurdering"));
        saksbehandler.ventTilBehandlingsstatus("AVSLU");
        verifiser(saksbehandler.valgtBehandling.behandlingsresultat.toString().equals("FORELDREPENGER_ENDRET"));
        verifiserLikhet(saksbehandler.valgtBehandling.behandlingsresultat.getKonsekvenserForYtelsen().get(0).kode, "ENDRING_I_UTTAK", "konsekvensForYtelsen");
        saksbehandler.hentFagsak(saksnummerE);
        verifiser(saksbehandler.valgtFagsak.hentStatus().kode.equals("LOP"), "Status på fagsaken er ikke løpende.");

    }

    //TODO (OL): Test satt til Disabled. Test bør bygges om til å bruke ett Scenario evt. tilpasse Autotest/VTP for å støtte hva som mangler her
    @Test
    @DisplayName("Revurdering via Inntektsmelding")
    @Description("Førstegangsbehandling til positivt vedtak. Sender inn IM uten endring. Så ny IM med endring i inntekt. Vedtak fortsatt løpende.")
    public void revurderingViaInntektsmelding() throws Exception {
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
        saksbehandler.ventTilØkonomioppdragFerdigstilles();
        saksbehandler.ventTilBehandlingsstatus("AVSLU");


        // Inntektsmelding - ingen endring
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        verifiser(saksbehandler.harBehandling(hentKodeverk().BehandlingType.getKode("Revurdering")), "Saken har ikke opprettet revurdering.");
        saksbehandler.velgBehandling(hentKodeverk().BehandlingType.getKode("Revurdering"));
        verifiserLikhet(saksbehandler.valgtBehandling.behandlingsresultat.toString(), "INGEN_ENDRING", "Behandlingsresultat");
        verifiserLikhet(saksbehandler.valgtBehandling.behandlingsresultat.getKonsekvenserForYtelsen().get(0).kode, "INGEN_ENDRING", "konsekvensForYtelsen");
        verifiserLikhet(saksbehandler.valgtBehandling.status.kode, "AVSLU", "Behandlingsstatus");

        // Inntektsmelding - endring i inntekt
        TestscenarioDto testscenarioEndret = opprettScenario("47");
        List<InntektsmeldingBuilder> inntektsmeldingerEndret = makeInntektsmeldingFromTestscenario(testscenarioEndret, fpStartdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerEndret, testscenarioEndret, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
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

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);
        beslutter.velgBehandling(beslutter.behandlinger.get(2));
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.FASTSETT_BEREGNINGSGRUNNLAG_ARBEIDSTAKER_FRILANS));
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.velgBehandling(saksbehandler.behandlinger.get(2));
        verifiserLikhet(saksbehandler.valgtBehandling.behandlingsresultat.toString(), "FORELDREPENGER_ENDRET", "Behandlingsresultat");
        verifiserLikhet(saksbehandler.valgtBehandling.behandlingsresultat.getKonsekvenserForYtelsen().get(0).kode, "ENDRING_I_BEREGNING", "konsekvensForYtelsen");
        verifiserLikhet(saksbehandler.valgtBehandling.status.kode, "AVSLU", "Behandlingsstatus");
        saksbehandler.hentFagsak(saksnummer);
        verifiser(saksbehandler.valgtFagsak.hentStatus().kode.equals("LOP"), "Fagsaken er ikke løpende.");
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
        verifiser(saksbehandler.harBehandling(hentKodeverk().BehandlingType.getKode("Revurdering")), "Revurdering er ikke opprettet.");
        saksbehandler.velgBehandling(hentKodeverk().BehandlingType.getKode("Revurdering"));
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
        saksbehandler.velgBehandling(hentKodeverk().BehandlingType.getKode("Revurdering"));
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
        beslutter.velgBehandling(hentKodeverk().BehandlingType.getKode("Revurdering"));
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.AVKLAR_FØRSTE_UTTAKSDATO));
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);
        verifiserLikhet(beslutter.valgtBehandling.behandlingsresultat.toString(), "FORELDREPENGER_ENDRET", "Behandlingsresultat");
        verifiserLikhet(beslutter.valgtBehandling.behandlingsresultat.getKonsekvenserForYtelsen().get(0).kode, "ENDRING_I_UTTAK", "konsekvensForYtelsen");

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.velgBehandling(hentKodeverk().BehandlingType.getKode("Revurdering"));
        verifiserLikhet(saksbehandler.valgtBehandling.status.kode, "AVSLU", "Behandlingsstatus");
        verifiser(saksbehandler.valgtFagsak.hentStatus().kode.equals("LOP"), "Fagsaken er ikke løpende.");


    }

    @Test
    @DisplayName("Revurdering pga berørt sak")
    @Description("Førstegangsbehandling til positivt vedtak for mor og far. Revurdering opprettes på mor pga berørt sak. Vedtak fortsatt løpende.")
    public void revurderingBerørtSak() throws Exception {
        TestscenarioDto testscenario = opprettScenario("81");
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdatoFar = fødselsdato.plusWeeks(6);

        long saksnummerMor = opprettSøknadMor(testscenario);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerMor);
        verifiserLikhet(saksbehandler.valgtFagsak.hentStatus().kode, "UBEH", "Fagsakstatus sak mor");
        long saksnummerFar = opprettSøknadFar(testscenario);
        saksbehandler.hentFagsak(saksnummerFar);
        verifiserLikhet(saksbehandler.valgtFagsak.hentStatus().kode, "UBEH", "Fagsakstatus sak far");
        verifiser(saksbehandler.valgtBehandling.behandlingPaaVent.booleanValue(), "Behandlingen er ikke på vent.");
        //Behandle ferdig mor sin sak
        saksbehandler.hentFagsak(saksnummerMor);
        saksbehandler.hentAksjonspunktbekreftelse(FastsettUttaksperioderManueltBekreftelse.class)
                .godkjennPeriode(fødselsdato.minusWeeks(3), fødselsdato.minusDays(1), 100)
                .godkjennPeriode(fødselsdato, fødselsdato.plusWeeks(6).minusDays(1), 100)
                .setBegrunnelse("Godkjent");
        saksbehandler.bekreftAksjonspunktBekreftelse(FastsettUttaksperioderManueltBekreftelse.class);
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummerMor);
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.FASTSETT_UTTAKPERIODER));
        beslutter.fattVedtakOgGodkjennØkonomioppdrag();
        verifiserLikhet(beslutter.valgtBehandling.status.kode, "AVSLU", "Behandlingsstatus");
        beslutter.refreshFagsak();
        verifiserLikhet(beslutter.valgtFagsak.hentStatus().kode, "LOP", "Fagsakstatus");
        //Behandle ferdig far sin sak
        sendInntektsmeldingFar(testscenario, saksnummerFar);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerFar);
        verifiser(saksbehandler.sakErKobletTilAnnenpart(), "Saken er ikke koblet til en annen behandling");
        saksbehandler.hentAksjonspunktbekreftelse(AvklarBrukerBosattBekreftelse.class)
                .bekreftBrukerErBosatt();
        saksbehandler.hentAksjonspunktbekreftelse(AvklarBrukerBosattBekreftelse.class)
                .setBegrunnelse("begrunnelse");
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarBrukerBosattBekreftelse.class);
        saksbehandler.hentAksjonspunktbekreftelse(FastsettUttaksperioderManueltBekreftelse.class)
                .godkjennPeriode(fpStartdatoFar, fpStartdatoFar.plusWeeks(3), 100);
        saksbehandler.bekreftAksjonspunktBekreftelse(FastsettUttaksperioderManueltBekreftelse.class);
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummerFar);
        List<Aksjonspunkt> aksjonspunkter = new ArrayList<>();
        aksjonspunkter.add(beslutter.hentAksjonspunkt(AksjonspunktKoder.AVKLAR_OM_ER_BOSATT));
        aksjonspunkter.add(beslutter.hentAksjonspunkt(AksjonspunktKoder.FASTSETT_UTTAKPERIODER));
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkter(aksjonspunkter);
        beslutter.fattVedtakOgGodkjennØkonomioppdrag();
        beslutter.ventTilBehandlingsstatus("AVSLU");
        verifiserLikhet(beslutter.valgtBehandling.status.kode, "AVSLU");
        // Sjekke at revurdering er opprettet på mor uten endring
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerMor);
        saksbehandler.ventTilSakHarBehandling("Revurdering");
        saksbehandler.velgBehandling(hentKodeverk().BehandlingType.getKode("Revurdering"));
        verifiserLikhet(saksbehandler.valgtBehandling.behandlingsresultat.toString(), "INGEN_ENDRING", "Behandlingsresultat");
        verifiserLikhet(saksbehandler.valgtBehandling.status.kode, "AVSLU", "Behandlingsstatus");

        // Sjekke at det ikke er opprettet revurdering på far
        saksbehandler.hentFagsak(saksnummerFar);
        verifiser(saksbehandler.harIkkeBehandling(hentKodeverk().BehandlingType.getKode("Revurdering")), "Behandlingen har fått opprettet revurdering.");

    }

    private long opprettSøknadMor(TestscenarioDto testscenario) throws Exception {
        String morIdent = testscenario.getPersonopplysninger().getSøkerIdent();
        String morAktørid = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        String farAktørid = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();

        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);

        Fordeling fordeling = FordelingErketyper.fordelingMorMedAksjonspunkt(fødselsdato);
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedMorMedFar(morAktørid, farAktørid, fødselsdato, LocalDate.now(), fordeling);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), morAktørid, morIdent, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);

        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenarioMedIdent(testscenario, morIdent, fpStartdato, false);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, morAktørid, morIdent, saksnummer);

        return saksnummer;

    }

    private long opprettSøknadFar(TestscenarioDto testscenario) throws Exception {
        String farIdent = testscenario.getPersonopplysninger().getAnnenpartIdent();
        String farAktørid = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        String morAktørid = testscenario.getPersonopplysninger().getSøkerAktørIdent();

        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();

        Fordeling fordeling = FordelingErketyper.fordelingFarHappyCaseMedMor(fødselsdato);
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedFarMedMor(farAktørid, morAktørid, fødselsdato, LocalDate.now(), fordeling);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), farAktørid, farIdent, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);

        return saksnummer;
    }

    private void sendInntektsmeldingFar(TestscenarioDto testscenario, long saksnummer) throws Exception {
        String farIdent = testscenario.getPersonopplysninger().getAnnenpartIdent();
        String farAktørid = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();

        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.plusWeeks(6);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenarioMedIdent(testscenario, farIdent, fpStartdato, true);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, farAktørid, farIdent, saksnummer);
    }


}
