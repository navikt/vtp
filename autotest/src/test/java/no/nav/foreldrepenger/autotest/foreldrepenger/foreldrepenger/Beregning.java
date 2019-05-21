package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import static java.time.LocalDate.now;
import static java.util.Collections.singletonList;
import static no.nav.foreldrepenger.autotest.util.AllureHelper.debugLoggBehandling;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import io.qameta.allure.Description;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.autotest.base.ForeldrepengerTestBase;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FastsatteVerdier;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FastsettMaanedsinntektUtenInntektsmeldingAndel;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FatterVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.ForesloVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.KontrollerManueltOpprettetRevurdering;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderFaktaOmBeregningBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderPerioderOpptjeningBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarArbeidsforholdBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.ArbeidstakerandelUtenIMMottarYtelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.FaktaOmBeregningTilfelle;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag.BeregningsgrunnlagArbeidsforholdDto;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag.BeregningsgrunnlagPeriodeDto;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag.BeregningsgrunnlagPrStatusOgAndelDto;
import no.nav.foreldrepenger.autotest.klienter.fpsak.historikk.dto.HistorikkInnslag;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arbeidsforhold.Arbeidsforhold;

/**
 * Tester i denne klassen vil ikkje kjøres i felles pipeline med mindre dei har Tag "fpsak"
 */
@Execution(ExecutionMode.CONCURRENT)
@Tag("beregning")
@Tag("foreldrepenger")
public class Beregning extends ForeldrepengerTestBase {

