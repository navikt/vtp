package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import static no.nav.foreldrepenger.autotest.util.AllureHelper.debugFritekst;
import static no.nav.foreldrepenger.autotest.util.AllureHelper.debugLoggBehandling;
import static no.nav.foreldrepenger.autotest.util.AllureHelper.debugLoggBehandlingsliste;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.OPPHOLDSTYPE_FEDREKVOTE_ANNEN_FORELDER;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.OPPHOLDSTYPE_KVOTE_FELLESPERIODE_ANNEN_FORELDER;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.OPPHOLDSTYPE_MØDREKVOTE_ANNEN_FORELDER;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_FEDREKVOTE;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_FELLESPERIODE;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.base.ForeldrepengerTestBase;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FastsettUttaksperioderManueltBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FatterVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.ForesloVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.KontrollerManueltOpprettetRevurdering;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.KontrollerOpplysningerOmFordelingAvStonadsperioden;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderManglendeFodselBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderSoknadsfristForeldrepengerBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarBrukerBosattBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.overstyr.OverstyrFodselsvilkaaret;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.historikk.dto.HistorikkInnslag;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.builders.UttaksperiodeBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.Fordeling;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.LukketPeriodeMedVedlegg;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.ObjectFactory;

@Execution(ExecutionMode.CONCURRENT)
@Tag("fpsak")
@Tag("foreldrepenger")
@Tag("fluoritt")
public class MorOgFarSammen extends ForeldrepengerTestBase {

