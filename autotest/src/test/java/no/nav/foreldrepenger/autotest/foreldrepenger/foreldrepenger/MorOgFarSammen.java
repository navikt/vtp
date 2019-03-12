package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import static no.nav.foreldrepenger.autotest.util.AllureHelper.debugFritekst;
import static no.nav.foreldrepenger.autotest.util.AllureHelper.debugLoggBehandling;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.OPPHOLDSTYPE_FEDREKVOTE_ANNEN_FORELDER;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.OPPHOLDSTYPE_MØDREKVOTE_ANNEN_FORELDER;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_FEDREKVOTE;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.qameta.allure.Description;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.base.ForeldrepengerTestBase;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FastsettUttaksperioderManueltBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FatterVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.ForesloVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.Fordeling;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.LukketPeriodeMedVedlegg;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.ObjectFactory;


@Tag("fpsak")
@Tag("foreldrepenger")
public class MorOgFarSammen extends ForeldrepengerTestBase {

    @Test
    @DisplayName("Mor og far koblet sak, kant til kant")
    @Description("Mor søker, får AP slik at behandling stopper opp. Far sender søknad og blir satt på vent. Behandler ferdig mor sin søknad (positivt vedtak)." +
            "Behandler far sin søknad (positivt vedtak). Ingen overlapp. Verifiserer at sakene er koblet og at det ikke opprettes revurdering berørt sak.")
    public void morOgFar_fødsel_ettArbeidsforholdHver_kobletsak_kantTilKant() throws Exception {
        TestscenarioDto testscenario = opprettScenario("82");
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();

        long saksnummerMor = opprettSøknadMor(testscenario);
        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
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
        beslutter.erLoggetInnMedRolle(Aktoer.Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummerMor);
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.FASTSETT_UTTAKPERIODER));
        beslutter.fattVedtakOgVentTilAvsluttetSak();
        verifiserLikhet(beslutter.valgtBehandling.status.kode, "AVSLU", "Behandlingsstatus");
        beslutter.refreshFagsak();
        verifiserLikhet(beslutter.valgtFagsak.hentStatus().kode, "LOP", "Fagsakstatus");
        //Behandle ferdig far sin sak
        sendInntektsmeldingFar(testscenario, saksnummerFar);
        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerFar);
        verifiser(saksbehandler.sakErKobletTilAnnenpart(), "Saken er ikke koblet til en annen behandling");
        saksbehandler.ventTilBehandlingsstatus("AVSLU");
        // Verifisere at det ikke er blitt opprettet revurdering berørt sak på mor
        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
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
        verifiserLikhet(saksbehandler.valgtBehandling.status.kode, "AVSLU", "Mors behandling er ikke ferdigbehandlet.");
        debugFritekst("Ferdig med første behandling mor");
        verifiser(saksbehandler.valgtBehandling.uttakResultatPerioder.getPerioderForSøker().size() == 3, "Antall perioder er ikke 3.");
        verifiser(saksbehandler.valgtBehandling.saldoer.getStonadskontoer().size() == 4, "Feil antall stønadskontoer.");
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
        verifiserLikhet(saksbehandler.valgtBehandling.status.kode, "AVSLU", "Fars behandling er ikke ferdigbehandlet.");
        debugFritekst("Ferdig med første behandling til far");
        verifiser(saksbehandler.sakErKobletTilAnnenpart(), "Saken er ikke koblet til en annen behandling");
        verifiser(saksbehandler.valgtBehandling.hentUttaksperioder().size() == 2, "Antall perioder er ikke 2.");
        verifiser(saksbehandler.valgtBehandling.saldoer.getStonadskontoer().size() == 4, "Feil antall stønadskontoer.");
        // Revurdering berørt sak mor
        saksbehandler.hentFagsak(saksnummerMor);
        verifiser(saksbehandler.harBehandling("Revurdering") == true, "Mangler berørt behandling på mor");
        saksbehandler.velgBehandling("Revurdering");
        debugFritekst("Revurdering berørt sak opprettet på mor.");
        verifiser(saksbehandler.sakErKobletTilAnnenpart(), "Saken er ikke koblet til en annen behandling");
        verifiser(saksbehandler.valgtBehandling.hentUttaksperioder().size() == 4, "Feil i splitting av mors perioder.");
        verifiser(saksbehandler.valgtBehandling.saldoer.getStonadskontoer().get("FEDREKVOTE").getSaldo() > 0, "Feil i stønadsdager fedrekvote.");
        verifiser(saksbehandler.valgtBehandling.saldoer.getStonadskontoer().get("MØDREKVOTE").getSaldo() > 0, "Feil i stønadsdager mødrekvote.");
        verifiser(saksbehandler.valgtBehandling.saldoer.getStonadskontoer().get("FELLESPERIODE").getSaldo() == 80, "Feil i stønadsdager fellesperiode.");
        verifiser(saksbehandler.valgtBehandling.saldoer.getStonadskontoer().get("FORELDREPENGER_FØR_FØDSEL").getSaldo() >= 0, "Feil i stønadsdager FPFF.");
        // verifiser ikke berørt sak far
        saksbehandler.hentFagsak(saksnummerFar);
        verifiser(saksbehandler.behandlinger.size() == 1, "Feil antall behandlinger i fagsak til far.");

    }

    @Test
    @DisplayName("Mor og far koblet sak med oppholdsperiode")
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
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerMor = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoMor);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerMor, testscenario, saksnummerMor);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerMor);
        saksbehandler.velgBehandling("Førstegangsbehandling");
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
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummerFar = fordel.sendInnSøknad(søknadFar.build(), farAktørId, farIdent, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerFar = makeInntektsmeldingFromTestscenarioMedIdent(testscenario,
                farIdent, fpStartdatoFar, true);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerFar, farAktørId, farIdent, saksnummerFar);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerFar);
        saksbehandler.velgBehandling("Førstegangsbehandling");
        debugLoggBehandling(saksbehandler.valgtBehandling);
        saksbehandler.ventTilBehandlingsstatus("AVSLU");
        debugFritekst("Ferdig med første behandling til far");

        saksbehandler.hentFagsak(saksnummerMor);
        saksbehandler.ventTilFagsakstatus("LOP");
        saksbehandler.velgBehandling("Revurdering");
        debugLoggBehandling(saksbehandler.valgtBehandling);
        verifiser(saksbehandler.sakErKobletTilAnnenpart(), "Mor sin sak ikke koblet til far sin sak.");
        saksbehandler.ventTilHistorikkinnslag("Uendret utfall");
        saksbehandler.refreshFagsak();
        verifiser(saksbehandler.behandlinger.size() == 3, "Feil antall behandlinger hos mor");
        Behandling sistebehandling = saksbehandler.behandlinger.get(2);
        saksbehandler.velgBehandling(sistebehandling);
        int morDispMødrekvote = saksbehandler.valgtBehandling.saldoer.getStonadskontoer().get("MØDREKVOTE").getSaldo();
        int morDispFedrekvote = saksbehandler.valgtBehandling.saldoer.getStonadskontoer().get("FEDREKVOTE").getSaldo();
        int morDispFellesperiode = saksbehandler.valgtBehandling.saldoer.getStonadskontoer().get("FELLESPERIODE").getSaldo();
        int morDispFPFF = saksbehandler.valgtBehandling.saldoer.getStonadskontoer().get("FORELDREPENGER_FØR_FØDSEL").getSaldo();

        saksbehandler.hentFagsak(saksnummerFar);
        verifiser(saksbehandler.behandlinger.size() == 2, "Feil antall behandlinger hos far");
        saksbehandler.velgBehandling("Revurdering");
        debugLoggBehandling(saksbehandler.valgtBehandling);
        verifiser(saksbehandler.sakErKobletTilAnnenpart(), "Far sin sak ikke koblet til mor sin sak.");
        int farDispMødrekvote = saksbehandler.valgtBehandling.saldoer.getStonadskontoer().get("MØDREKVOTE").getSaldo();
        int farDispFedrekvote = saksbehandler.valgtBehandling.saldoer.getStonadskontoer().get("FEDREKVOTE").getSaldo();
        int farDispFellesperiode = saksbehandler.valgtBehandling.saldoer.getStonadskontoer().get("FELLESPERIODE").getSaldo();
        int farDispFPFF = saksbehandler.valgtBehandling.saldoer.getStonadskontoer().get("FORELDREPENGER_FØR_FØDSEL").getSaldo();

        verifiser(morDispMødrekvote == farDispMødrekvote, "Partene har forskjellig saldo for Mødrekvote");
        verifiser(morDispFedrekvote == farDispFedrekvote, "Partene har forskjellig saldo for Fedrekvote");
        verifiser(morDispFellesperiode == farDispFellesperiode, "Partene har forskjellig saldo for Fellesperiode");
        verifiser(morDispFPFF == farDispFPFF, "Partene har forskjellig saldo for Foreldrepenger før fødsel");

    }

    private long opprettSøknadMor(TestscenarioDto testscenario) throws Exception {
        String morIdent = testscenario.getPersonopplysninger().getSøkerIdent();
        String morAktørid = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        String farAktørid = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();

        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);

        Fordeling fordeling = FordelingErketyper.fordelingMorMedAksjonspunkt(fødselsdato);
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedMorMedFar(morAktørid, farAktørid, fødselsdato, LocalDate.now(), fordeling);

        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
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

        Fordeling fordeling = new Fordeling();
        fordeling.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
        perioder.add(uttaksperiode(STØNADSKONTOTYPE_FEDREKVOTE, fødselsdato.plusWeeks(6), fødselsdato.plusWeeks(9).minusDays(1)));
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedFarMedMor(farAktørid, morAktørid, fødselsdato, LocalDate.now(), fordeling);

        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), farAktørid, farIdent, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);

        return saksnummer;
    }

    private void sendInntektsmeldingFar(TestscenarioDto testscenario, long saksnummer) throws Exception {
        String farIdent = testscenario.getPersonopplysninger().getAnnenpartIdent();
        String farAktørid = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();

        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.plusWeeks(6);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenarioMedIdent(testscenario, farIdent, fpStartdato, true);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, farAktørid, farIdent, saksnummer);
    }


}
