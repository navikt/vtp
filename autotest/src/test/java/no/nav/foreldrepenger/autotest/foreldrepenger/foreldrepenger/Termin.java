package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import static no.nav.foreldrepenger.autotest.util.AllureHelper.debugLoggBehandling;
import static no.nav.foreldrepenger.autotest.util.AllureHelper.debugLoggHistorikkinnslag;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_FELLESPERIODE;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import io.qameta.allure.Description;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.base.ForeldrepengerTestBase;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FastsettUttaksperioderManueltBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FatterVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.ForesloVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarArbeidsforholdBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaUttakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Aksjonspunkt;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak.UttakResultatPeriode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak.UttakResultatPerioder;
import no.nav.foreldrepenger.autotest.klienter.fpsak.historikk.dto.HistorikkInnslag;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.Fordeling;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.LukketPeriodeMedVedlegg;

@Execution(ExecutionMode.CONCURRENT)
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
        debugLoggHistorikkinnslag(saksbehandler.getHistorikkInnslag());
        debugLoggBehandling(saksbehandler.valgtBehandling);

        saksbehandler.ventTilHistorikkinnslag(HistorikkInnslag.VEDTAK_FATTET);
        saksbehandler.ventTilHistorikkinnslag(HistorikkInnslag.BREV_SENDT);
    }

    @Test
    @DisplayName("Mor søker sak behandlet før inntektsmelding mottatt")
    @Description("Mor søker og saken  blir behandlet før inntektsmelding er mottat basert på data fra inntektskomponenten, så mottas inntektsmeldingen ")
    public void MorSøkerMedEttArbeidsforholdInntektsmeldingPåGjennopptattSøknad() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
        LocalDate termindato = LocalDate.now().minusWeeks(1);
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


        verifiser(saksbehandler.harAksjonspunkt(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN), "Mangler aksonspunkt for vurdering av fakta arbeid frilans (5058)");

        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, startDatoForeldrepenger);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        saksbehandler.refreshBehandling();
        verifiser(!saksbehandler.harAksjonspunkt(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN), "Har uventet aksonspunkt - vurdering av fakta arbeid frilans (5058)");

        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
    }

    @Tag("pending")
    @Test
    @Disabled
    public void MorSøkerMedEttArbeidsforholdOvergangFraYtelse() throws Exception {
        //TODO
    }

    @Test
    @DisplayName("Mor søker termin med avvik i gradering")
    @Description("Mor med to arbeidsforhold søker termin. Søknad inneholder gradering. En periode som er forflyttet i fht IM, " +
            "en periode som har feil graderingsprosent i fht IM, en periode som har feil orgnr i fht IM og en periode som " +
            "er ok.")
    public void morSøkerTerminEttArbeidsforhold_avvikIGradering() throws Exception {
        TestscenarioDto testscenario = opprettScenario("77");

        String søkerAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate termindato = LocalDate.now().plusWeeks(6);
        LocalDate fpstartdato = termindato.minusWeeks(3);
        // 138 - 40%
        String orgnr1 = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        // 200 - 60%
        String orgnr2 = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(1).getArbeidsgiverOrgnr();

        Fordeling fordeling = new Fordeling();
        fordeling.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();;
        perioder.add(uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, fpstartdato, fpstartdato.plusWeeks(3).minusDays(1)));
        perioder.add(uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, termindato, termindato.plusWeeks(6).minusDays(1)));
        perioder.add(graderingsperiode(STØNADSKONTOTYPE_MØDREKVOTE, termindato.plusWeeks(6), termindato.plusWeeks(9).minusDays(1), orgnr2, BigDecimal.valueOf(40)));
        perioder.add(uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, termindato.plusWeeks(9), termindato.plusWeeks(12).minusDays(1)));
        perioder.add(graderingsperiode(STØNADSKONTOTYPE_MØDREKVOTE, termindato.plusWeeks(12), termindato.plusWeeks(15).minusDays(1), orgnr1, BigDecimal.valueOf(10)));
        perioder.add(graderingsperiode(STØNADSKONTOTYPE_FELLESPERIODE, termindato.plusWeeks(15), termindato.plusWeeks(18).minusDays(1), orgnr2, BigDecimal.valueOf(20)));
        perioder.add(graderingsperiode(STØNADSKONTOTYPE_FELLESPERIODE, termindato.plusWeeks(18), termindato.plusWeeks(21).minusDays(1), orgnr1, BigDecimal.valueOf(30)));

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.termindatoUttakKunMor(søkerAktørId, fordeling, termindato);
        Long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);

        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, fpstartdato);
        InntektsmeldingBuilder imOrg1 = finnIM(inntektsmeldinger, orgnr1);
        InntektsmeldingBuilder imOrg2 = finnIM(inntektsmeldinger, orgnr2);
        imOrg2.addGradertperiode(BigDecimal.valueOf(40), termindato.plusWeeks(7), termindato.plusWeeks(10).minusDays(1));
        imOrg1.addGradertperiode(BigDecimal.valueOf(30),termindato.plusWeeks(12), termindato.plusWeeks(15).minusDays(1));
        imOrg1.addGradertperiode(BigDecimal.valueOf(20), termindato.plusWeeks(15), termindato.plusWeeks(18).minusDays(1));
        imOrg1.addGradertperiode(BigDecimal.valueOf(30), termindato.plusWeeks(18), termindato.plusWeeks(21).minusDays(1));
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        Kode kodeVurderingstype = saksbehandler.kodeverk.UttakPeriodeVurderingType.getKode("PERIODE_KAN_IKKE_AVKLARES");
        saksbehandler.hentAksjonspunktbekreftelse(AvklarFaktaUttakBekreftelse.AvklarFaktaUttakPerioder.class)
                .godkjennPeriode(termindato.plusWeeks(6), termindato.plusWeeks(9).minusDays(1), kodeVurderingstype)
                .godkjennPeriode(termindato.plusWeeks(9), termindato.plusWeeks(12).minusDays(1), kodeVurderingstype)
                .godkjennPeriode(termindato.plusWeeks(12), termindato.plusWeeks(15).minusDays(1), kodeVurderingstype)
                .godkjennPeriode(termindato.plusWeeks(15), termindato.plusWeeks(18).minusDays(1), kodeVurderingstype);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaUttakBekreftelse.AvklarFaktaUttakPerioder.class);
        List<UttakResultatPeriode> resultatPerioder = saksbehandler.valgtBehandling.getUttakResultatPerioder().getPerioderForSøker();
        verifiser(resultatPerioder.size() == 7, "Antall perioder er ikke 7.");
        verifiser(resultatPerioder.get(0).getPeriodeResultatType().kode.equals("INNVILGET"), "Perioden er ikke automatisk innvilget.");
        verifiser(resultatPerioder.get(1).getPeriodeResultatType().kode.equals("INNVILGET"), "Perioden er ikke automatisk innvilget.");
        verifiser(resultatPerioder.get(2).getPeriodeResultatType().kode.equals("MANUELL_BEHANDLING"), "Perioden har ikke gått til manuell behandling.");
        verifiser(resultatPerioder.get(3).getPeriodeResultatType().kode.equals("MANUELL_BEHANDLING"), "Perioden har ikke gått til manuell behandling.");
        verifiser(resultatPerioder.get(4).getPeriodeResultatType().kode.equals("MANUELL_BEHANDLING"), "Perioden har ikke gått til manuell behandling.");
        verifiser(resultatPerioder.get(5).getPeriodeResultatType().kode.equals("MANUELL_BEHANDLING"), "Perioden har ikke gått til manuell behandling.");
        verifiser(resultatPerioder.get(6).getPeriodeResultatType().kode.equals("INNVILGET"), "Perioden er ikke automatisk innvilget.");
        Kode periodeResultatÅrsakInnvilget = saksbehandler.kodeverk.InnvilgetÅrsak.getKode("2003");
        Kode periodeResultatÅrsakGradFelles = saksbehandler.kodeverk.InnvilgetÅrsak.getKode("2030");
        UttakResultatPerioder uttakResultatPerioder = saksbehandler.valgtBehandling.getUttakResultatPerioder();
        saksbehandler.hentAksjonspunktbekreftelse(FastsettUttaksperioderManueltBekreftelse.class)
                .godkjennPeriodeMedGradering(uttakResultatPerioder.getPerioderForSøker().get(2), periodeResultatÅrsakInnvilget)
                .godkjennPeriodeMedGradering(uttakResultatPerioder.getPerioderForSøker().get(3), periodeResultatÅrsakInnvilget)
                .godkjennPeriodeMedGradering(uttakResultatPerioder.getPerioderForSøker().get(4), periodeResultatÅrsakInnvilget)
                .godkjennPeriodeMedGradering(uttakResultatPerioder.getPerioderForSøker().get(5), periodeResultatÅrsakGradFelles);
        saksbehandler.bekreftAksjonspunktBekreftelse(FastsettUttaksperioderManueltBekreftelse.class);
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);
        Aksjonspunkt ap1 = beslutter.hentAksjonspunkt(AksjonspunktKoder.AVKLAR_FAKTA_UTTAK);
        Aksjonspunkt ap2 = beslutter.hentAksjonspunkt(AksjonspunktKoder.FASTSETT_UTTAKPERIODER);
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkter(Arrays.asList(ap1, ap2));
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        verifiser(saksbehandler.valgtBehandling.status.kode.equals("AVSLU"), "Behandlingen har ikke status avsluttet.");


    }

    @Test
    @DisplayName("Mor søker termin uten FPFF")
    @Description("Mor søker termin uten periode for foreldrepenger før fødsel. FPFF-perioden skal opprettes og avslåes (automatisk). " +
            "Skjæringstidspunkt skal være 3 uker før termindato.")
    public void morSokerTerminUtenFPFFperiode() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");
        String søkerAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate termindato = LocalDate.now().plusWeeks(3);

        Fordeling fordeling = new Fordeling();
        fordeling.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
        perioder.add(uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, termindato, termindato.plusWeeks(15).minusDays(1)));

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.termindatoUttakKunMor(søkerAktørId, fordeling, termindato);
        Long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);

        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, termindato);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

        List<UttakResultatPeriode> resultatPerioder = saksbehandler.valgtBehandling.hentUttaksperioder();
        verifiser(resultatPerioder.size() == 3, "Det er ikke blitt opprettet riktig antall perioder.");
        verifiser(resultatPerioder.get(0).getPeriodeResultatType().kode.equals("AVSLÅTT"), "FPFF er ikke avslått.");
        verifiser(resultatPerioder.get(0).getAktiviteter().get(0).getStønadskontoType().kode.equals("FORELDREPENGER_FØR_FØDSEL"), "Feil stønadskontotype.");
        verifiser(resultatPerioder.get(1).getPeriodeResultatType().kode.equals("INNVILGET"), "Perioden søkt for skal være innvilget.");
        verifiser(resultatPerioder.get(1).getAktiviteter().get(0).getStønadskontoType().kode.equals("MØDREKVOTE"), "Feil stønadskontotype.");
        verifiser(resultatPerioder.get(2).getPeriodeResultatType().kode.equals("INNVILGET"), "Perioden søkt for skal være innvilget.");
        verifiser(resultatPerioder.get(2).getAktiviteter().get(0).getStønadskontoType().kode.equals("MØDREKVOTE"), "Feil stønadskontotype.");
        String skjaeringstidspunkt = termindato.minusWeeks(3).toString();
        verifiser(saksbehandler.valgtBehandling.behandlingsresultat.getSkjaeringstidspunktForeldrepenger().equals(skjaeringstidspunkt), "Mismatch på skjæringstidspunkt.");


    }

}