    @Test
    @DisplayName("Mor og far koblet sak, kant til kant")
    @Description("Mor søker, får AP slik at behandling stopper opp. Far sender søknad og blir satt på vent. Behandler ferdig mor sin søknad (positivt vedtak)." +
            "Behandler far sin søknad (positivt vedtak). Ingen overlapp. Verifiserer at sakene er koblet og at det ikke opprettes revurdering berørt sak.")
    public void morOgFar_fødsel_ettArbeidsforholdHver_kobletsak_kantTilKant() throws Exception {
        TestscenarioDto testscenario = opprettScenario("82");
        String morIdent = testscenario.getPersonopplysninger().getSøkerIdent();
        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        String farIdent = testscenario.getPersonopplysninger().getAnnenpartIdent();
        String farAktørId = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpstartdatoMor = fødselsdato.minusWeeks(3);
        LocalDate fpstartdatoFar = fødselsdato.plusWeeks(6);

        long saksnummerMor = sendInnSøknadMorMedAksjonspunkt(testscenario, fødselsdato);
        List<InntektsmeldingBuilder> inntektsmeldingerMor = makeInntektsmeldingFromTestscenarioMedIdent(testscenario, morIdent, fpstartdatoMor, false);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerMor, morAktørId, morIdent, saksnummerMor);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerMor);
        verifiserLikhet(saksbehandler.valgtFagsak.hentStatus().kode, "UBEH", "Fagsakstatus sak mor");
        long saksnummerFar = sendInnSøknadFar(testscenario, fødselsdato, fpstartdatoFar);
        saksbehandler.hentFagsak(saksnummerFar);
        debugLoggBehandlingsliste(saksbehandler.behandlinger);
        verifiserLikhet(saksbehandler.valgtFagsak.hentStatus().kode, "UBEH", "Fagsakstatus sak far");
        verifiser(saksbehandler.valgtBehandling.erSattPåVent(), "Behandlingen er ikke på vent.");
        //Behandle ferdig mor sin sak
        saksbehandler.hentFagsak(saksnummerMor);
        debugLoggBehandlingsliste("mors behandlinger", saksbehandler.behandlinger);
        saksbehandler.hentAksjonspunktbekreftelse(FastsettUttaksperioderManueltBekreftelse.class)
                .godkjennAllePerioder();
        saksbehandler.bekreftAksjonspunktBekreftelse(FastsettUttaksperioderManueltBekreftelse.class);
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummerMor);
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.FASTSETT_UTTAKPERIODER));
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();
        verifiserLikhet(beslutter.valgtBehandling.status.kode, "AVSLU", "Behandlingsstatus");
        beslutter.refreshFagsak();
        verifiserLikhet(beslutter.valgtFagsak.hentStatus().kode, "LOP", "Fagsakstatus");
        debugFritekst("Ferdig med behandling mor");
        //Behandle ferdig far sin sak
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        List<InntektsmeldingBuilder> inntektsmeldingerFar = makeInntektsmeldingFromTestscenarioMedIdent(testscenario, farIdent, fpstartdatoFar, true);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerFar, farAktørId, farIdent, saksnummerFar);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerFar);
        verifiser(saksbehandler.sakErKobletTilAnnenpart(), "Saken er ikke koblet til en annen behandling");
        saksbehandler.ventTilBehandlingsstatus("AVSLU");
        debugFritekst("Ferdig med behandling far");
        // Verifisere at det ikke er blitt opprettet revurdering berørt sak på mor
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerMor);
        verifiser(saksbehandler.behandlinger.size() == 1, "Mor har for mange behandlinger.");

    }

    @Test
    @DisplayName("Far og mor søker fødsel med overlappende uttaksperiode")
    @Description("Mor søker og får innvilget. Far søker med to uker overlapp med mor (stjeling). Far får innvilget. " +
            "Berørt sak opprettet mor. Siste periode blir spittet i to og siste del blir avlsått. Det opprettes ikke" +
            "berørt sak på far.")
    public void farOgMor_fødsel_ettArbeidsforholdHver_overlappendePeriode() throws Exception {
        TestscenarioDto testscenario = opprettScenario("82");
        String morIdent = testscenario.getPersonopplysninger().getSøkerIdent();
        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        String farIdent = testscenario.getPersonopplysninger().getAnnenpartIdent();
        String farAktørId = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpstartdatoMor = fødselsdato.minusWeeks(3);
        LocalDate fpStartdatoFar = fødselsdato.plusWeeks(8);
        // MOR
        Fordeling fordelingMor = new Fordeling();
        fordelingMor.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> periodeMor = fordelingMor.getPerioder();
        periodeMor.add(uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, fødselsdato.minusWeeks(3), fødselsdato.minusDays(1)));
        periodeMor.add(uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato, fødselsdato.plusWeeks(10).minusDays(1)));
        ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.fodselfunnetstedMorMedFar(morAktørId, farAktørId, fødselsdato, LocalDate.now(), fordelingMor);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), morAktørId, morIdent, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerMor = makeInntektsmeldingFromTestscenarioMedIdent(testscenario, morIdent, fpstartdatoMor, false);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerMor, morAktørId, morIdent, saksnummerMor);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerMor);
        saksbehandler.ventTilBehandlingsstatus("AVSLU");
        verifiserLikhet(saksbehandler.valgtBehandling.status.kode, "AVSLU", "Mors behandling er ikke ferdigbehandlet.");
        debugFritekst("Ferdig med første behandling mor");
        saksbehandler.refreshBehandling();
        verifiser(saksbehandler.valgtBehandling.getUttakResultatPerioder().getPerioderForSøker().size() == 3, "Antall perioder for mor er ikke 3.");
        verifiser(saksbehandler.valgtBehandling.getSaldoer().getStonadskontoer().size() == 4, "Feil antall stønadskontoer.");
        // FAR
        Fordeling fordelingFar = new Fordeling();
        fordelingFar.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> periodeFar = fordelingFar.getPerioder();
        periodeFar.add(uttaksperiode(STØNADSKONTOTYPE_FEDREKVOTE, fødselsdato.plusWeeks(8), fødselsdato.plusWeeks(12)));
        ForeldrepengesoknadBuilder søknadFar = foreldrepengeSøknadErketyper.fodselfunnetstedFarMedMor(farAktørId, morAktørId, fødselsdato, LocalDate.now(), fordelingFar);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummerFar = fordel.sendInnSøknad(søknadFar.build(), farAktørId, farIdent, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerFar = makeInntektsmeldingFromTestscenarioMedIdent(testscenario, farIdent, fpStartdatoFar, true);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerFar, farAktørId, farIdent, saksnummerFar);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerFar);
        saksbehandler.ventTilBehandlingsstatus("AVSLU");
        verifiserLikhet(saksbehandler.valgtBehandling.status.kode, "AVSLU", "Fars behandling er ikke ferdigbehandlet.");
        debugFritekst("Ferdig med første behandling til far");
        saksbehandler.refreshBehandling();
        verifiser(saksbehandler.sakErKobletTilAnnenpart(), "Saken er ikke koblet til mor sin behandling");
        verifiser(saksbehandler.valgtBehandling.hentUttaksperioder().size() == 2, "Antall perioder er ikke 2.");
        verifiser(saksbehandler.valgtBehandling.getSaldoer().getStonadskontoer().size() == 4, "Feil antall stønadskontoer.");
        // Revurdering berørt sak mor
        saksbehandler.hentFagsak(saksnummerMor);
        verifiser(saksbehandler.harRevurderingBehandling() == true, "Mangler berørt behandling på mor");
        saksbehandler.ventTilSakHarRevurdering();
        saksbehandler.velgRevurderingBehandling();
        debugFritekst("Revurdering berørt sak opprettet på mor.");
        verifiser(saksbehandler.sakErKobletTilAnnenpart(), "Saken er ikke koblet til en far sin behandling");
        verifiser(saksbehandler.valgtBehandling.hentUttaksperioder().size() == 4, "Feil i splitting av mors perioder.");
        verifiser(saksbehandler.valgtBehandling.getSaldoer().getStonadskontoer().get("FEDREKVOTE").getSaldo() > 0, "Feil i stønadsdager fedrekvote.");
        verifiser(saksbehandler.valgtBehandling.getSaldoer().getStonadskontoer().get("MØDREKVOTE").getSaldo() > 0, "Feil i stønadsdager mødrekvote.");
        verifiser(saksbehandler.valgtBehandling.getSaldoer().getStonadskontoer().get("FELLESPERIODE").getSaldo() == 80, "Feil i stønadsdager fellesperiode.");
        verifiser(saksbehandler.valgtBehandling.getSaldoer().getStonadskontoer().get("FORELDREPENGER_FØR_FØDSEL").getSaldo() >= 0, "Feil i stønadsdager FPFF.");
        // verifiser ikke berørt sak far
        saksbehandler.hentFagsak(saksnummerFar);
        verifiser(saksbehandler.behandlinger.size() == 1, "Feil antall behandlinger i fagsak til far.");

    }

    @Disabled
    @Test
    @DisplayName("Mor og far koblet sak med oppholdsperiode i søknad")
    @Description("Mor og far sender inn søknader med oppholdsperiode for den andre parten. Periodene er kant til kant. " +
            "Berørt sak opprettes fordi periodene anses som overlapp. Verifiserer på like trekkdager i siste behandling hos begge.")
    public void morOgFar_berørtSak_oppholdsperioder() throws Exception {
        TestscenarioDto testscenario = opprettScenario("82");

        String morIdent = testscenario.getPersonopplysninger().getSøkerIdent();
        String farIdent = testscenario.getPersonopplysninger().getAnnenpartIdent();
        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        String farAktørId = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdatoMor = fødselsdato.minusWeeks(3);
        LocalDate fpStartdatoFar = fødselsdato.plusWeeks(6);

        // Fordeling og søknad mor
        Fordeling fordelingMor = new ObjectFactory().createFordeling();
        fordelingMor.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioderMor = fordelingMor.getPerioder();
        perioderMor.add(uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, fpStartdatoMor, fødselsdato.minusDays(1)));
        perioderMor.add(uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato, fødselsdato.plusWeeks(6).minusDays(1)));
        perioderMor.add(oppholdsperiode(OPPHOLDSTYPE_FEDREKVOTE_ANNEN_FORELDER, fødselsdato.plusWeeks(6), fødselsdato.plusWeeks(8).minusDays(1)));
        perioderMor.add(uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato.plusWeeks(8), fødselsdato.plusWeeks(12).minusDays(1)));

        ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.fodselfunnetstedMorMedFar(morAktørId,
                farAktørId, fødselsdato, LocalDate.now().minusWeeks(1), fordelingMor);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerMor = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoMor);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerMor, testscenario, saksnummerMor);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerMor);
        saksbehandler.velgFørstegangsbehandling();
        debugLoggBehandling(saksbehandler.valgtBehandling);
        saksbehandler.ventTilBehandlingsstatus("AVSLU");
        debugFritekst("Ferdig med første behandling til mor");

        // Fordeling og søknad far
        Fordeling fordelingFar = new ObjectFactory().createFordeling();
        fordelingFar.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioderFar = fordelingFar.getPerioder();
        perioderFar.add(uttaksperiode(STØNADSKONTOTYPE_FEDREKVOTE, fpStartdatoFar, fpStartdatoFar.plusWeeks(2).minusDays(1)));
        perioderFar.add(oppholdsperiode(OPPHOLDSTYPE_MØDREKVOTE_ANNEN_FORELDER, fødselsdato.plusWeeks(8), fødselsdato.plusWeeks(12).minusDays(1)));
        perioderFar.add(uttaksperiode(STØNADSKONTOTYPE_FEDREKVOTE, fødselsdato.plusWeeks(12), fødselsdato.plusWeeks(18).minusDays(1)));

        ForeldrepengesoknadBuilder søknadFar = foreldrepengeSøknadErketyper.fodselfunnetstedFarMedMor(farAktørId,
                morAktørId, fødselsdato, LocalDate.now().minusWeeks(1), fordelingFar);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummerFar = fordel.sendInnSøknad(søknadFar.build(), farAktørId, farIdent, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerFar = makeInntektsmeldingFromTestscenarioMedIdent(testscenario,
                farIdent, fpStartdatoFar, true);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerFar, farAktørId, farIdent, saksnummerFar);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerFar);
        saksbehandler.velgFørstegangsbehandling();
        debugLoggBehandling(saksbehandler.valgtBehandling);
        saksbehandler.ventTilBehandlingsstatus("AVSLU");
        debugFritekst("Ferdig med første behandling til far");

        saksbehandler.hentFagsak(saksnummerMor);
        debugLoggBehandlingsliste("Mors behandlinger",saksbehandler.behandlinger);
        saksbehandler.ventTilFagsakLøpende();
        saksbehandler.velgRevurderingBehandling();
        debugLoggBehandling(saksbehandler.valgtBehandling);
        verifiser(saksbehandler.sakErKobletTilAnnenpart(), "Mor sin sak ikke koblet til far sin sak.");
        saksbehandler.ventTilHistorikkinnslag(HistorikkInnslag.UENDRET_UTFALL);
        saksbehandler.refreshFagsak();
        verifiser(saksbehandler.behandlinger.size() == 3, "Feil antall behandlinger hos mor");
        Behandling sistebehandling = saksbehandler.behandlinger.get(2);
        saksbehandler.velgBehandling(sistebehandling);
        int morDispMødrekvote = saksbehandler.valgtBehandling.getSaldoer().getStonadskontoer().get("MØDREKVOTE").getSaldo();
        int morDispFedrekvote = saksbehandler.valgtBehandling.getSaldoer().getStonadskontoer().get("FEDREKVOTE").getSaldo();
        int morDispFellesperiode = saksbehandler.valgtBehandling.getSaldoer().getStonadskontoer().get("FELLESPERIODE").getSaldo();
        int morDispFPFF = saksbehandler.valgtBehandling.getSaldoer().getStonadskontoer().get("FORELDREPENGER_FØR_FØDSEL").getSaldo();

        saksbehandler.hentFagsak(saksnummerFar);
        verifiser(saksbehandler.behandlinger.size() == 2, "Feil antall behandlinger hos far");
        saksbehandler.velgRevurderingBehandling();
        debugLoggBehandling(saksbehandler.valgtBehandling);
        verifiser(saksbehandler.sakErKobletTilAnnenpart(), "Far sin sak ikke koblet til mor sin sak.");
        int farDispMødrekvote = saksbehandler.valgtBehandling.getSaldoer().getStonadskontoer().get("MØDREKVOTE").getSaldo();
        int farDispFedrekvote = saksbehandler.valgtBehandling.getSaldoer().getStonadskontoer().get("FEDREKVOTE").getSaldo();
        int farDispFellesperiode = saksbehandler.valgtBehandling.getSaldoer().getStonadskontoer().get("FELLESPERIODE").getSaldo();
        int farDispFPFF = saksbehandler.valgtBehandling.getSaldoer().getStonadskontoer().get("FORELDREPENGER_FØR_FØDSEL").getSaldo();

        verifiser(morDispMødrekvote == farDispMødrekvote, "Partene har forskjellig saldo for Mødrekvote");
        verifiser(morDispFedrekvote == farDispFedrekvote, "Partene har forskjellig saldo for Fedrekvote");
        verifiser(morDispFellesperiode == farDispFellesperiode, "Partene har forskjellig saldo for Fellesperiode");
        verifiser(morDispFPFF == farDispFPFF, "Partene har forskjellig saldo for Foreldrepenger før fødsel");
    }

    @Test
    @DisplayName("Koblet sak endringssøknad ingen endring")
    @Description("Sender inn søknad mor. Sender inn søknad far uten overlapp. Sender inn endringssøknad mor som er lik " +
            "førstegangsbehandlingen. Verifiserer at det ikke blir berørt sak på far.")
    public void KobletSakIngenEndring() throws Exception {
        TestscenarioDto testscenario = opprettScenario("84");
        long saksnummerMor = behandleSøknadForMorUtenOverlapp(testscenario, LocalDate.now().minusMonths(4));
        long saksnummerFar = behandleSøknadForFarUtenOverlapp(testscenario, LocalDate.now().minusMonths(4));

        saksbehandler.hentFagsak(saksnummerMor);
        verifiser(!saksbehandler.harRevurderingBehandling(), "Mor har fått revurdering uten endringssøknad eller endring i behandling");

        sendInnEndringssøknadforMor(testscenario, saksnummerMor);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerMor);

        saksbehandler.ventTilSakHarRevurdering();
        saksbehandler.velgRevurderingBehandling();

        saksbehandler.hentAksjonspunktbekreftelse(VurderSoknadsfristForeldrepengerBekreftelse.class)
            .bekreftHarGyldigGrunn(LocalDate.now().minusMonths(4));
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderSoknadsfristForeldrepengerBekreftelse.class);

        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);

        beslutter.hentFagsak(saksnummerMor);
        beslutter.velgRevurderingBehandling();
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
            .godkjennAksjonspunkt(saksbehandler.hentAksjonspunkt(AksjonspunktKoder.MANUELL_VURDERING_AV_SØKNADSFRIST_FORELDREPENGER));
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();

        saksbehandler.hentFagsak(saksnummerFar);
        verifiser(!saksbehandler.harRevurderingBehandling(), "Fars behandling fikk revurdering selv uten endringer i mors behandling av endringssøknaden");
    }


    @Test
    @DisplayName("Mor får revurdering fra endringssøknad vedtak opphører")
    @Description("Mor får revurdering fra endringssøknad vedtak opphører - far får revurdering")
    public void BerørtSakOpphør() throws Exception {
        TestscenarioDto testscenario = opprettScenario("84");
        long saksnummerMor = behandleSøknadForMorUtenOverlapp(testscenario, LocalDate.now().minusMonths(4));
        long saksnummerFar = behandleSøknadForFarUtenOverlapp(testscenario, LocalDate.now().minusMonths(4));

        sendInnEndringssøknadforMor(testscenario, saksnummerMor);

        overstyrer.erLoggetInnMedRolle(Rolle.OVERSTYRER);
        overstyrer.hentFagsak(saksnummerMor);
        overstyrer.ventTilSakHarRevurdering();
        overstyrer.velgRevurderingBehandling();

        OverstyrFodselsvilkaaret overstyr = new OverstyrFodselsvilkaaret(overstyrer.valgtFagsak, overstyrer.valgtBehandling);
        overstyr.avvis(overstyrer.kodeverk.Avslagsårsak.get("FP_VK_1").getKode("1003" /*Søker er far */));
        overstyr.setBegrunnelse("avvist");
        overstyrer.overstyr(overstyr);

        overstyrer.hentAksjonspunktbekreftelse(VurderSoknadsfristForeldrepengerBekreftelse.class)
            .bekreftHarGyldigGrunn(LocalDate.now().minusMonths(4));
        overstyrer.bekreftAksjonspunktBekreftelse(VurderSoknadsfristForeldrepengerBekreftelse.class);

        overstyrer.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummerMor);
        beslutter.velgRevurderingBehandling();

        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
            .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.MANUELL_VURDERING_AV_SØKNADSFRIST_FORELDREPENGER))
            .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.OVERSTYRING_AV_FØDSELSVILKÅRET));
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();

        saksbehandler.hentFagsak(saksnummerFar);
        verifiser(saksbehandler.harRevurderingBehandling(), "Fars behandling fikk ikke revurdering selv med opphørt vedtak i mors behandling av endringssøknaden");
    }

    @Test
    @DisplayName("Mor får revurdering fra endringssøknad endring av uttak")
    @Description("Mor får revurdering fra endringssøknad endring av uttak - fører til revurdering hos far")
    public void BerørtSakEndringAvUttak() throws Exception {
        TestscenarioDto testscenario = opprettScenario("84");
        long saksnummerMor = behandleSøknadForMorUtenOverlapp(testscenario, LocalDate.now().minusMonths(4));
        long saksnummerFar = behandleSøknadForFarUtenOverlapp(testscenario, LocalDate.now().minusMonths(4));
        sendInnEndringssøknadforMorMedEndretUttak(testscenario, saksnummerMor);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerMor);

        saksbehandler.ventTilSakHarRevurdering();
        saksbehandler.velgRevurderingBehandling();


        saksbehandler.hentAksjonspunktbekreftelse(VurderSoknadsfristForeldrepengerBekreftelse.class)
            .bekreftHarGyldigGrunn(LocalDate.now().minusMonths(4));
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderSoknadsfristForeldrepengerBekreftelse.class);


        System.out.println("Date start: " + LocalDate.now().minusMonths(4).plusWeeks(6).plusDays(1));
        System.out.println("Date start: " + LocalDate.now().minusMonths(4).plusWeeks(10).minusDays(2));

        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);

        beslutter.hentFagsak(saksnummerMor);
        beslutter.velgRevurderingBehandling();
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
            .godkjennAksjonspunkt(saksbehandler.hentAksjonspunkt(AksjonspunktKoder.MANUELL_VURDERING_AV_SØKNADSFRIST_FORELDREPENGER));
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();

        saksbehandler.hentFagsak(saksnummerFar);
        verifiser(saksbehandler.harRevurderingBehandling(), "Fars behandling fikk ikke revurdering selv uten med endringer i mors behandling av endringssøknaden");
    }

    @Test
    @DisplayName("Koblet sak mor søker etter far og sniker i køen")
    @Description("Far søker. Blir satt på vent pga for tidlig søknad. Mor søker og får innvilget. Oppretter manuell " +
            "revurdering på mor. ")
    public void KobletSakMorSøkerEtterFar() throws Exception {
        TestscenarioDto testscenario = opprettScenario("84");
        LocalDate fødselsdato = LocalDate.now().minusDays(15);
        long saksnummerFar = behandleSøknadForFarSattPåVent(testscenario, fødselsdato);
        long saksnummerMor = behandleSøknadForMorUregistrert(testscenario, fødselsdato);

        saksbehandler.hentFagsak(saksnummerMor);

        // Bruk når  revurdering fungerer
        saksbehandler.opprettBehandlingRevurdering("RE-FRDLING");
        saksbehandler.velgRevurderingBehandling();

        saksbehandler.hentAksjonspunktbekreftelse(VurderManglendeFodselBekreftelse.class)
            .bekreftDokumentasjonForeligger(1, fødselsdato);
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderManglendeFodselBekreftelse.class);

        saksbehandler.hentAksjonspunktbekreftelse(KontrollerOpplysningerOmFordelingAvStonadsperioden.class)
            .godkjennAllePerioder();
        saksbehandler.bekreftAksjonspunktBekreftelse(KontrollerOpplysningerOmFordelingAvStonadsperioden.class);

        saksbehandler.bekreftAksjonspunktbekreftelserer(
                saksbehandler.hentAksjonspunktbekreftelse(KontrollerManueltOpprettetRevurdering.class),
                saksbehandler.hentAksjonspunktbekreftelse(ForesloVedtakBekreftelse.class));

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);

        beslutter.hentFagsak(saksnummerMor);
        beslutter.velgRevurderingBehandling();

        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
            .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.KONTROLLER_OPPLYSNINGER_OM_FORDELING_AV_STØNADSPERIODEN))
            .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.SJEKK_MANGLENDE_FØDSEL));
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();
    }

    @Test
    @DisplayName("Koblet sak med flerbarnsdager og samtidig uttak")
    @Description("Mor søker med blabla. Far søker med blabla.")
    public void kobletSakMedFlerbarnsdagerOgSamtidigUttak() throws Exception {
        TestscenarioDto testscenario = opprettScenario("85");

        String morIdent = testscenario.getPersonopplysninger().getSøkerIdent();
        String morAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        String farIdent = testscenario.getPersonopplysninger().getAnnenpartIdent();
        String farAktørIdent = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        LocalDate fødsel = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpstartdatoMor = fødsel.minusWeeks(3);

        //Søknad mor
        Fordeling fordelingMor = fordelingMorSamtidigUttakFlerbarnsdager(fpstartdatoMor, fødsel);
        ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.fodselfunnetstedMorMedFar(morAktørIdent, farAktørIdent, fødsel, LocalDate.now(), fordelingMor, 2);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerMor = makeInntektsmeldingFromTestscenarioMedIdent(testscenario, morIdent, fpstartdatoMor, false);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerMor, morAktørIdent, morIdent, saksnummerMor);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerMor);
        saksbehandler.ventTilBehandlingsstatus("AVSLU");

        //Søknad far
        Fordeling fordelingFar = fordelingFarSamtidigUttakFlerbarnsdager(fødsel);
        ForeldrepengesoknadBuilder søknadFar = foreldrepengeSøknadErketyper.fodselfunnetstedFarMedMor(farAktørIdent, morAktørIdent, fødsel, LocalDate.now(), fordelingFar, 2);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummerFar = fordel.sendInnSøknad(søknadFar.build(), farAktørIdent, farIdent, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerFar = makeInntektsmeldingFromTestscenarioMedIdent(testscenario, farIdent, fødsel.plusWeeks(2), true);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerFar, farAktørIdent, farIdent, saksnummerFar);
        saksbehandler.hentFagsak(saksnummerFar);
        Kode godkjennFellesperiode = saksbehandler.kodeverk.InnvilgetÅrsak.getKode("2002");
        saksbehandler.hentAksjonspunktbekreftelse(FastsettUttaksperioderManueltBekreftelse.class)
                .godkjennPeriode(fødsel.plusWeeks(27), fødsel.plusWeeks(31).minusDays(1), godkjennFellesperiode, 100)
                .godkjennPeriode(fødsel.plusWeeks(36), fødsel.plusWeeks(38).minusDays(1), 100, godkjennFellesperiode, false, false);
        saksbehandler.bekreftAksjonspunktBekreftelse(FastsettUttaksperioderManueltBekreftelse.class);
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummerFar);
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.FASTSETT_UTTAKPERIODER));
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);

        // Behandler revurdering berørt sak mor
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerMor);
        saksbehandler.velgRevurderingBehandling();
        Kode godkjennInnvilget = saksbehandler.kodeverk.InnvilgetÅrsak.getKode("2003");
        saksbehandler.hentAksjonspunktbekreftelse(FastsettUttaksperioderManueltBekreftelse.class)
                .godkjennPeriode(fødsel.plusWeeks(2), fødsel.plusWeeks(6).minusDays(1), 100, godkjennInnvilget, true, 100)
                .godkjennPeriode(fødsel.plusWeeks(6), fødsel.plusWeeks(9).minusDays(1), 100, godkjennInnvilget, true, 100)
                .godkjennPeriode(fødsel.plusWeeks(10), fødsel.plusWeeks(11).minusDays(1), 100, godkjennFellesperiode, true, 100)
                .godkjennPeriode(fødsel.plusWeeks(23), fødsel.plusWeeks(27).minusDays(1), 100, godkjennInnvilget, true, 100)
                .godkjennPeriode(fødsel.plusWeeks(31), fødsel.plusWeeks(36).minusDays(1), 100, godkjennFellesperiode, true, 100);
        saksbehandler.bekreftAksjonspunktBekreftelse(FastsettUttaksperioderManueltBekreftelse.class);
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        beslutter.hentFagsak(saksnummerMor);
        beslutter.velgRevurderingBehandling();
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.FASTSETT_UTTAKPERIODER));
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);

        //TODO: slik fpsak fungerer nå blir det opprettet ny berørt sak på far etter at man har ryddet opp hos mor.
        /* Dette skal ikke være nødvendig. Veldig mye merarbeid for saksbehandler. Sakskomplekset er dermed ikke avsluttet. */

        //TODO: legg til valideringer før testen taes i bruk.


    }

    public Fordeling fordelingMorSamtidigUttakFlerbarnsdager(LocalDate fpstartdatoMor, LocalDate fødsel) throws Exception {
        Fordeling fordelingMor = new Fordeling();
        fordelingMor.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioderMor = fordelingMor.getPerioder();
        perioderMor.add(uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, fpstartdatoMor, fødsel.minusDays(1)));
        perioderMor.add(uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødsel, fødsel.plusWeeks(6).minusDays(1)));
        perioderMor.add(uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødsel.plusWeeks(6), fødsel.plusWeeks(9).minusDays(1)));
        perioderMor.add(uttaksperiode(STØNADSKONTOTYPE_FELLESPERIODE, fødsel.plusWeeks(9), fødsel.plusWeeks(21).minusDays(1)));
        perioderMor.add(uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødsel.plusWeeks(21), fødsel.plusWeeks(27).minusDays(1)));
        perioderMor.add(oppholdsperiode(OPPHOLDSTYPE_KVOTE_FELLESPERIODE_ANNEN_FORELDER, fødsel.plusWeeks(27), fødsel.plusWeeks(31).minusDays(1)));
        perioderMor.add(uttaksperiode(STØNADSKONTOTYPE_FELLESPERIODE, fødsel.plusWeeks(31), fødsel.plusWeeks(38).minusDays(1)));
        return fordelingMor;
    }

    public Fordeling fordelingFarSamtidigUttakFlerbarnsdager(LocalDate fødsel) throws Exception {
        Fordeling fordelingFar = new Fordeling();
        fordelingFar.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> periodeFar = fordelingFar.getPerioder();
        periodeFar.add(new UttaksperiodeBuilder()
                .medFlerbarnsdager(true)
                .medSamtidigUttak(true, BigDecimal.valueOf(100))
                .medStønadskontoType(STØNADSKONTOTYPE_FELLESPERIODE)
                .medTidsperiode(fødsel.plusWeeks(2), fødsel.plusWeeks(6).minusDays(1))
                .build());
        periodeFar.add(new UttaksperiodeBuilder()
                .medFlerbarnsdager(true)
                .medSamtidigUttak(true, BigDecimal.valueOf(100))
                .medStønadskontoType(STØNADSKONTOTYPE_FEDREKVOTE)
                .medTidsperiode(fødsel.plusWeeks(6), fødsel.plusWeeks(9).minusDays(1))
                .build());
        periodeFar.add(new UttaksperiodeBuilder()
                .medFlerbarnsdager(true)
                .medStønadskontoType(STØNADSKONTOTYPE_FEDREKVOTE)
                .medTidsperiode(fødsel.plusWeeks(9), fødsel.plusWeeks(10).minusDays(1))
                .build());
        periodeFar.add(new UttaksperiodeBuilder()
                .medSamtidigUttak(true, BigDecimal.valueOf(50))
                .medStønadskontoType(STØNADSKONTOTYPE_FEDREKVOTE)
                .medTidsperiode(fødsel.plusWeeks(10), fødsel.plusWeeks(11).minusDays(1))
                .build());
        periodeFar.add(new UttaksperiodeBuilder()
                .medFlerbarnsdager(true)
                .medSamtidigUttak(true, BigDecimal.valueOf(100))
                .medStønadskontoType(STØNADSKONTOTYPE_FEDREKVOTE)
                .medTidsperiode(fødsel.plusWeeks(23), fødsel.plusWeeks(27).minusDays(1))
                .build());
        periodeFar.add(new UttaksperiodeBuilder()
                .medStønadskontoType(STØNADSKONTOTYPE_FELLESPERIODE)
                .medTidsperiode(fødsel.plusWeeks(27), fødsel.plusWeeks(31).minusDays(1))
                .build());
        periodeFar.add(new UttaksperiodeBuilder()
                .medFlerbarnsdager(true)
                .medSamtidigUttak(true, BigDecimal.valueOf(100))
                .medStønadskontoType(STØNADSKONTOTYPE_FELLESPERIODE)
                .medTidsperiode(fødsel.plusWeeks(31), fødsel.plusWeeks(38).minusDays(1))
                .build());
        periodeFar.add(uttaksperiode(STØNADSKONTOTYPE_FEDREKVOTE, fødsel.plusWeeks(38), fødsel.plusWeeks(42).minusDays(1)));
        return fordelingFar;
    }

    @Step("Behandle søknad for mor registrert fødsel")
    public long behandleSøknadForMorRegistrertFødsel(TestscenarioDto testscenario) throws Exception {
        long saksnummer = sendInnSøknadOgInntektMor(testscenario, testscenario.getPersonopplysninger().getFødselsdato());

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilAvsluttetBehandling();

        return saksnummer;
    }

    @Step("Behandle søknad for mor uregistrert")
    private long behandleSøknadForMorUregistrert(TestscenarioDto testscenario, LocalDate fødselsdato) throws Exception {
        long saksnummer = sendInnSøknadOgInntektMor(testscenario, fødselsdato);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

        saksbehandler.hentAksjonspunktbekreftelse(VurderManglendeFodselBekreftelse.class)
            .bekreftDokumentasjonForeligger(1, fødselsdato);
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderManglendeFodselBekreftelse.class);

        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);

        beslutter.hentFagsak(saksnummer);
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
            .godkjennAksjonspunkt(saksbehandler.hentAksjonspunkt(AksjonspunktKoder.SJEKK_MANGLENDE_FØDSEL));
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();

        return saksnummer;
    }

    @Step("Behandle søknad for mor uten overlapp")
    private long behandleSøknadForMorUtenOverlapp(TestscenarioDto testscenario, LocalDate fødselsdato) throws Exception {
        long saksnummer = sendInnSøknadOgInntektMor(testscenario, fødselsdato);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

        saksbehandler.hentAksjonspunktbekreftelse(VurderManglendeFodselBekreftelse.class)
            .bekreftDokumentasjonForeligger(1, fødselsdato);
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderManglendeFodselBekreftelse.class);
        saksbehandler.hentAksjonspunktbekreftelse(VurderSoknadsfristForeldrepengerBekreftelse.class)
            .bekreftHarGyldigGrunn(LocalDate.now().minusMonths(4));
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderSoknadsfristForeldrepengerBekreftelse.class);
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);

        beslutter.hentFagsak(saksnummer);
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
            .godkjennAksjonspunkt(saksbehandler.hentAksjonspunkt(AksjonspunktKoder.MANUELL_VURDERING_AV_SØKNADSFRIST_FORELDREPENGER))
            .godkjennAksjonspunkt(saksbehandler.hentAksjonspunkt(AksjonspunktKoder.SJEKK_MANGLENDE_FØDSEL));
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();

        return saksnummer;
    }

    @Step("Behandle søknad for far uten overlapp")
    public long behandleSøknadForFarUtenOverlapp(TestscenarioDto testscenario, LocalDate fødselsdato) throws Exception {
        long saksnummer = sendInnSøknadOgInntektFar(testscenario, fødselsdato, fødselsdato.plusWeeks(10).plusDays(1));

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

        verifiser(saksbehandler.sakErKobletTilAnnenpart(), "Saken er ikke koblet til en annen behandling");

        saksbehandler.hentAksjonspunktbekreftelse(VurderManglendeFodselBekreftelse.class)
            .bekreftDokumentasjonForeligger(1, LocalDate.now().minusMonths(4));
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderManglendeFodselBekreftelse.class);

        saksbehandler.hentAksjonspunktbekreftelse(AvklarBrukerBosattBekreftelse.class)
            .bekreftBrukerErBosatt();
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarBrukerBosattBekreftelse.class);

        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);

        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
            .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.SJEKK_MANGLENDE_FØDSEL))
            .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.AVKLAR_OM_ER_BOSATT));
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();

        return saksnummer;
    }

    @Step("Send inn endringssøknad for mor")
    private void sendInnEndringssøknadforMor(TestscenarioDto testscenario, long saksnummerMor) throws Exception {
        String søkerAktørid = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        String søkerIdent = testscenario.getPersonopplysninger().getSøkerIdent();
        String annenPartAktørid = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        LocalDate fødselsdato = LocalDate.now().minusMonths(4);
        LocalDate startDatoForeldrepenger = fødselsdato.minusWeeks(3);

        Fordeling fordeling = FordelingErketyper.fordelingMorHappyCase(fødselsdato);
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedMorMedFar(
                søkerAktørid,
                annenPartAktørid,
                fødselsdato,
                startDatoForeldrepenger.plusWeeks(2),
                fordeling);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), søkerAktørid, søkerIdent, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER, saksnummerMor);
    }

    @Step("Send inn endringssøknad for mor med endret uttak")
    private void sendInnEndringssøknadforMorMedEndretUttak(TestscenarioDto testscenario, long saksnummerMor) throws Exception  {
        String søkerAktørid = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        String søkerIdent = testscenario.getPersonopplysninger().getSøkerIdent();
        String annenPartAktørid = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        LocalDate fødselsdato = LocalDate.now().minusMonths(4);
        LocalDate startDatoForeldrepenger = fødselsdato.minusWeeks(3);

        Fordeling fordeling = FordelingErketyper.fordelingMorHappyCaseLong(fødselsdato);
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedMorMedFar(
                søkerAktørid,
                annenPartAktørid,
                fødselsdato,
                startDatoForeldrepenger.plusWeeks(2),
                fordeling);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), søkerAktørid, søkerIdent, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER, saksnummerMor);
    }

    @Step("Behandle søknad for far satt på vent")
    private long behandleSøknadForFarSattPåVent(TestscenarioDto testscenario, LocalDate fødselsdato) throws Exception {
        long saksnummer = sendInnSøknadOgInntektFar(testscenario, fødselsdato, fødselsdato.plusWeeks(10).plusDays(1));

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        verifiser(saksbehandler.valgtBehandling.erSattPåVent(), "Behandling ble ikke satt på vent selv om far har søkt for tidlig");

        return saksnummer;
    }

    @Step("Send inn søknad og inntekt mor")
    private long sendInnSøknadOgInntektMor(TestscenarioDto testscenario, LocalDate fødselsdato) throws Exception {
        long saksnummer = sendInnSøknadMor(testscenario, fødselsdato);
        sendInnInntektsmeldingMor(testscenario, fødselsdato, saksnummer);
        return saksnummer;
    }
    @Step("Send inn søknad mor: fødsel funnet sted mor med far")
    private long sendInnSøknadMor(TestscenarioDto testscenario, LocalDate fødselsdato) throws Exception {
        String søkerAktørid = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        String søkerIdent = testscenario.getPersonopplysninger().getSøkerIdent();
        String annenPartAktørid = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        LocalDate startDatoForeldrepenger = fødselsdato.minusWeeks(3);

        Fordeling fordeling = FordelingErketyper.fordelingMorHappyCase(fødselsdato);
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedMorMedFar(
                søkerAktørid,
                annenPartAktørid,
                fødselsdato,
                startDatoForeldrepenger.plusWeeks(2),
                fordeling);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        return fordel.sendInnSøknad(søknad.build(), søkerAktørid, søkerIdent, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
    }
    @Step("Send inn søknad mor med aksjonspunkt")
    private long sendInnSøknadMorMedAksjonspunkt(TestscenarioDto testscenario, LocalDate fødselsdato) throws Exception {
        String søkerAktørid = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        String søkerIdent = testscenario.getPersonopplysninger().getSøkerIdent();
        String annenPartAktørid = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);

        Fordeling fordeling = new Fordeling();
        fordeling.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, fpStartdato, fødselsdato.minusDays(1)));
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedMorMedFar(
                søkerAktørid,
                annenPartAktørid,
                fødselsdato,
                LocalDate.now(),
                fordeling);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        return fordel.sendInnSøknad(søknad.build(), søkerAktørid, søkerIdent, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
    }
    @Step("Send inn inntektsmelding mor")
    private long sendInnInntektsmeldingMor(TestscenarioDto testscenario, LocalDate fødselsdato, Long saksnummer) throws Exception {
        String søkerAktørid = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        String søkerIdent = testscenario.getPersonopplysninger().getSøkerIdent();
        LocalDate startDatoForeldrepenger = fødselsdato.minusWeeks(3);

        List<Integer> inntekter = sorterteInntektsbeløp(testscenario);
        String orgnr = testscenario.getScenariodataAnnenpart().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();

        InntektsmeldingBuilder inntektsmelding = lagInntektsmeldingBuilder(
                inntekter.get(0),
                søkerIdent,
                startDatoForeldrepenger,
                orgnr,
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
        return fordel.sendInnInntektsmelding(inntektsmelding, søkerAktørid, søkerIdent, saksnummer);
    }
    @Step("Send inn søknad og inntektsmelding far")
    private long sendInnSøknadOgInntektFar(TestscenarioDto testscenario, LocalDate fødselsdato, LocalDate startDatoForeldrepenger) throws Exception {
        long saksnummer = sendInnSøknadFar(testscenario, fødselsdato, startDatoForeldrepenger);
        sendInnInntektsmeldingFar(testscenario, fødselsdato, startDatoForeldrepenger, saksnummer);
        return saksnummer;
    }
    @Step("Send inn søknad far")
    private long sendInnSøknadFar(TestscenarioDto testscenario, LocalDate fødselsdato, LocalDate startDatoForeldrepenger) throws Exception {
        String søkerAktørid = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        String søkerIdent = testscenario.getPersonopplysninger().getAnnenpartIdent();
        String annenPartAktørid = testscenario.getPersonopplysninger().getSøkerAktørIdent();

        Fordeling fordeling = fordeling(uttaksperiode(FordelingErketyper.STØNADSKONTOTYPE_FEDREKVOTE, startDatoForeldrepenger, startDatoForeldrepenger.plusWeeks(2)));
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedFarMedMor(søkerAktørid,
                annenPartAktørid,
                fødselsdato,
                fødselsdato.plusWeeks(3),
                fordeling);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        return fordel.sendInnSøknad(søknad.build(), søkerAktørid, søkerIdent, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
    }
    @Step("Send inn inntektsmelding far")
    private long sendInnInntektsmeldingFar(TestscenarioDto testscenario, LocalDate fødselsdato, LocalDate startDatoForeldrepenger, Long saksnummer)  throws Exception {
        String søkerAktørid = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        String søkerIdent = testscenario.getPersonopplysninger().getAnnenpartIdent();

        List<Integer> inntekter = sorterteInntektsbeløp(testscenario);
        String orgnr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();

        InntektsmeldingBuilder inntektsmelding = lagInntektsmeldingBuilder(
                inntekter.get(0),
                søkerIdent,
                startDatoForeldrepenger,
                orgnr,
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
        return fordel.sendInnInntektsmelding(inntektsmelding, søkerAktørid, søkerIdent, saksnummer);
    }

}