    @Test
    @DisplayName("Mor med ventelønn og vartpenger")
    @Description("Mor med ventelønn og vartpenger")
    @Tag("beregning")
    public void mor_med_ventelønn_og_vartpenger() throws Exception {
        TestscenarioDto testscenario = opprettScenario("150");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorVentelonnVartpenger(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);

        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.VURDER_PERIODER_MED_OPPTJENING);
        saksbehandler.hentAksjonspunktbekreftelse(VurderPerioderOpptjeningBekreftelse.class)
                .godkjennAllOpptjening();
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderPerioderOpptjeningBekreftelse.class);

        debugLoggBehandling(saksbehandler.valgtBehandling);
        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.AVKLAR_AKTIVITETER);
        verifiserLikhet(saksbehandler.valgtBehandling.getAksjonspunkter().stream()
                .anyMatch(ap -> ap.erUbekreftet() && ap.getDefinisjon().kode.equals(AksjonspunktKoder.AVKLAR_AKTIVITETER)), true);
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getAvklarAktiviteter().getVentelonnVartpenger().getInkludert()).isNull();
    }

    @Test
    @DisplayName("Mor med kortvarig arbeidsforhold")
    @Description("Mor med kortvarig arbeidsforhold")
    @Tag("beregning")
    public void vurder_tidsbegrenset_uten_inntektsmelding() throws Exception {
        TestscenarioDto testscenario = opprettScenario("151");

        String fnr = testscenario.getPersonopplysninger().getSøkerIdent();
        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);
        String orgNr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        int inntektPerMåned = 20_000;
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        InntektsmeldingBuilder inntektsmeldingBuilder = lagInntektsmeldingBuilder(inntektPerMåned, fnr, fpStartdato,
                orgNr, Optional.empty(), Optional.empty(), Optional.empty());
        fordel.sendInnInntektsmelding(inntektsmeldingBuilder, testscenario, saksnummer);
        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilHistorikkinnslag(HistorikkInnslag.VEDLEGG_MOTTATT);

        debugLoggBehandling(saksbehandler.valgtBehandling);

        saksbehandler.gjenopptaBehandling();
        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.VURDER_ARBEIDSFORHOLD);
        saksbehandler.hentAksjonspunktbekreftelse(AvklarArbeidsforholdBekreftelse.class)
                .bekreftArbeidsforholdErRelevant("STATOIL", true);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarArbeidsforholdBekreftelse.class);
        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN);
        verifiserLikhet(saksbehandler.valgtBehandling.getAksjonspunkter().stream()
                .anyMatch(ap -> ap.erUbekreftet() &&
                        ap.getDefinisjon().kode.equals(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN)), true);
        verifiserLikhet(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning().getFaktaOmBeregningTilfeller()
                .contains(FaktaOmBeregningTilfelle.VURDER_TIDSBEGRENSET_ARBEIDSFORHOLD), true);
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getKortvarigeArbeidsforhold().size()).isEqualTo(1);
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getKortvarigeArbeidsforhold().get(0).getAndelsnr()).isEqualTo(2L);
        assertArbeidsforhold(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getKortvarigeArbeidsforhold().get(0).getArbeidsforhold(), "STATOIL", "892850372");

        assertMottarYtelse(2L, "STATOIL", "892850372");
    }

    @Test
    @DisplayName("Mor med kortvarig arbeidsforhold med inntektsmelding")
    @Description("Mor med kortvarig arbeidsforhold med inntektsmelding")
    @Tag("beregning")
    public void vurder_tidsbegrenset_med_inntektsmelding() throws Exception {
        TestscenarioDto testscenario = opprettScenario("151");

        String fnr = testscenario.getPersonopplysninger().getSøkerIdent();
        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);
        String orgNr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        String orgNr2 = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(1).getArbeidsgiverOrgnr();
        int inntektPerMåned = 20_000;
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        InntektsmeldingBuilder inntektsmeldingBuilder = lagInntektsmeldingBuilder(inntektPerMåned, fnr, fpStartdato,
                orgNr, Optional.empty(), Optional.empty(), Optional.empty());
        InntektsmeldingBuilder inntektsmeldingBuilder2 = lagInntektsmeldingBuilder(inntektPerMåned, fnr, fpStartdato,
                orgNr2, Optional.empty(), Optional.empty(), Optional.empty());
        fordel.sendInnInntektsmelding(inntektsmeldingBuilder, testscenario, saksnummer);
        fordel.sendInnInntektsmelding(inntektsmeldingBuilder2, testscenario, saksnummer);
        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilHistorikkinnslag(HistorikkInnslag.VEDLEGG_MOTTATT);

        debugLoggBehandling(saksbehandler.valgtBehandling);
        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN);
        verifiserLikhet(saksbehandler.valgtBehandling.getAksjonspunkter().stream()
                .anyMatch(ap -> ap.erUbekreftet() &&
                        ap.getDefinisjon().kode.equals(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN)), true);
        verifiserLikhet(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning().getFaktaOmBeregningTilfeller()
                .contains(FaktaOmBeregningTilfelle.VURDER_TIDSBEGRENSET_ARBEIDSFORHOLD), true);
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getKortvarigeArbeidsforhold().size()).isEqualTo(1);
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getKortvarigeArbeidsforhold().get(0).getAndelsnr()).isEqualTo(2L);
        assertArbeidsforhold(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getKortvarigeArbeidsforhold().get(0).getArbeidsforhold(), "STATOIL", "892850372");
    }

    @Test
    @DisplayName("Tilkommer nytt arbeidsforhold med refusjonskrav på STP")
    @Description("Tilkommer nytt arbeidsforhold med refusjonskrav på STP")
    @Tag("beregning")
    public void morSøkerFødselMedToArbeidsforholdDerDetEneTilkommerPåSTP() throws Exception {
        TestscenarioDto testscenario = opprettScenario("161");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);

        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);


        String fnr = testscenario.getPersonopplysninger().getSøkerIdent();

        List<InntektsmeldingBuilder> inntektsmeldinger = List.of(
                lagInntektsmeldingBuilder(20000, fnr, fpStartdato, "910909088", Optional.of("ARB001-001"), Optional.empty(), Optional.empty()),
                lagInntektsmeldingBuilderMedGradering(10000, fnr, fpStartdato, "973861778", Optional.of("ARB001-002"), Optional.of(BigDecimal.valueOf(1000)), 50, fpStartdato, fpStartdato.plusWeeks(3)));

        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN);
    }

    @Test
    @DisplayName("Endret beregningsgrunnlag med kortvarig")
    @Description("Endret beregningsgrunnlag med kortvarig")
    @Tag("beregning")
    public void mor_søker_fødsel_med_to_arbeidsforhold_i_samme_organisasjon_inntektsmelding_for_en_med_id_velger_og_sette_det_andre_til_inaktivt() throws Exception {

        TestscenarioDto testscenario = opprettScenario("57");

        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);
        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);

        String fnr = testscenario.getPersonopplysninger().getSøkerIdent();
        List<Integer> inntekter = sorterteInntektsbeløp(testscenario);
        String orgnr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        InntektsmeldingBuilder inntektsmelding = lagInntektsmeldingBuilder(inntekter.get(0), fnr,
                fpStartdato, orgnr, Optional.of("ARB001-001"), Optional.empty(), Optional.empty());

        fordel.sendInnInntektsmelding(inntektsmelding, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

        saksbehandler.gjenopptaBehandling();

        saksbehandler.hentAksjonspunktbekreftelse(AvklarArbeidsforholdBekreftelse.class)
                .bekreftArbeidsforholdErOverstyrt("BEDRIFT AS", LocalDate.now().minusYears(3), fpStartdato.minusDays(10));

        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarArbeidsforholdBekreftelse.class);

        saksbehandler.hentFagsak(saksnummer);

        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.FASTSETT_BEREGNINGSGRUNNLAG_ARBEIDSTAKER_FRILANS);
    }

    @Test
    @DisplayName("Endret beregningsgrunnlag med kortvarig")
    @Description("Endret beregningsgrunnlag med kortvarig")
    @Tag("beregning")
    public void endret_beregningsgrunnlag_med_kortvarig() throws Exception {
        TestscenarioDto testscenario = opprettScenario("151");

        String fnr = testscenario.getPersonopplysninger().getSøkerIdent();
        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);
        String orgNr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        String orgNr2 = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(1).getArbeidsgiverOrgnr();
        int inntektPerMåned = 20_000;
        BigDecimal refusjon = BigDecimal.valueOf(50_000);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        InntektsmeldingBuilder inntektsmeldingBuilder = lagInntektsmeldingBuilder(inntektPerMåned, fnr, fpStartdato,
                orgNr, Optional.empty(), Optional.of(refusjon), Optional.empty());
        InntektsmeldingBuilder inntektsmeldingBuilder2 = lagInntektsmeldingBuilderMedGradering(inntektPerMåned, fnr, fpStartdato,
                orgNr2, Optional.empty(), Optional.empty(), 50, fpStartdato, fpStartdato.plusMonths(2));
        fordel.sendInnInntektsmelding(inntektsmeldingBuilder, testscenario, saksnummer);
        fordel.sendInnInntektsmelding(inntektsmeldingBuilder2, testscenario, saksnummer);
        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilHistorikkinnslag(HistorikkInnslag.VEDLEGG_MOTTATT);

        debugLoggBehandling(saksbehandler.valgtBehandling);
        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN);
        verifiserLikhet(saksbehandler.valgtBehandling.getAksjonspunkter().stream()
                .anyMatch(ap -> ap.erUbekreftet() &&
                        ap.getDefinisjon().kode.equals(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN)), true);
        verifiserLikhet(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning().getFaktaOmBeregningTilfeller()
                .contains(FaktaOmBeregningTilfelle.VURDER_TIDSBEGRENSET_ARBEIDSFORHOLD), true);
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getKortvarigeArbeidsforhold().size()).isEqualTo(1);
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getKortvarigeArbeidsforhold().get(0).getAndelsnr()).isEqualTo(2L);
        assertArbeidsforhold(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getKortvarigeArbeidsforhold().get(0).getArbeidsforhold(), "STATOIL", "892850372");


        verifiserLikhet(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning().getFaktaOmBeregningTilfeller()
                .contains(FaktaOmBeregningTilfelle.FASTSETT_ENDRET_BEREGNINGSGRUNNLAG), true);
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getEndringBeregningsgrunnlag().getEndredeArbeidsforhold().size()).isEqualTo(1);
        assertArbeidsforhold(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getEndringBeregningsgrunnlag().getEndredeArbeidsforhold().get(0), "STATOIL", "892850372");
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getEndringBeregningsgrunnlag().getEndredeArbeidsforhold().get(0).getPerioderMedGraderingEllerRefusjon().size()).isEqualTo(1);
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getEndringBeregningsgrunnlag().getEndredeArbeidsforhold().get(0)
                .getPerioderMedGraderingEllerRefusjon().get(0).getFom()).isEqualTo(fpStartdato);
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getEndringBeregningsgrunnlag().getEndredeArbeidsforhold().get(0)
                .getPerioderMedGraderingEllerRefusjon().get(0).getTom()).isEqualTo(fpStartdato.plusMonths(2));

        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().antallBeregningsgrunnlagPeriodeDto()).isEqualTo(1);
    }

    @Test
    @DisplayName("Vurder besteberegning: Mor med arbeidsforhold og dagpenger i opptjeningsperioden")
    @Description("Vurder besteberegning: Mor med arbeidsforhold og dagpenger i opptjeningsperioden.")
    public void vurder_besteberegning() throws Exception {
        TestscenarioDto testscenario = opprettScenario("156");

        String fnr = testscenario.getPersonopplysninger().getSøkerIdent();
        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);
        String orgNr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        int inntektPerMåned = 30_000;
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        InntektsmeldingBuilder inntektsmeldingBuilder = lagInntektsmeldingBuilder(inntektPerMåned, fnr, fpStartdato,
                orgNr, Optional.empty(), Optional.empty(), Optional.empty());
        fordel.sendInnInntektsmelding(inntektsmeldingBuilder, testscenario, saksnummer);
        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilHistorikkinnslag(HistorikkInnslag.VEDLEGG_MOTTATT);

        debugLoggBehandling(saksbehandler.valgtBehandling);
        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN);
        verifiserLikhet(saksbehandler.valgtBehandling.getAksjonspunkter().stream()
                .anyMatch(ap -> ap.erUbekreftet() &&
                        ap.getDefinisjon().kode.equals(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN)), true);
        verifiserLikhet(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning().getFaktaOmBeregningTilfeller()
                .contains(FaktaOmBeregningTilfelle.VURDER_BESTEBEREGNING), true);
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getVurderBesteberegning().getSkalHaBesteberegning()).isNull();
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getVurderBesteberegning().getAndeler().size()).isEqualTo(1);
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getVurderBesteberegning().getAndeler().get(0).getAndelsnr()).isEqualTo(1);
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getVurderBesteberegning().getAndeler().get(0).getFastsattBelopPrMnd()).isNull();
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getVurderBesteberegning().getAndeler().get(0).getAktivitetStatus().kode).isEqualTo("AT");
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getVurderBesteberegning().getAndeler().get(0).getInntektskategori().kode).isEqualTo("ARBEIDSTAKER");
        assertArbeidsforhold(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getVurderBesteberegning().getAndeler().get(0).getArbeidsforhold(), "BEDRIFT AS", "910909088");
    }


    @Test
    @DisplayName("Vurder besteberegning: Mor med arbeidsforhold og dagpenger i opptjeningsperioden. Uten inntektsmelding.")
    @Description("Vurder besteberegning: Mor med arbeidsforhold og dagpenger i opptjeningsperioden. Uten inntektsmelding")
    public void vurder_besteberegning_vurder_mottar_ytelse() throws Exception {
        TestscenarioDto testscenario = opprettScenario("156");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

        debugLoggBehandling(saksbehandler.valgtBehandling);
        saksbehandler.gjenopptaBehandling();
        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.VURDER_ARBEIDSFORHOLD);
        saksbehandler.hentAksjonspunktbekreftelse(AvklarArbeidsforholdBekreftelse.class)
                .bekreftArbeidsforholdErRelevant("BEDRIFT AS", true);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarArbeidsforholdBekreftelse.class);
        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN);
        verifiserLikhet(saksbehandler.valgtBehandling.getAksjonspunkter().stream()
                .anyMatch(ap -> ap.erUbekreftet() &&
                        ap.getDefinisjon().kode.equals(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN)), true);
        verifiserLikhet(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning().getFaktaOmBeregningTilfeller()
                .contains(FaktaOmBeregningTilfelle.VURDER_BESTEBEREGNING), true);
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getVurderBesteberegning().getSkalHaBesteberegning()).isNull();
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getVurderBesteberegning().getAndeler().size()).isEqualTo(1);
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getVurderBesteberegning().getAndeler().get(0).getAndelsnr()).isEqualTo(1);
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getVurderBesteberegning().getAndeler().get(0).getFastsattBelopPrMnd()).isNull();
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getVurderBesteberegning().getAndeler().get(0).getAktivitetStatus().kode).isEqualTo("AT");
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getVurderBesteberegning().getAndeler().get(0).getInntektskategori().kode).isEqualTo("ARBEIDSTAKER");
        assertArbeidsforhold(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getVurderBesteberegning().getAndeler().get(0).getArbeidsforhold(), "BEDRIFT AS", "910909088");
    }

    @Test
    @DisplayName("Vurder besteberegning: Mor med arbeidsforhold og dagpenger i opptjeningsperioden. Uten inntektsmelding, med lønnsendring")
    @Description("Vurder besteberegning: Mor med arbeidsforhold og dagpenger i opptjeningsperioden. Uten inntektsmelding, med lønnsendring")
    public void vurder_besteberegning_vurder_mottar_ytelse_vurder_lonnsendring() throws Exception {
        TestscenarioDto testscenario = opprettScenario("159");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

        debugLoggBehandling(saksbehandler.valgtBehandling);
        saksbehandler.gjenopptaBehandling();
        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.VURDER_ARBEIDSFORHOLD);
        saksbehandler.hentAksjonspunktbekreftelse(AvklarArbeidsforholdBekreftelse.class)
                .bekreftArbeidsforholdErRelevant("BEDRIFT AS", true);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarArbeidsforholdBekreftelse.class);
        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN);
        verifiserLikhet(saksbehandler.valgtBehandling.getAksjonspunkter().stream()
                .anyMatch(ap -> ap.erUbekreftet() &&
                        ap.getDefinisjon().kode.equals(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN)), true);
        verifiserLikhet(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning().getFaktaOmBeregningTilfeller()
                .contains(FaktaOmBeregningTilfelle.VURDER_BESTEBEREGNING), true);
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getVurderBesteberegning().getSkalHaBesteberegning()).isNull();
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getVurderBesteberegning().getAndeler().size()).isEqualTo(1);
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getVurderBesteberegning().getAndeler().get(0).getAndelsnr()).isEqualTo(1);
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getVurderBesteberegning().getAndeler().get(0).getFastsattBelopPrMnd()).isNull();
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getVurderBesteberegning().getAndeler().get(0).getAktivitetStatus().kode).isEqualTo("AT");
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getVurderBesteberegning().getAndeler().get(0).getInntektskategori().kode).isEqualTo("ARBEIDSTAKER");
        assertArbeidsforhold(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getVurderBesteberegning().getAndeler().get(0).getArbeidsforhold(), "BEDRIFT AS", "910909088");
    }

    @Test
    @DisplayName("Uten inntektsmelding, med lønnsendring")
    @Description("Uten inntektsmelding, med lønnsendring")
    public void vurder_mottar_ytelse_vurder_lonnsendring() throws Exception {
        TestscenarioDto testscenario = opprettScenario("161");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

        debugLoggBehandling(saksbehandler.valgtBehandling);
        saksbehandler.gjenopptaBehandling();
        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.VURDER_ARBEIDSFORHOLD);
        saksbehandler.hentAksjonspunktbekreftelse(AvklarArbeidsforholdBekreftelse.class)
                .bekreftArbeidsforholdErRelevant("BEDRIFT AS", true);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarArbeidsforholdBekreftelse.class);
        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN);
        verifiserLikhet(saksbehandler.valgtBehandling.getAksjonspunkter().stream()
                .anyMatch(ap -> ap.erUbekreftet() &&
                        ap.getDefinisjon().kode.equals(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN)), true);
    }

    @Test
    @DisplayName("ATFL i samme org med lønnsendring")
    @Description("ATFL i samme org med lønnsendring")
    public void ATFL_samme_org_med_lønnendring_uten_inntektsmelding() throws Exception {
        TestscenarioDto testscenario = opprettScenario("163");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorMedFrilans(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

        debugLoggBehandling(saksbehandler.valgtBehandling);
        saksbehandler.gjenopptaBehandling();
        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.VURDER_ARBEIDSFORHOLD);
        saksbehandler.hentAksjonspunktbekreftelse(AvklarArbeidsforholdBekreftelse.class)
                .bekreftArbeidsforholdErRelevant("BEDRIFT AS", true);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarArbeidsforholdBekreftelse.class);

        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN);
        verifiserLikhet(saksbehandler.valgtBehandling.getAksjonspunkter().stream()
                .anyMatch(ap -> ap.erUbekreftet() &&
                        ap.getDefinisjon().kode.equals(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN)), true);
    }

    @Test
    @DisplayName("Vurder besteberegning: Mor med arbeidsforhold og dagpenger på skjæringstidspunktet")
    @Description("Vurder besteberegning: Mor med arbeidsforhold og dagpenger på skjæringstidspunktet.")
    public void vurder_besteberegning_dagpenger_på_skjæringstidspunktet() throws Exception {
        TestscenarioDto testscenario = opprettScenario("158");

        String fnr = testscenario.getPersonopplysninger().getSøkerIdent();
        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);
        String orgNr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        int inntektPerMåned = 30_000;
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        InntektsmeldingBuilder inntektsmeldingBuilder = lagInntektsmeldingBuilder(inntektPerMåned, fnr, fpStartdato,
                orgNr, Optional.empty(), Optional.empty(), Optional.empty());
        fordel.sendInnInntektsmelding(inntektsmeldingBuilder, testscenario, saksnummer);
        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilHistorikkinnslag(HistorikkInnslag.VEDLEGG_MOTTATT);

        debugLoggBehandling(saksbehandler.valgtBehandling);
        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN);
        verifiserLikhet(saksbehandler.valgtBehandling.getAksjonspunkter().stream()
                .anyMatch(ap -> ap.erUbekreftet() &&
                        ap.getDefinisjon().kode.equals(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN)), true);
        verifiserLikhet(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning().getFaktaOmBeregningTilfeller()
                .contains(FaktaOmBeregningTilfelle.VURDER_BESTEBEREGNING), true);
    }


    @Test
    @DisplayName("Mor med arbeidsforhold uten inntektsmelding som mottar ytelse")
    @Description("Mor med arbeidsforhold uten inntektsmelding som mottar ytelse. Produksjonshendelse som feilet i frontend.")
    @Tag("beregning")
    public void vurder_mottar_ytelse() throws Exception {
        TestscenarioDto testscenario = opprettScenario("155");

        String fnr = testscenario.getPersonopplysninger().getSøkerIdent();
        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate termindato = now().minusMonths(1).plusWeeks(3);
        LocalDate startDatoForeldrepenger = termindato.minusWeeks(3);
        String orgNr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        int inntektPerMåned = 30_000;
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.termindatoUttakKunMor(søkerAktørIdent, termindato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);

        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

        debugLoggBehandling(saksbehandler.valgtBehandling);

        saksbehandler.gjenopptaBehandling();
        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.VURDER_ARBEIDSFORHOLD);
        saksbehandler.hentAksjonspunktbekreftelse(AvklarArbeidsforholdBekreftelse.class)
                .bekreftArbeidsforholdErRelevant("BEDRIFT AS", true);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarArbeidsforholdBekreftelse.class);
        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN);
        verifiserLikhet(saksbehandler.valgtBehandling.getAksjonspunkter().stream()
                .anyMatch(ap -> ap.erUbekreftet() &&
                        ap.getDefinisjon().kode.equals(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN)), true);
        assertMottarYtelse(1L, "BEDRIFT AS", "910909088");

        saksbehandler.hentAksjonspunktbekreftelse(VurderFaktaOmBeregningBekreftelse.class)
                .leggTilFaktaOmBeregningTilfeller(FaktaOmBeregningTilfelle.VURDER_MOTTAR_YTELSE.kode)
                .leggTilFaktaOmBeregningTilfeller(FaktaOmBeregningTilfelle.FASTSETT_MAANEDSLONN_ARBEIDSTAKER_UTEN_INNTEKTSMELDING.kode)
                .leggTilMottarYtelse(false, singletonList(new ArbeidstakerandelUtenIMMottarYtelse(1, true)))
                .leggTilMaanedsinntektUtenInntektsmelding(singletonList(new FastsettMaanedsinntektUtenInntektsmeldingAndel(1, 30000)));
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderFaktaOmBeregningBekreftelse.class);

        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Aktoer.Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);

        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN));
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();

        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);

        saksbehandler.opprettBehandlingRevurdering("RE-HENDELSE-FØDSEL");

        saksbehandler.velgRevurderingBehandling();

        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN);

        assertMottarYtelse(1L, "BEDRIFT AS", "910909088");

        saksbehandler.settBehandlingPåVent(now().plusWeeks(1), "VENT_OPDT_INNTEKTSMELDING");

        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.AUTO_MANUELT_SATT_PÅ_VENT);

        InntektsmeldingBuilder inntektsmeldingBuilder = lagInntektsmeldingBuilder(inntektPerMåned, fnr, startDatoForeldrepenger,
        orgNr, Optional.empty(), Optional.empty(), Optional.empty());
        inntektsmeldingBuilder.addUtsettelseperiode(FordelingErketyper.UTSETTELSETYPE_ARBEID, startDatoForeldrepenger, startDatoForeldrepenger.plusMonths(1));
        fordel.sendInnInntektsmelding(inntektsmeldingBuilder, testscenario, saksnummer);

        // Når testen skrives er det riktig at behandling fortsatt skal stå på vent
        assertThat(saksbehandler.valgtBehandling.erSattPåVent()).isTrue();

        InntektsmeldingBuilder inntektsmeldingBuilder2 = lagInntektsmeldingBuilder(inntektPerMåned, fnr, startDatoForeldrepenger,
                orgNr, Optional.empty(), Optional.empty(), Optional.empty());
        fordel.sendInnInntektsmelding(inntektsmeldingBuilder2, testscenario, saksnummer);

        saksbehandler.gjenopptaBehandling();

        assertThat(saksbehandler.valgtBehandling.erSattPåVent()).isFalse();

        saksbehandler.bekreftAksjonspunktBekreftelse(KontrollerManueltOpprettetRevurdering.class);

        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.FORESLÅ_VEDTAK_MANUELT);

        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()).isNull();
    }


    @Test
    @DisplayName("Arbeidsforhold tilkommer etter stp")
    @Description("Arbeidsforhold tilkommer etter stp")
    @Tag("beregning")
    public void arbeidsforhold_tilkommer_etter_stp() throws Exception {
        TestscenarioDto testscenario = opprettScenario("157");

        String fnr = testscenario.getPersonopplysninger().getSøkerIdent();
        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);
        List<Arbeidsforhold> arbeidsforhold = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold();
        Arbeidsforhold tilkommet = arbeidsforhold.get(1);
        LocalDate tilkommetDato = tilkommet.getAnsettelsesperiodeFom();
        String orgNr = tilkommet.getArbeidsgiverOrgnr();
        String orgNr2 = arbeidsforhold.get(2).getArbeidsgiverOrgnr();
        int inntektPerMåned = 30_000;
        BigDecimal refusjon = BigDecimal.valueOf(10_000);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        InntektsmeldingBuilder inntektsmeldingBuilder = lagInntektsmeldingBuilder(inntektPerMåned, fnr, fpStartdato,
                orgNr, Optional.empty(), Optional.of(refusjon), Optional.empty());
        InntektsmeldingBuilder inntektsmeldingBuilder2 = lagInntektsmeldingBuilder(inntektPerMåned, fnr, fpStartdato,
                orgNr2, Optional.empty(), Optional.empty(), Optional.empty());
        fordel.sendInnInntektsmelding(inntektsmeldingBuilder, testscenario, saksnummer);
        fordel.sendInnInntektsmelding(inntektsmeldingBuilder2, testscenario, saksnummer);
        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilHistorikkinnslag(HistorikkInnslag.VEDLEGG_MOTTATT);

        debugLoggBehandling(saksbehandler.valgtBehandling);
        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN);
        verifiserLikhet(saksbehandler.valgtBehandling.getAksjonspunkter().stream()
                .anyMatch(ap -> ap.erUbekreftet() &&
                        ap.getDefinisjon().kode.equals(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN)), true);


        assertEndretArbeidsforhold(tilkommetDato);

        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().antallBeregningsgrunnlagPeriodeDto()).isEqualTo(2);

        BeregningsgrunnlagPeriodeDto manuellPeriode = saksbehandler.valgtBehandling.getBeregningsgrunnlag().getBeregningsgrunnlagPeriode(1);
        BeregningsgrunnlagPrStatusOgAndelDto tilkommetAndel = manuellPeriode.getBeregningsgrunnlagPrStatusOgAndel().stream()
                .filter(andel -> andel.getArbeidsforhold().getArbeidsgiverId().equals(orgNr)).findFirst().get();
        BeregningsgrunnlagPrStatusOgAndelDto eksisterendeAndel = manuellPeriode.getBeregningsgrunnlagPrStatusOgAndel().stream()
                .filter(andel -> andel.getArbeidsforhold().getArbeidsgiverId().equals(orgNr2)).findFirst().get();
        VurderFaktaOmBeregningBekreftelse bekreftelse = saksbehandler.hentAksjonspunktbekreftelse(VurderFaktaOmBeregningBekreftelse.class);
        int fastsattBeløp = 15_000;
        FastsatteVerdier fastsatteVerdier = new FastsatteVerdier(10_000, fastsattBeløp,
                saksbehandler.kodeverk.Inntektskategori.getKode("ARBEIDSTAKER"));
        FastsatteVerdier fastsatteVerdier2 = new FastsatteVerdier(0, fastsattBeløp,
                saksbehandler.kodeverk.Inntektskategori.getKode("ARBEIDSTAKER"));
        bekreftelse
                .leggTilFaktaOmBeregningTilfeller(FaktaOmBeregningTilfelle.FASTSETT_ENDRET_BEREGNINGSGRUNNLAG.kode)
                .leggTilAndelerEndretBg(manuellPeriode, tilkommetAndel, fastsatteVerdier)
                .leggTilAndelerEndretBg(manuellPeriode, eksisterendeAndel, fastsatteVerdier2);
        saksbehandler.bekreftAksjonspunktBekreftelse(bekreftelse);

        assertEndretArbeidsforhold(tilkommetDato);

        BeregningsgrunnlagPeriodeDto behandletPeriode = saksbehandler.valgtBehandling.getBeregningsgrunnlag().getBeregningsgrunnlagPeriode(1);
        tilkommetAndel = behandletPeriode.getBeregningsgrunnlagPrStatusOgAndel().stream()
                .filter(andel -> andel.getArbeidsforhold().getArbeidsgiverId().equals(orgNr)).findFirst().get();
        eksisterendeAndel = behandletPeriode.getBeregningsgrunnlagPrStatusOgAndel().stream()
                .filter(andel -> andel.getArbeidsforhold().getArbeidsgiverId().equals(orgNr2)).findFirst().get();
        assertThat(tilkommetAndel.getBeregnetPrAar()).isEqualTo((double)12*fastsattBeløp);
        assertThat(eksisterendeAndel.getBeregnetPrAar()).isEqualTo((double)12*fastsattBeløp);
    }

    private void assertEndretArbeidsforhold(LocalDate tilkommetDato) {
        verifiserLikhet(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning().getFaktaOmBeregningTilfeller()
                .contains(FaktaOmBeregningTilfelle.FASTSETT_ENDRET_BEREGNINGSGRUNNLAG), true);
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getEndringBeregningsgrunnlag().getEndredeArbeidsforhold().size()).isEqualTo(1);
        assertArbeidsforhold(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getEndringBeregningsgrunnlag().getEndredeArbeidsforhold().get(0), "BEDRIFT AS", "910909088");
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getEndringBeregningsgrunnlag().getEndredeArbeidsforhold().get(0).getPerioderMedGraderingEllerRefusjon().size()).isEqualTo(1);
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getEndringBeregningsgrunnlag().getEndredeArbeidsforhold().get(0)
                .getPerioderMedGraderingEllerRefusjon().get(0).getFom()).isEqualTo(tilkommetDato);
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getEndringBeregningsgrunnlag().getEndredeArbeidsforhold().get(0)
                .getPerioderMedGraderingEllerRefusjon().get(0).getTom()).isNull();
    }


    private void assertMottarYtelse(long l, String s, String s2) {
        verifiserLikhet(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning().getFaktaOmBeregningTilfeller()
                .contains(FaktaOmBeregningTilfelle.VURDER_MOTTAR_YTELSE), true);
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getVurderMottarYtelse().isErFrilans()).isEqualTo(false);
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getVurderMottarYtelse().getArbeidstakerAndelerUtenIM().size()).isEqualTo(1);
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getVurderMottarYtelse().getArbeidstakerAndelerUtenIM().get(0)
                .getAndelsnr()).isEqualTo(l);
        assertThat(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                .getVurderMottarYtelse().getArbeidstakerAndelerUtenIM().get(0)
                .getInntektPrMnd()).isEqualTo(30000);
        assertArbeidsforhold(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getFaktaOmBeregning()
                        .getVurderMottarYtelse().getArbeidstakerAndelerUtenIM().get(0).getArbeidsforhold(),
                s, s2);
    }


    private void assertArbeidsforhold(BeregningsgrunnlagArbeidsforholdDto arbeidsforhold, String navn, String arbeidsgiverId) {
        assertThat(arbeidsforhold.getArbeidsgiverNavn()).isEqualTo(navn);
        assertThat(arbeidsforhold.getArbeidsgiverId()).isEqualTo(arbeidsgiverId);
    }

}
