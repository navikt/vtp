package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import static no.nav.foreldrepenger.autotest.util.AllureHelper.debugLoggBehandling;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.qameta.allure.Description;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderPerioderOpptjeningBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarArbeidsforholdBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.VilkarTypeKoder;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.FaktaOmBeregningTilfelle;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag.BeregningsgrunnlagArbeidsforholdDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

@Tag("smoke")
@Tag("foreldrepenger")
public class Beregning extends ForeldrepengerTestBase {

    @Test
    @DisplayName("Mor med ventelønn og vartpenger")
    @Description("Mor med ventelønn og vartpenger")
    public void mor_med_ventelønn_og_vartpenger() throws Exception {
        TestscenarioDto testscenario = opprettScenario("150");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorVentelonnVartpenger(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);

        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

        saksbehandler.hentAksjonspunktbekreftelse(VurderPerioderOpptjeningBekreftelse.class)
                .godkjennAllOpptjening();
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderPerioderOpptjeningBekreftelse.class);

        debugLoggBehandling(saksbehandler.valgtBehandling);

        verifiserLikhet(saksbehandler.valgtBehandling.aksjonspunkter.stream()
                .anyMatch(ap -> ap.erUbekreftet() && ap.getDefinisjon().kode.equals(AksjonspunktKoder.AVKLAR_AKTIVITETER)), true);
        assertThat(saksbehandler.valgtBehandling.beregningsgrunnlag.getFaktaOmBeregning()
                .getAvklarAktiviteter().getVentelonnVartpenger().getInkludert()).isNull();
    }

    @Test
    @DisplayName("Mor med kortvarig arbeidsforhold")
    @Description("Mor med kortvarig arbeidsforhold")
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
                orgNr, Optional.empty(), Optional.empty());
        fordel.sendInnInntektsmelding(inntektsmeldingBuilder, testscenario, saksnummer);
        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilHistorikkinnslag("Vedlegg mottatt");

        debugLoggBehandling(saksbehandler.valgtBehandling);

        saksbehandler.gjenopptaBehandling();


        saksbehandler.hentAksjonspunktbekreftelse(AvklarArbeidsforholdBekreftelse.class)
                .bekreftArbeidsforholdErRelevant("THOMAS AS", true);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarArbeidsforholdBekreftelse.class);

        verifiserLikhet(saksbehandler.valgtBehandling.aksjonspunkter.stream()
                .anyMatch(ap -> ap.erUbekreftet() &&
                        ap.getDefinisjon().kode.equals(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN)), true);
        verifiserLikhet(saksbehandler.valgtBehandling.beregningsgrunnlag.getFaktaOmBeregning().getFaktaOmBeregningTilfeller()
                .contains(FaktaOmBeregningTilfelle.VURDER_TIDSBEGRENSET_ARBEIDSFORHOLD), true);
        assertThat(saksbehandler.valgtBehandling.beregningsgrunnlag.getFaktaOmBeregning()
                .getKortvarigeArbeidsforhold().size()).isEqualTo(1);
        assertThat(saksbehandler.valgtBehandling.beregningsgrunnlag.getFaktaOmBeregning()
                .getKortvarigeArbeidsforhold().get(0).getAndelsnr()).isEqualTo(2L);
        assertArbeidsforhold(saksbehandler.valgtBehandling.beregningsgrunnlag.getFaktaOmBeregning()
                .getKortvarigeArbeidsforhold().get(0).getArbeidsforhold(), "THOMAS AS", "973861778");

        verifiserLikhet(saksbehandler.valgtBehandling.beregningsgrunnlag.getFaktaOmBeregning().getFaktaOmBeregningTilfeller()
                .contains(FaktaOmBeregningTilfelle.VURDER_MOTTAR_YTELSE), true);
        assertThat(saksbehandler.valgtBehandling.beregningsgrunnlag.getFaktaOmBeregning()
                .getVurderMottarYtelse().isErFrilans()).isEqualTo(false);
        assertThat(saksbehandler.valgtBehandling.beregningsgrunnlag.getFaktaOmBeregning()
                .getVurderMottarYtelse().getArbeidstakerAndelerUtenIM().size()).isEqualTo(1);
        assertThat(saksbehandler.valgtBehandling.beregningsgrunnlag.getFaktaOmBeregning()
                .getVurderMottarYtelse().getArbeidstakerAndelerUtenIM().get(0)
        .getAndelsnr()).isEqualTo(2L);
        assertThat(saksbehandler.valgtBehandling.beregningsgrunnlag.getFaktaOmBeregning()
                .getVurderMottarYtelse().getArbeidstakerAndelerUtenIM().get(0)
                .getInntektPrMnd()).isEqualTo(30000);
        assertArbeidsforhold(saksbehandler.valgtBehandling.beregningsgrunnlag.getFaktaOmBeregning()
                .getVurderMottarYtelse().getArbeidstakerAndelerUtenIM().get(0).getArbeidsforhold(),
                "THOMAS AS", "973861778");
    }

    @Test
    @DisplayName("Mor med kortvarig arbeidsforhold med inntektsmelding")
    @Description("Mor med kortvarig arbeidsforhold med inntektsmelding")
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
                orgNr, Optional.empty(), Optional.empty());
        InntektsmeldingBuilder inntektsmeldingBuilder2 = lagInntektsmeldingBuilder(inntektPerMåned, fnr, fpStartdato,
                orgNr2, Optional.empty(), Optional.empty());
        fordel.sendInnInntektsmelding(inntektsmeldingBuilder, testscenario, saksnummer);
        fordel.sendInnInntektsmelding(inntektsmeldingBuilder2, testscenario, saksnummer);
        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilHistorikkinnslag("Vedlegg mottatt");

        debugLoggBehandling(saksbehandler.valgtBehandling);
        verifiserLikhet(saksbehandler.valgtBehandling.aksjonspunkter.stream()
                .anyMatch(ap -> ap.erUbekreftet() &&
                        ap.getDefinisjon().kode.equals(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN)), true);
        verifiserLikhet(saksbehandler.valgtBehandling.beregningsgrunnlag.getFaktaOmBeregning().getFaktaOmBeregningTilfeller()
                .contains(FaktaOmBeregningTilfelle.VURDER_TIDSBEGRENSET_ARBEIDSFORHOLD), true);
        assertThat(saksbehandler.valgtBehandling.beregningsgrunnlag.getFaktaOmBeregning()
                .getKortvarigeArbeidsforhold().size()).isEqualTo(1);
        assertThat(saksbehandler.valgtBehandling.beregningsgrunnlag.getFaktaOmBeregning()
                .getKortvarigeArbeidsforhold().get(0).getAndelsnr()).isEqualTo(2L);
        assertArbeidsforhold(saksbehandler.valgtBehandling.beregningsgrunnlag.getFaktaOmBeregning()
                .getKortvarigeArbeidsforhold().get(0).getArbeidsforhold(), "THOMAS AS", "973861778");
    }


    @Test
    @DisplayName("Endret beregningsgrunnlag med kortvarig")
    @Description("Endret beregningsgrunnlag med kortvarig")
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
                orgNr, Optional.empty(), Optional.of(refusjon));
        InntektsmeldingBuilder inntektsmeldingBuilder2 = lagInntektsmeldingBuilderMedGradering(inntektPerMåned, fnr, fpStartdato,
                orgNr2, Optional.empty(), Optional.empty(), 50, fpStartdato, fpStartdato.plusMonths(2));
        fordel.sendInnInntektsmelding(inntektsmeldingBuilder, testscenario, saksnummer);
        fordel.sendInnInntektsmelding(inntektsmeldingBuilder2, testscenario, saksnummer);
        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilHistorikkinnslag("Vedlegg mottatt");

        debugLoggBehandling(saksbehandler.valgtBehandling);
        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN);
        verifiserLikhet(saksbehandler.valgtBehandling.aksjonspunkter.stream()
                .anyMatch(ap -> ap.erUbekreftet() &&
                        ap.getDefinisjon().kode.equals(AksjonspunktKoder.VURDER_FAKTA_FOR_ATFL_SN)), true);
        verifiserLikhet(saksbehandler.valgtBehandling.beregningsgrunnlag.getFaktaOmBeregning().getFaktaOmBeregningTilfeller()
                .contains(FaktaOmBeregningTilfelle.VURDER_TIDSBEGRENSET_ARBEIDSFORHOLD), true);
        assertThat(saksbehandler.valgtBehandling.beregningsgrunnlag.getFaktaOmBeregning()
                .getKortvarigeArbeidsforhold().size()).isEqualTo(1);
        assertThat(saksbehandler.valgtBehandling.beregningsgrunnlag.getFaktaOmBeregning()
                .getKortvarigeArbeidsforhold().get(0).getAndelsnr()).isEqualTo(2L);
        assertArbeidsforhold(saksbehandler.valgtBehandling.beregningsgrunnlag.getFaktaOmBeregning()
                .getKortvarigeArbeidsforhold().get(0).getArbeidsforhold(), "THOMAS AS", "973861778");


        verifiserLikhet(saksbehandler.valgtBehandling.beregningsgrunnlag.getFaktaOmBeregning().getFaktaOmBeregningTilfeller()
                .contains(FaktaOmBeregningTilfelle.FASTSETT_ENDRET_BEREGNINGSGRUNNLAG), true);
        assertThat(saksbehandler.valgtBehandling.beregningsgrunnlag.getFaktaOmBeregning()
                .getEndringBeregningsgrunnlag().getEndredeArbeidsforhold().size()).isEqualTo(1);
        assertArbeidsforhold(saksbehandler.valgtBehandling.beregningsgrunnlag.getFaktaOmBeregning()
                .getEndringBeregningsgrunnlag().getEndredeArbeidsforhold().get(0), "THOMAS AS", "973861778");
        assertThat(saksbehandler.valgtBehandling.beregningsgrunnlag.getFaktaOmBeregning()
                .getEndringBeregningsgrunnlag().getEndredeArbeidsforhold().get(0).getPerioderMedGraderingEllerRefusjon().size()).isEqualTo(1);
        assertThat(saksbehandler.valgtBehandling.beregningsgrunnlag.getFaktaOmBeregning()
                .getEndringBeregningsgrunnlag().getEndredeArbeidsforhold().get(0)
                .getPerioderMedGraderingEllerRefusjon().get(0).getFom()).isEqualTo(fpStartdato);
        assertThat(saksbehandler.valgtBehandling.beregningsgrunnlag.getFaktaOmBeregning()
                .getEndringBeregningsgrunnlag().getEndredeArbeidsforhold().get(0)
                .getPerioderMedGraderingEllerRefusjon().get(0).getTom()).isEqualTo(fpStartdato.plusMonths(2));

        assertThat(saksbehandler.valgtBehandling.beregningsgrunnlag.antallBeregningsgrunnlagPeriodeDto()).isEqualTo(1);
    }


    private void assertArbeidsforhold(BeregningsgrunnlagArbeidsforholdDto arbeidsforhold, String navn, String arbeidsgiverId) {
        assertThat(arbeidsforhold.getArbeidsgiverNavn()).isEqualTo(navn);
        assertThat(arbeidsforhold.getArbeidsgiverId()).isEqualTo(arbeidsgiverId);
    }

}
