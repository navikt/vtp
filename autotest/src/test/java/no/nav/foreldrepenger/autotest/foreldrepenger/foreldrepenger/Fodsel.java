package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import static no.nav.foreldrepenger.autotest.util.AllureHelper.debugLoggBehandling;
import static no.nav.foreldrepenger.autotest.util.AllureHelper.debugLoggHistorikkinnslag;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_FELLESPERIODE;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.UTSETTELSETYPE_ARBEID;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.UTSETTELSETYPE_INNLAGTBARN;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.UTSETTELSETYPE_INNLAGTSØKER;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.UTSETTELSETYPE_SYKDOMSKADE;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import io.qameta.allure.Description;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.base.ForeldrepengerTestBase;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FastsettBruttoBeregningsgrunnlagSNBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FastsettUttaksperioderManueltBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FatterVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.ForesloVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderBeregnetInntektsAvvikBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderFaktaOmBeregningBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderManglendeFodselBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderPerioderOpptjeningBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderVarigEndringEllerNyoppstartetSNBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaAleneomsorgBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaUttakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.papirsoknad.PapirSoknadForeldrepengerBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Aksjonspunkt;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.BeregningsresultatMedUttaksplan;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.BeregningsresultatPeriode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.BeregningsresultatPeriodeAndel;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.FaktaOmBeregningTilfelle;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag.BeregningsgrunnlagPrStatusOgAndelDto;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.papirsøknad.FordelingDto;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.papirsøknad.PermisjonPeriodeDto;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak.UttakResultatPeriode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak.UttakResultatPeriodeAktivitet;
import no.nav.foreldrepenger.autotest.klienter.fpsak.historikk.dto.HistorikkInnslag;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;
import no.nav.foreldrepenger.autotest.klienter.vtp.expect.dto.ExpectRequestDto;
import no.nav.foreldrepenger.autotest.klienter.vtp.expect.dto.ExpectResultDto;
import no.nav.foreldrepenger.autotest.klienter.vtp.expect.dto.ExpectTokenDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.felles.ExpectPredicate;
import no.nav.foreldrepenger.fpmock2.felles.ExpectRepository.Mock;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arbeidsforhold.Arbeidsforhold;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.Fordeling;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.LukketPeriodeMedVedlegg;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.ObjectFactory;

@Execution(ExecutionMode.CONCURRENT)
@Tag("fpsak")
@Tag("foreldrepenger")
public class Fodsel extends ForeldrepengerTestBase {

    @Test
    @DisplayName("Mor fødsel med arbeidsforhold og frilans. Vurderer opptjening og beregning. Finner avvik")
    @Description("Mor søker fødsel med ett arbeidsforhold og frilans. Vurder opptjening. Vurder fakta om beregning. Avvik i beregning")
    public void morSøkerFødselMedEttArbeidsforholdOgFrilans_VurderOpptjening_VurderFaktaOmBeregning_AvvikIBeregning() throws Exception {
        TestscenarioDto testscenario = opprettScenario("59");

        String fnr = testscenario.getPersonopplysninger().getSøkerIdent();
        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);
        String orgNr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();

        int inntektPerMåned = 20_000;
        int overstyrtInntekt = 500_000;
        int overstyrtFrilanserInntekt = 500_000;
        BigDecimal refusjon = BigDecimal.valueOf(overstyrtInntekt + overstyrtFrilanserInntekt);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorMedFrilans(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        InntektsmeldingBuilder inntektsmeldingBuilder = lagInntektsmeldingBuilder(inntektPerMåned, fnr, fpStartdato,
                orgNr, Optional.empty(), Optional.of(refusjon), Optional.empty());

        fordel.sendInnInntektsmelding(inntektsmeldingBuilder, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilHistorikkinnslag(HistorikkInnslag.VEDLEGG_MOTTATT);

        debugLoggBehandling(saksbehandler.valgtBehandling);
        saksbehandler.hentAksjonspunktbekreftelse(VurderFaktaOmBeregningBekreftelse.class)
                .leggTilFaktaOmBeregningTilfeller(FaktaOmBeregningTilfelle.VURDER_MOTTAR_YTELSE.kode)
                .leggTilMottarYtelse(false, Collections.emptyList());
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderFaktaOmBeregningBekreftelse.class);

        //Verifiser Beregningsgrunnlag
        verifiserLikhet(saksbehandler.valgtBehandling.getBeregningsgrunnlag().antallAktivitetStatus(), 1);//ikke sikker på denne
        verifiserLikhet(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getAktivitetStatus(0).kode, "AT_FL");
        verifiserLikhet(saksbehandler.valgtBehandling.getBeregningsgrunnlag().antallBeregningsgrunnlagPeriodeDto(), 1);
        List<BeregningsgrunnlagPrStatusOgAndelDto> andeler = saksbehandler.valgtBehandling.getBeregningsgrunnlag().getBeregningsgrunnlagPeriode(0).getBeregningsgrunnlagPrStatusOgAndel();
        verifiserLikhet(andeler.size(), 2);
        verifiserLikhet(andeler.get(0).getAktivitetStatus().kode, "AT");
        verifiserLikhet(andeler.get(1).getAktivitetStatus().kode, "FL");

        //Legg til og fjern ytelser for å se tilbakehopp og opprettelse av akjsonspunkter
        verifiser(saksbehandler.harAksjonspunkt(AksjonspunktKoder.FASTSETT_BEREGNINGSGRUNNLAG_ARBEIDSTAKER_FRILANS));
        saksbehandler.hentAksjonspunktbekreftelse(VurderFaktaOmBeregningBekreftelse.class)
                .leggTilFaktaOmBeregningTilfeller(FaktaOmBeregningTilfelle.VURDER_MOTTAR_YTELSE.kode)
                .leggTilFaktaOmBeregningTilfeller(FaktaOmBeregningTilfelle.FASTSETT_MAANEDSINNTEKT_FL.kode)
                .leggTilMottarYtelse(true, Collections.emptyList())
                .leggTilMaanedsinntekt(25800);
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderFaktaOmBeregningBekreftelse.class);
        verifiser(!saksbehandler.harAksjonspunkt(AksjonspunktKoder.FASTSETT_BEREGNINGSGRUNNLAG_ARBEIDSTAKER_FRILANS), "Fått AP hvor det ikke skal være det");

        saksbehandler.hentAksjonspunktbekreftelse(VurderFaktaOmBeregningBekreftelse.class)
                .leggTilFaktaOmBeregningTilfeller(FaktaOmBeregningTilfelle.VURDER_MOTTAR_YTELSE.kode)
                .leggTilMottarYtelse(false, Collections.emptyList());
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderFaktaOmBeregningBekreftelse.class);

        saksbehandler.hentAksjonspunktbekreftelse(VurderBeregnetInntektsAvvikBekreftelse.class)
                .leggTilInntektFrilans(overstyrtFrilanserInntekt)
                .leggTilInntekt(overstyrtInntekt, 2L)
                .setBegrunnelse("Begrunnelse");
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderBeregnetInntektsAvvikBekreftelse.class);

        saksbehandler.hentAksjonspunktbekreftelse(ForesloVedtakBekreftelse.class);
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Aktoer.Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);

        Aksjonspunkt ap = beslutter.hentAksjonspunkt(AksjonspunktKoder.FASTSETT_BEREGNINGSGRUNNLAG_ARBEIDSTAKER_FRILANS);
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkter(Collections.singletonList(ap));
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();


        verifiserLikhet(beslutter.valgtBehandling.hentBehandlingsresultat(), "INNVILGET");
        verifiserLikhet(beslutter.getBehandlingsstatus(), "AVSLU");
        verifiser(beslutter.harHistorikkinnslag(HistorikkInnslag.BREV_SENDT));
        verifiserUttak(2, beslutter.valgtBehandling.hentUttaksperioder());
        verifiserTilkjentYtelse(beslutter.valgtBehandling.getBeregningResultatForeldrepenger(), true);

    }

    @Test
    @DisplayName("Mor fødsel SN med avvik i beregning")
    @Description("Mor søker fødsel som selvstendig næringsdrivende. Avvik i beregning")
    public void morSøkerFødselSomSelvstendingNæringsdrivende_AvvikIBeregning() throws Exception {

        TestscenarioDto testscenario = opprettScenario("48");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorMedEgenNaering(søkerAktørIdent, fødselsdato, fpStartdato.plusWeeks(2));
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);

        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

        saksbehandler.hentAksjonspunktbekreftelse(VurderPerioderOpptjeningBekreftelse.class).godkjennAllOpptjening();
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderPerioderOpptjeningBekreftelse.class);

        //Verifiser at aksjonspunkt 5042 ikke blir oprettet uten varig endring
        saksbehandler.hentAksjonspunktbekreftelse(VurderVarigEndringEllerNyoppstartetSNBekreftelse.class)
                .setErVarigEndretNaering(false)
                .setBegrunnelse("Ingen endring");
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderVarigEndringEllerNyoppstartetSNBekreftelse.class);
        verifiser(!saksbehandler.harAksjonspunkt(AksjonspunktKoder.FASTSETT_BEREGNINGSGRUNNLAG_SELVSTENDIG_NÆRINGSDRIVENDE), "Har uventet aksjonspunkt: 5042");

        saksbehandler.hentAksjonspunktbekreftelse(VurderVarigEndringEllerNyoppstartetSNBekreftelse.class)
                .setErVarigEndretNaering(true)
                .setBegrunnelse("Endring eller nyoppstartet begrunnelse");
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderVarigEndringEllerNyoppstartetSNBekreftelse.class);
        saksbehandler.hentAksjonspunktbekreftelse(FastsettBruttoBeregningsgrunnlagSNBekreftelse.class)
                .setBruttoBeregningsgrunnlag(1_000_000)
                .setBegrunnelse("Grunnlag begrunnelse");
        saksbehandler.bekreftAksjonspunktBekreftelse(FastsettBruttoBeregningsgrunnlagSNBekreftelse.class);

        //verifiser skjæringstidspunkt i følge søknad
        verifiserLikhet(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getSkjaeringstidspunktBeregning(), fpStartdato);


        saksbehandler.hentAksjonspunktbekreftelse(ForesloVedtakBekreftelse.class);
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Aktoer.Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);

        Aksjonspunkt ap1 = beslutter.hentAksjonspunkt(AksjonspunktKoder.VURDER_VARIG_ENDRET_ELLER_NYOPPSTARTET_NÆRING_SELVSTENDIG_NÆRINGSDRIVENDE);
        Aksjonspunkt ap2 = beslutter.hentAksjonspunkt(AksjonspunktKoder.FASTSETT_BEREGNINGSGRUNNLAG_SELVSTENDIG_NÆRINGSDRIVENDE);
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkter(Arrays.asList(ap1, ap2));
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();

        verifiserLikhet(beslutter.valgtBehandling.hentBehandlingsresultat(), "INNVILGET");
        verifiserLikhet(beslutter.getBehandlingsstatus(), "AVSLU");
        verifiser(beslutter.harHistorikkinnslag(HistorikkInnslag.BREV_SENDT));
        verifiserUttak(1, beslutter.valgtBehandling.hentUttaksperioder());
        verifiserTilkjentYtelse(beslutter.valgtBehandling.getBeregningResultatForeldrepenger(), false);

    }

    @Test
    @DisplayName("Mor søker fødsel med 2 arbeidsforhold og avvik i beregning")
    public void morSøkerFødselMedToArbeidsforhold_AvvikIBeregning() throws Exception {

        TestscenarioDto testscenario = opprettScenario("57");

        String fnr = testscenario.getPersonopplysninger().getSøkerIdent();
        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);

        int inntektPrMåned = 50_000;
        int overstyrtInntekt = inntektPrMåned * 12;
        Optional<BigDecimal> refusjon = Optional.of(BigDecimal.valueOf(inntektPrMåned));

        Arbeidsforhold arbeidsforhold_1 = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0);
        Arbeidsforhold arbeidsforhold_2 = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(1);
        Optional<String> arbeidsforholdId_1 = Optional.of(arbeidsforhold_1.getArbeidsforholdId());
        Optional<String> arbeidsforholdId_2 = Optional.of(arbeidsforhold_2.getArbeidsforholdId());
        String orgNr_1 = arbeidsforhold_1.getArbeidsgiverOrgnr();
        String orgNr_2 = arbeidsforhold_2.getArbeidsgiverOrgnr();

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        InntektsmeldingBuilder inntektsmeldingBuilder_1 = lagInntektsmeldingBuilder(inntektPrMåned, fnr, fpStartdato, orgNr_1, arbeidsforholdId_1, refusjon, Optional.empty());
        InntektsmeldingBuilder inntektsmeldingBuilder_2 = lagInntektsmeldingBuilder(inntektPrMåned, fnr, fpStartdato, orgNr_2, arbeidsforholdId_2, refusjon, Optional.empty());

        fordel.sendInnInntektsmeldinger(Arrays.asList(inntektsmeldingBuilder_1, inntektsmeldingBuilder_2), testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);

        saksbehandler.hentFagsak(saksnummer);
        debugLoggBehandling(saksbehandler.valgtBehandling);
        saksbehandler.hentAksjonspunktbekreftelse(VurderBeregnetInntektsAvvikBekreftelse.class)
                .leggTilInntekt(overstyrtInntekt, 1L)
                .leggTilInntekt(overstyrtInntekt, 2L)
                .setBegrunnelse("Begrunnelse");
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderBeregnetInntektsAvvikBekreftelse.class);

        saksbehandler.hentAksjonspunktbekreftelse(ForesloVedtakBekreftelse.class);
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Aktoer.Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);

        Aksjonspunkt ap = beslutter.hentAksjonspunkt(AksjonspunktKoder.FASTSETT_BEREGNINGSGRUNNLAG_ARBEIDSTAKER_FRILANS);
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(ap);
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();

        verifiserLikhet(beslutter.valgtBehandling.hentBehandlingsresultat(), "INNVILGET");
        verifiserLikhet(beslutter.getBehandlingsstatus(), "AVSLU");
        verifiser(beslutter.harHistorikkinnslag(HistorikkInnslag.BREV_SENDT));
        verifiserUttak(2, beslutter.valgtBehandling.hentUttaksperioder());
        verifiserTilkjentYtelse(beslutter.valgtBehandling.getBeregningResultatForeldrepenger(), true);

    }

    @Test
    @DisplayName("Mor søker fødsel med 1 arbeidsforhold og avvik i beregning")
    public void morSøkerFødselMedEttArbeidsforhold_AvvikIBeregning() throws Exception {

        TestscenarioDto testscenario = opprettScenario("49");

        String orgNr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        String fnr = testscenario.getPersonopplysninger().getSøkerIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);

        int inntektPrMåned = 15_000;
        BigDecimal refusjon = BigDecimal.valueOf(inntektPrMåned);
        int overstyrtInntekt = inntektPrMåned * 12;

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(testscenario.getPersonopplysninger().getSøkerAktørIdent(), fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);

        InntektsmeldingBuilder inntektsmeldingBuilder = lagInntektsmeldingBuilder(inntektPrMåned, fnr, fpStartdato, orgNr, Optional.empty(), Optional.of(refusjon), Optional.empty());
        fordel.sendInnInntektsmelding(inntektsmeldingBuilder, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

        debugLoggBehandling(saksbehandler.valgtBehandling);
        saksbehandler.hentAksjonspunktbekreftelse(VurderBeregnetInntektsAvvikBekreftelse.class)
                .leggTilInntekt(overstyrtInntekt, 1L)
                .setBegrunnelse("Begrunnelse");
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderBeregnetInntektsAvvikBekreftelse.class);

        saksbehandler.hentAksjonspunktbekreftelse(ForesloVedtakBekreftelse.class);
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Aktoer.Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);

        Aksjonspunkt ap = beslutter.hentAksjonspunkt(AksjonspunktKoder.FASTSETT_BEREGNINGSGRUNNLAG_ARBEIDSTAKER_FRILANS);
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(ap);
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();

        verifiserLikhet(beslutter.valgtBehandling.hentBehandlingsresultat(), "INNVILGET");
        verifiserLikhet(beslutter.getBehandlingsstatus(), "AVSLU");
        verifiser(beslutter.harHistorikkinnslag(HistorikkInnslag.BREV_SENDT));
        verifiserUttak(1, beslutter.valgtBehandling.hentUttaksperioder());
        verifiserTilkjentYtelse(beslutter.valgtBehandling.getBeregningResultatForeldrepenger(), true);

    }

    @Test
    @DisplayName("Mor søker fødsel med 2 arbeidsforhold i samme organisasjon")
    public void morSøkerFødselMedToArbeidsforholdISammeOrganisasjon() throws Exception {

        TestscenarioDto testscenario = opprettScenario("57");

        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);
        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        String fnr = testscenario.getPersonopplysninger().getSøkerIdent();

        Arbeidsforhold arbeidsforhold_1 = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0);
        Arbeidsforhold arbeidsforhold_2 = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(1);
        String arbeidsgiverOrgnr_1 = arbeidsforhold_1.getArbeidsgiverOrgnr();
        String arbeidsgiverOrgnr_2 = arbeidsforhold_2.getArbeidsgiverOrgnr();

        List<Integer> inntekter = sorterteInntektsbeløp(testscenario);

        InntektsmeldingBuilder inntektsmeldingBuilder_1 = lagInntektsmeldingBuilder(inntekter.get(0), fnr,
                fpStartdato, arbeidsgiverOrgnr_1, Optional.of(arbeidsforhold_1.getArbeidsforholdId()), Optional.empty(), Optional.empty());
        InntektsmeldingBuilder inntektsmeldingBuilder_2 = lagInntektsmeldingBuilder(inntekter.get(1), fnr,
                fpStartdato, arbeidsgiverOrgnr_2, Optional.of(arbeidsforhold_2.getArbeidsforholdId()), Optional.empty(), Optional.empty());

        fordel.sendInnInntektsmeldinger(Arrays.asList(inntektsmeldingBuilder_1, inntektsmeldingBuilder_2), testscenario, saksnummer);

        debugLoggBehandling(saksbehandler.valgtBehandling);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilAvsluttetBehandling();

        debugLoggBehandling(saksbehandler.valgtBehandling);
        verifiserLikhet(saksbehandler.valgtBehandling.hentBehandlingsresultat(), "INNVILGET");
        verifiserLikhet(saksbehandler.getBehandlingsstatus(), "AVSLU");
        verifiser(saksbehandler.harHistorikkinnslag(HistorikkInnslag.BREV_SENDT));
        verifiserUttak(2, saksbehandler.valgtBehandling.hentUttaksperioder());
        verifiserTilkjentYtelse(saksbehandler.valgtBehandling.getBeregningResultatForeldrepenger(), false);

    }

    @Test
    @DisplayName("Mor søker fødsel med 1 arbeidsforhold")
    public void morSøkerFødselMedEttArbeidsforhold() throws Exception {
        TestscenarioDto testscenario = opprettScenario("49");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate startDatoForeldrepenger = fødselsdato.minusWeeks(3);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, startDatoForeldrepenger);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilAvsluttetBehandling();

        debugLoggBehandling(saksbehandler.valgtBehandling);
        verifiserLikhet(saksbehandler.valgtBehandling.hentBehandlingsresultat(), "INNVILGET");
        verifiserLikhet(saksbehandler.getBehandlingsstatus(), "AVSLU");
        verifiser(saksbehandler.harHistorikkinnslag(HistorikkInnslag.BREV_SENDT));
        verifiserUttak(1, saksbehandler.valgtBehandling.hentUttaksperioder());
        verifiserTilkjentYtelse(saksbehandler.valgtBehandling.getBeregningResultatForeldrepenger(), false);
    }

    @Test
    @DisplayName("Mor søker fødsel med 2 arbeidsforhold")
    public void morSøkerFødselMedToArbeidsforhold() throws Exception {
        TestscenarioDto testscenario = opprettScenario("56");

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
        saksbehandler.ventTilAvsluttetBehandling();

        verifiserLikhet(saksbehandler.valgtBehandling.hentBehandlingsresultat(), "INNVILGET");
        verifiserLikhet(saksbehandler.getBehandlingsstatus(), "AVSLU");
        verifiser(saksbehandler.harHistorikkinnslag(HistorikkInnslag.BREV_SENDT));
        verifiserUttak(2, saksbehandler.valgtBehandling.hentUttaksperioder());
        verifiserTilkjentYtelse(saksbehandler.valgtBehandling.getBeregningResultatForeldrepenger(), false);
    }

    @Test
    @DisplayName("Far søker fødsel med 1 arbeidsforhold")
    public void farSøkerFødselMedEttArbeidsforhold() throws Exception {
        TestscenarioDto testscenario = opprettScenario("62");

        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate startDatoForeldrepenger = fødselsdato.plusWeeks(3);

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        String søkerIdent = testscenario.getPersonopplysninger().getSøkerIdent();
        System.out.println("Ident: " + søkerIdent);

        ExpectTokenDto token = expectKlient.createExpectation(new ExpectRequestDto(Mock.GSAK.toString(), "opprettSak", new ExpectPredicate("aktør", søkerIdent)));
        
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunFar(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);

        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);

        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, startDatoForeldrepenger);

        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.AVKLAR_FAKTA_UTTAK);
        saksbehandler.hentAksjonspunktbekreftelse(AvklarFaktaUttakBekreftelse.class).
                godkjennPeriode(startDatoForeldrepenger, startDatoForeldrepenger.plusWeeks(2),
                        saksbehandler.kodeverk.UttakPeriodeVurderingType.getKode("PERIODE_KAN_IKKE_AVKLARES"));
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaUttakBekreftelse.class);

        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.FASTSETT_UTTAKPERIODER);
        saksbehandler.hentAksjonspunktbekreftelse(FastsettUttaksperioderManueltBekreftelse.class).
                godkjennPeriode(startDatoForeldrepenger, startDatoForeldrepenger.plusWeeks(2), 100);
        saksbehandler.bekreftAksjonspunktBekreftelse(FastsettUttaksperioderManueltBekreftelse.class);

        List<UttakResultatPeriode> perioder = saksbehandler.valgtBehandling.hentUttaksperioder();
        verifiserLikhet(perioder.size(), 1);

        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);

        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
            .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.AVKLAR_FAKTA_UTTAK));

        ExpectTokenDto expectXml = expectKlient.createExpectation(new ExpectRequestDto(Mock.DOKUMENTPRODUKSJON.toString(), "produserIkkeredigerbartDokument", new ExpectPredicate("aktør", søkerIdent)));

        beslutter.fattVedtakOgVentTilAvsluttetBehandling();

        verifiser(beslutter.harHistorikkinnslag(HistorikkInnslag.VEDTAK_FATTET), "behandling har ikke historikkinslag 'Vedtak fattet'");
        verifiser(beslutter.harHistorikkinnslag(HistorikkInnslag.BREV_SENDT), "behandling har ikke historikkinslag 'Brev sendt'");
        
        ExpectResultDto result = expectKlient.checkExpectation(token);
        verifiser(result.isExpectationMet(), "Forventningen er ikke møtt");
        
        result = expectKlient.checkExpectation(expectXml);
        verifiser(result.isExpectationMet(), "xml brev ikke truffet");
        
        //String xml = result.getResultData();
        //System.out.println(xml);
        //BrevMalXml mal1 = BrevMalXml.fromString(xml);
        //BrevMalXml mal2 = BrevMalXml.fromString(xml);
        
        //verifiser(mal1.isComparable(mal2));
    }

    @Test
    @DisplayName("Mor søker fødsel med 2 arbeidsforhold i samme organisasjon med 1 inntektsmelding")
    public void morSøkerFødselMedToArbeidsforholdISammeOrganisasjonEnInntektsmelding() throws Exception {

        TestscenarioDto testscenario = opprettScenario("57");

        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);
        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);

        String fnr = testscenario.getPersonopplysninger().getSøkerIdent();
        List<Integer> inntekter = sorterteInntektsbeløp(testscenario);
        String orgnr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        InntektsmeldingBuilder inntektsmelding = lagInntektsmeldingBuilder(inntekter.get(0) + inntekter.get(1), fnr,
                fpStartdato, orgnr, Optional.empty(), Optional.empty(), Optional.empty());

        fordel.sendInnInntektsmelding(inntektsmelding, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilAvsluttetBehandling();

        verifiserLikhet(saksbehandler.valgtBehandling.hentBehandlingsresultat(), "INNVILGET");
        verifiserLikhet(saksbehandler.getBehandlingsstatus(), "AVSLU");
        verifiser(saksbehandler.harHistorikkinnslag(HistorikkInnslag.BREV_SENDT));
        verifiserUttak(1, saksbehandler.valgtBehandling.hentUttaksperioder());
        verifiserTilkjentYtelse(saksbehandler.valgtBehandling.getBeregningResultatForeldrepenger(), false);

    }

    @Test
    @DisplayName("Mor søker fødsel med 1 arbeidsforhold, Papirsøkand")
    public void morSøkerFødselMedEttArbeidsforhold_papirsøknad() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnPapirsøkand(testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate startDatoForeldrepenger = fødselsdato.minusWeeks(3);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, startDatoForeldrepenger);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

        PapirSoknadForeldrepengerBekreftelse aksjonspunktBekreftelse = saksbehandler.aksjonspunktBekreftelse(PapirSoknadForeldrepengerBekreftelse.class);
        FordelingDto fordeling = new FordelingDto();
        PermisjonPeriodeDto fpff = new PermisjonPeriodeDto(FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL,
                startDatoForeldrepenger, fødselsdato.minusDays(1));
        PermisjonPeriodeDto mødrekvote = new PermisjonPeriodeDto(FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE,
                fødselsdato, fødselsdato.plusWeeks(10));
        fordeling.permisjonsPerioder.add(fpff);
        fordeling.permisjonsPerioder.add(mødrekvote);
        aksjonspunktBekreftelse.morSøkerFødsel(fordeling, fødselsdato, fpff.periodeFom);

        saksbehandler.bekreftAksjonspunktBekreftelse(aksjonspunktBekreftelse);
        saksbehandler.ventTilAvsluttetBehandling();

        //verifiserer uttak
        List<UttakResultatPeriode> perioder = saksbehandler.valgtBehandling.hentUttaksperioder();
        assertThat(perioder).hasSize(3);
        verifiserUttaksperiode(perioder.get(0), STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, 1);
        verifiserUttaksperiode(perioder.get(1), STØNADSKONTOTYPE_MØDREKVOTE, 1);
        verifiserUttaksperiode(perioder.get(2), STØNADSKONTOTYPE_MØDREKVOTE, 1);
    }

    @Test
    @Description("Mor søker fødsel med 2 arbeidsforhold med arbeidsforhold som ikke matcher på ID")
    @DisplayName("Mor søker fødsel med 2 arbeidsforhold med arbeidsforhold som ikke matcher på ID")
    public void morSøkerFødselMed2ArbeidsforholdArbeidsforholdIdMatcherIkke() throws Exception {
        TestscenarioDto testscenario = opprettScenario("56");
        String fnr = testscenario.getPersonopplysninger().getSøkerIdent();
        String orgNr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        String orgNr2 = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(1).getArbeidsgiverOrgnr();

        int inntektPerMåned = 20_000;

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate startDatoForeldrepenger = fødselsdato.minusWeeks(3);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        InntektsmeldingBuilder inntektsmeldingBuilder = lagInntektsmeldingBuilder(inntektPerMåned, fnr, startDatoForeldrepenger,
                orgNr, Optional.of("1"), Optional.empty(), Optional.empty());
        InntektsmeldingBuilder inntektsmeldingBuilder2 = lagInntektsmeldingBuilder(inntektPerMåned, fnr, startDatoForeldrepenger,
                orgNr2, Optional.of("9"), Optional.empty(), Optional.empty());

        fordel.sendInnInntektsmelding(inntektsmeldingBuilder, testscenario, saksnummer);
        fordel.sendInnInntektsmelding(inntektsmeldingBuilder2, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        verifiser(saksbehandler.valgtBehandling.erSattPåVent() == true, "Behandling ikke satt på vent");
        debugLoggBehandling(saksbehandler.valgtBehandling);

    }

    @Test
    @Description("Mor søker fødsel med privatperson som arbeidsgiver")
    @DisplayName("Mor søker fødsel med privatperson som arbeidsgiver")
    public void morSøkerFødselMedPrivatpersonSomArbeidsgiver() throws Exception {

        int overstyrtInntekt = 250_000;

        // Lag privat arbeidsgiver
        TestscenarioDto arbeidsgiverScenario = opprettScenario("59");
        String arbeidsgiverFnr = arbeidsgiverScenario.getPersonopplysninger().getSøkerIdent();

        // Lag testscenario
        TestscenarioDto testscenario = opprettScenarioMedPrivatArbeidsgiver("152", arbeidsgiverFnr);

        // Send inn søknad
        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = LocalDate.now().minusDays(2);
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);

        // Send inn inntektsmelding
        int inntektPerMaaned = 20_000;
        LocalDate startDatoForeldrepenger = fødselsdato.minusWeeks(3);
        String fnr = testscenario.getPersonopplysninger().getSøkerIdent();
        InntektsmeldingBuilder inntektsmeldingBuilder = lagInntektsmeldingBuilderPrivatArbeidsgiver(inntektPerMaaned, fnr,
                startDatoForeldrepenger, arbeidsgiverFnr, Optional.empty(), Optional.empty());
        fordel.sendInnInntektsmelding(inntektsmeldingBuilder, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

        debugLoggBehandling(saksbehandler.valgtBehandling);

        // Bekreft Opptjening
        saksbehandler.hentAksjonspunktbekreftelse(VurderPerioderOpptjeningBekreftelse.class)
                .godkjennAllOpptjening();
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderPerioderOpptjeningBekreftelse.class);

        //Verifiser Beregningsgrunnlag
        verifiserLikhet(saksbehandler.valgtBehandling.getBeregningsgrunnlag().antallAktivitetStatus(), 1);//ikke sikker på denne
        verifiserLikhet(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getAktivitetStatus(0).kode, "AT");
        verifiserLikhet(saksbehandler.valgtBehandling.getBeregningsgrunnlag().antallBeregningsgrunnlagPeriodeDto(), 1);
        List<BeregningsgrunnlagPrStatusOgAndelDto> andeler = saksbehandler.valgtBehandling.getBeregningsgrunnlag().getBeregningsgrunnlagPeriode(0).getBeregningsgrunnlagPrStatusOgAndel();
        verifiserLikhet(andeler.size(), 1);
        verifiserLikhet(andeler.get(0).getAktivitetStatus().kode, "AT");

        // Bekreft inntekt i beregning
        saksbehandler.hentAksjonspunktbekreftelse(VurderBeregnetInntektsAvvikBekreftelse.class)
                .leggTilInntekt(overstyrtInntekt, 1L)
                .setBegrunnelse("Begrunnelse");
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderBeregnetInntektsAvvikBekreftelse.class);

        // Foreslå vedtak
        saksbehandler.hentAksjonspunktbekreftelse(ForesloVedtakBekreftelse.class);
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        // Totrinnskontroll
        beslutter.erLoggetInnMedRolle(Aktoer.Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);
        Aksjonspunkt ap = beslutter.hentAksjonspunkt(AksjonspunktKoder.FASTSETT_BEREGNINGSGRUNNLAG_ARBEIDSTAKER_FRILANS);
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkter(Collections.singletonList(ap));
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();


        verifiserLikhet(beslutter.valgtBehandling.hentBehandlingsresultat(), "INNVILGET");
        verifiserLikhet(beslutter.getBehandlingsstatus(), "AVSLU");
        verifiser(beslutter.harHistorikkinnslag(HistorikkInnslag.BREV_SENDT));
        verifiserUttak(1, beslutter.valgtBehandling.hentUttaksperioder());
        verifiserTilkjentYtelse(beslutter.valgtBehandling.getBeregningResultatForeldrepenger(), false);
    }

    @Test
    @Description("Mor søker fødsel med privatperson som arbeidsgiver med endring i refusjon")
    @DisplayName("Mor søker fødsel med privatperson som arbeidsgiver med endring i refusjon")
    public void morSøkerFødselMedPrivatpersonSomArbeidsgiverMedEndringIRefusjon() throws Exception {

        int overstyrtInntekt = 250_000;

        // Lag privat arbeidsgiver
        TestscenarioDto arbeidsgiverScenario = opprettScenario("59");
        String arbeidsgiverFnr = arbeidsgiverScenario.getPersonopplysninger().getSøkerIdent();

        // Lag testscenario
        TestscenarioDto testscenario = opprettScenarioMedPrivatArbeidsgiver("152", arbeidsgiverFnr);

        // Send inn søknad
        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = LocalDate.now().minusDays(2);
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);

        // Send inn inntektsmelding
        int inntektPerMaaned = 20_000;
        int refusjon = 25_000;
        int endret_refusjon = 10_000;
        LocalDate endringsdato = fødselsdato.plusMonths(1);
        HashMap<LocalDate, BigDecimal> endringRefusjonMap = new HashMap<>();
        endringRefusjonMap.put(endringsdato, BigDecimal.valueOf(endret_refusjon));
        LocalDate startDatoForeldrepenger = fødselsdato.minusWeeks(3);
        String fnr = testscenario.getPersonopplysninger().getSøkerIdent();
        InntektsmeldingBuilder inntektsmeldingBuilder = lagInntektsmeldingBuilderMedEndringIRefusjonPrivatArbeidsgiver(inntektPerMaaned, fnr,
                startDatoForeldrepenger, arbeidsgiverFnr, Optional.empty(), Optional.of(BigDecimal.valueOf(refusjon)), endringRefusjonMap);
        fordel.sendInnInntektsmelding(inntektsmeldingBuilder, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

        debugLoggBehandling(saksbehandler.valgtBehandling);

        // Bekreft Opptjening
        saksbehandler.hentAksjonspunktbekreftelse(VurderPerioderOpptjeningBekreftelse.class)
                .godkjennAllOpptjening();
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderPerioderOpptjeningBekreftelse.class);

        //Verifiser Beregningsgrunnlag
        verifiserLikhet(saksbehandler.valgtBehandling.getBeregningsgrunnlag().antallAktivitetStatus(), 1);//ikke sikker på denne
        verifiserLikhet(saksbehandler.valgtBehandling.getBeregningsgrunnlag().getAktivitetStatus(0).kode, "AT");
        verifiserLikhet(saksbehandler.valgtBehandling.getBeregningsgrunnlag().antallBeregningsgrunnlagPeriodeDto(), 2);
        List<BeregningsgrunnlagPrStatusOgAndelDto> andelerFørstePeriode = saksbehandler.valgtBehandling.getBeregningsgrunnlag()
                .getBeregningsgrunnlagPeriode(0).getBeregningsgrunnlagPrStatusOgAndel();
        verifiserLikhet(andelerFørstePeriode.size(), 1);
        verifiserLikhet(andelerFørstePeriode.get(0).getAktivitetStatus().kode, "AT");

        // Bekreft inntekt i beregning
        saksbehandler.hentAksjonspunktbekreftelse(VurderBeregnetInntektsAvvikBekreftelse.class)
                .leggTilInntekt(overstyrtInntekt, 1L)
                .setBegrunnelse("Begrunnelse");
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderBeregnetInntektsAvvikBekreftelse.class);

        // Assert refusjon
        BeregningsresultatPeriode[] resultatPerioder = saksbehandler.valgtBehandling.getBeregningResultatForeldrepenger().getPerioder();
        assertThat(resultatPerioder.length).isEqualTo(4);
        BigDecimal forventetDagsats = BigDecimal.valueOf(overstyrtInntekt).divide(BigDecimal.valueOf(260), RoundingMode.HALF_EVEN);
        assertThat(resultatPerioder[0].getAndeler()[0].getRefusjon()).isEqualTo(forventetDagsats.intValue());
        assertThat(resultatPerioder[1].getAndeler()[0].getRefusjon()).isEqualTo(forventetDagsats.intValue());
        BigDecimal forventetRefusjon = BigDecimal.valueOf(endret_refusjon*12).divide(BigDecimal.valueOf(260), RoundingMode.HALF_EVEN);
        assertThat(resultatPerioder[2].getAndeler()[0].getRefusjon()).isEqualTo(forventetRefusjon.intValue());
        assertThat(resultatPerioder[3].getAndeler()[0].getRefusjon()).isEqualTo(forventetRefusjon.intValue());

        // Foreslå vedtak
        saksbehandler.hentAksjonspunktbekreftelse(ForesloVedtakBekreftelse.class);
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        // Totrinnskontroll
        beslutter.erLoggetInnMedRolle(Aktoer.Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);
        Aksjonspunkt ap = beslutter.hentAksjonspunkt(AksjonspunktKoder.FASTSETT_BEREGNINGSGRUNNLAG_ARBEIDSTAKER_FRILANS);
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkter(Collections.singletonList(ap));
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();


        verifiserLikhet(beslutter.valgtBehandling.hentBehandlingsresultat(), "INNVILGET");
        verifiserLikhet(beslutter.getBehandlingsstatus(), "AVSLU");
        verifiser(beslutter.harHistorikkinnslag(HistorikkInnslag.BREV_SENDT));
        verifiserUttak(1, beslutter.valgtBehandling.hentUttaksperioder());
    }

    @Test
    @DisplayName("Far søker fødsel med aleneomsorg men er gift og bor med annenpart")
    public void farSøkerFødselAleneomsorgMenErGiftOgBorMedAnnenpart() throws Exception {

        TestscenarioDto testscenario = opprettScenario("62");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakFarAleneomsorg(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, fødselsdato);

        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        //fikk aleneomsorg aksjonspunkt siden far er gift og bor på sammested med ektefelle
        //saksbehandler bekreftet at han har ikke aleneomsorg
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        AvklarFaktaAleneomsorgBekreftelse bekreftelse = saksbehandler.hentAksjonspunktbekreftelse(AvklarFaktaAleneomsorgBekreftelse.class);
        bekreftelse.bekreftBrukerHarIkkeAleneomsorg();
        saksbehandler.bekreftAksjonspunktbekreftelserer(bekreftelse);
        saksbehandler.hentAksjonspunktbekreftelse(AvklarFaktaUttakBekreftelse.AvklarFaktaUttakPerioder.class)
                //20 uker fra erketype
                .delvisGodkjennPeriode(fødselsdato, fødselsdato.plusWeeks(20), fødselsdato, fødselsdato.plusWeeks(20),
                        hentKodeverk().UttakPeriodeVurderingType.getKode("PERIODE_KAN_IKKE_AVKLARES"));
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaUttakBekreftelse.AvklarFaktaUttakPerioder.class);

        saksbehandler.ventTilAksjonspunkt(AksjonspunktKoder.FASTSETT_UTTAKPERIODER);
        //verifiserer uttak
        List<UttakResultatPeriode> uttaksperioder = saksbehandler.valgtBehandling.hentUttaksperioder();
        assertThat(uttaksperioder).hasSize(2);

        //uttak går til manuell behandling
        UttakResultatPeriode foreldrepengerFørste6Ukene = uttaksperioder.get(0);
        assertThat(foreldrepengerFørste6Ukene.getPeriodeResultatType().kode).isEqualTo("MANUELL_BEHANDLING");
        assertThat(foreldrepengerFørste6Ukene.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER);
        UttakResultatPeriode foreldrepengerEtterUke6 = uttaksperioder.get(1);
        assertThat(foreldrepengerEtterUke6.getPeriodeResultatType().kode).isEqualTo("MANUELL_BEHANDLING");
        assertThat(foreldrepengerEtterUke6.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER);

        verifiser(saksbehandler.valgtBehandling.getSaldoer().getStonadskontoer().size() == 4, "Antall stønadskontoer er feil.");
    }

    @Test
    @DisplayName("Mor søker fødsel har stillingsprosent 0")
    @Description("Mor søker fødsel har stillingsprosent 0 som fører til aksjonspunkt for opptjening")
    public void morSøkerFødselStillingsprosent0() throws Exception { //TODO
        TestscenarioDto testscenario = opprettScenario("45");

        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);
        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);

        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, fpStartdato);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

        saksbehandler.hentAksjonspunktbekreftelse(VurderPerioderOpptjeningBekreftelse.class)
            .godkjennAllOpptjening();
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderPerioderOpptjeningBekreftelse.class);

        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);

        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.VURDER_PERIODER_MED_OPPTJENING));

        beslutter.fattVedtakOgVentTilAvsluttetBehandling();
    }

    @Test
    @DisplayName("Mor søker gradering og utsettelse. Med to arbeidsforhold. Uten avvikende inntektsmelding")
    @Description("Mor, med to arbeidsforhold, søker gradering og utsettelse. Samsvar med IM.")
    public void morSøkerGraderingOgUtsettelseMedToArbeidsforhold_utenAvvikendeInntektsmeldinger() throws Exception {

        TestscenarioDto testscenario = opprettScenario("76");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();

        Fordeling fordeling = new ObjectFactory().createFordeling();
        fordeling.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
        LocalDate startdatoForeldrePenger = fødselsdato.minusWeeks(3);
        perioder.add(FordelingErketyper.uttaksperiode(FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, startdatoForeldrePenger, fødselsdato.minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato, fødselsdato.plusWeeks(10)));
        String gradetArbeidsgiver = "910909088";
        LocalDate graderingFom = fødselsdato.plusWeeks(10).plusDays(1);
        LocalDate graderingTom = fødselsdato.plusWeeks(12);
        BigDecimal arbeidstidsprosent = BigDecimal.TEN;
        perioder.add(FordelingErketyper.graderingPeriode(FordelingErketyper.STØNADSKONTOTYPE_FELLESPERIODE, graderingFom, graderingTom, gradetArbeidsgiver,
                arbeidstidsprosent));
        LocalDate utsettelseFom = fødselsdato.plusWeeks(12).plusDays(1);
        LocalDate utsettelseTom = fødselsdato.plusWeeks(14);
        perioder.add(FordelingErketyper.utsettelsePeriode(FordelingErketyper.UTSETTELSETYPE_ARBEID, utsettelseFom, utsettelseTom));
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fordeling, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);

        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, startdatoForeldrePenger);

        for (InntektsmeldingBuilder im : inntektsmeldinger) {
            im.addUtsettelseperiode(FordelingErketyper.UTSETTELSETYPE_ARBEID, utsettelseFom, utsettelseTom);
            if (im.getArbeidsgiver().getVirksomhetsnummer().equals(gradetArbeidsgiver)) {
                im.addGradertperiode(arbeidstidsprosent, graderingFom, graderingTom);
            }
        }

        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);
        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        debugLoggBehandling(saksbehandler.valgtBehandling);
        //hackForÅKommeForbiØkonomi(saksnummer);

        //verifiserer uttak
        List<UttakResultatPeriode> uttaksperioder = saksbehandler.valgtBehandling.hentUttaksperioder();
        assertThat(uttaksperioder).hasSize(5);
        for (UttakResultatPeriode periode : uttaksperioder) {
            assertThat(periode.getPeriodeResultatType().kode).isEqualTo("INNVILGET");
            assertThat(periode.getPeriodeResultatÅrsak().kode).isNotEqualTo("-");
            assertThat(periode.getAktiviteter()).hasSize(2);
            for (UttakResultatPeriodeAktivitet aktivitet : periode.getAktiviteter()) {
                assertThat(aktivitet.getArbeidsgiver().getVirksomhet()).isTrue();
                assertThat(aktivitet.getArbeidsgiver().getAktørId()).isNull();
                assertThat(aktivitet.getArbeidsgiver().getNavn()).isNotNull();
                assertThat(aktivitet.getArbeidsgiver().getNavn()).isNotEmpty();
                assertThat(aktivitet.getUttakArbeidType().kode).isEqualTo("ORDINÆRT_ARBEID");
                List<Arbeidsforhold> arbeidsforholdFraScenario = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold();
                assertThat(aktivitet.getArbeidsgiver().getIdentifikator()).isIn(arbeidsforholdFraScenario.get(0).getArbeidsgiverOrgnr(),
                        arbeidsforholdFraScenario.get(1).getArbeidsgiverOrgnr());
            }
        }
        UttakResultatPeriode fpff = uttaksperioder.get(0);
        assertThat(fpff.getAktiviteter().get(0).getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(fpff.getAktiviteter().get(0).getTrekkdagerDesimaler()).isGreaterThan(BigDecimal.ZERO);
        assertThat(fpff.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL);
        assertThat(fpff.getAktiviteter().get(1).getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(fpff.getAktiviteter().get(1).getTrekkdagerDesimaler()).isGreaterThan(BigDecimal.ZERO);
        assertThat(fpff.getAktiviteter().get(1).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL);
        UttakResultatPeriode mødrekvoteFørste6Ukene = uttaksperioder.get(1);
        assertThat(mødrekvoteFørste6Ukene.getAktiviteter().get(0).getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(mødrekvoteFørste6Ukene.getAktiviteter().get(0).getTrekkdagerDesimaler()).isGreaterThan(BigDecimal.ZERO);
        assertThat(mødrekvoteFørste6Ukene.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE);
        assertThat(mødrekvoteFørste6Ukene.getAktiviteter().get(1).getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(mødrekvoteFørste6Ukene.getAktiviteter().get(1).getTrekkdagerDesimaler()).isGreaterThan(BigDecimal.ZERO);
        assertThat(mødrekvoteFørste6Ukene.getAktiviteter().get(1).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE);
        UttakResultatPeriode mødrekvoteEtterUke6 = uttaksperioder.get(2);
        assertThat(mødrekvoteEtterUke6.getAktiviteter().get(0).getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(mødrekvoteEtterUke6.getAktiviteter().get(0).getTrekkdagerDesimaler()).isGreaterThan(BigDecimal.ZERO);
        assertThat(mødrekvoteEtterUke6.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE);
        assertThat(mødrekvoteEtterUke6.getAktiviteter().get(1).getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(mødrekvoteEtterUke6.getAktiviteter().get(1).getTrekkdagerDesimaler()).isGreaterThan(BigDecimal.ZERO);
        assertThat(mødrekvoteEtterUke6.getAktiviteter().get(1).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE);
        UttakResultatPeriode gradering = uttaksperioder.get(3);
        assertThat(gradering.getGraderingAvslagÅrsak().kode).isEqualTo("-");
        assertThat(gradering.getGraderingInnvilget()).isTrue();
        assertThat(gradering.getGradertArbeidsprosent()).isEqualTo(arbeidstidsprosent);
        assertThat(gradering.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_FELLESPERIODE);
        assertThat(gradering.getAktiviteter().get(1).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_FELLESPERIODE);
        UttakResultatPeriodeAktivitet gradertAktivitet = finnAktivitetForArbeidsgiver(gradering, gradetArbeidsgiver);
        assertThat(gradertAktivitet.getTrekkdagerDesimaler()).isGreaterThan(BigDecimal.ZERO);
        assertThat(gradertAktivitet.getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100).subtract(arbeidstidsprosent));
        assertThat(gradertAktivitet.getProsentArbeid()).isEqualTo(arbeidstidsprosent);
        UttakResultatPeriode utsettelse = uttaksperioder.get(4);
        assertThat(utsettelse.getUtsettelseType().kode).isEqualTo(UTSETTELSETYPE_ARBEID);
        assertThat(utsettelse.getAktiviteter().get(0).getUtbetalingsgrad()).isEqualTo(BigDecimal.ZERO);
        assertThat(utsettelse.getAktiviteter().get(0).getTrekkdagerDesimaler()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(utsettelse.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE);
        assertThat(utsettelse.getAktiviteter().get(1).getUtbetalingsgrad()).isEqualTo(BigDecimal.ZERO);
        assertThat(utsettelse.getAktiviteter().get(1).getTrekkdagerDesimaler()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(utsettelse.getAktiviteter().get(1).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE);

        verifiser(saksbehandler.valgtBehandling.getSaldoer().getStonadskontoer().size() == 4, "Feil i antall stønadskontoer, skal være 4.");
        verifiser(saksbehandler.valgtBehandling.getSaldoer().getStonadskontoer().get("FORELDREPENGER_FØR_FØDSEL").getSaldo() >= 0, "FPFF skal ikke være i minus.");
        verifiser(saksbehandler.valgtBehandling.getSaldoer().getStonadskontoer().get("FELLESPERIODE").getSaldo() >= 0, "Fellerperiode skal ikke være i minus.");
        verifiser(saksbehandler.valgtBehandling.getSaldoer().getStonadskontoer().get("MØDREKVOTE").getSaldo() >= 0, "Mødrekvote skal ikke være i minus");

        verifiserLikhet(saksbehandler.valgtBehandling.hentBehandlingsresultat(), "INNVILGET");
        verifiserLikhet(saksbehandler.getBehandlingsstatus(), "AVSLU");
        debugLoggHistorikkinnslag(saksbehandler.getHistorikkInnslag());
        saksbehandler.ventTilHistorikkinnslag(HistorikkInnslag.BREV_SENDT);
    }

    @Test
    @DisplayName("Mor søker fødsel med aleneomsorg")
    @Description("Mor søker fødsel aleneomsorg. Annen forelder ikke kjent.")
    public void morSøkerFødselAleneomsorgKunEnHarRett() throws Exception {

        TestscenarioDto testscenario = opprettScenario("102");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakMorAleneomsorg(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        LocalDate startdatoForeldrePenger = fødselsdato.minusWeeks(3);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, startdatoForeldrePenger);

        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);
        debugLoggBehandling(saksbehandler.valgtBehandling);
        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        //saksbehandler.ventTilØkonomioppdragFerdigstilles();

        //verifiserer uttak
        List<UttakResultatPeriode> uttaksperioder = saksbehandler.valgtBehandling.hentUttaksperioder();
        assertThat(uttaksperioder).hasSize(4);

        UttakResultatPeriode fpff = uttaksperioder.get(0);
        assertThat(fpff.getPeriodeResultatType().kode).isEqualTo("INNVILGET");
        assertThat(fpff.getAktiviteter().get(0).getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(fpff.getAktiviteter().get(0).getTrekkdagerDesimaler()).isGreaterThan(BigDecimal.ZERO);
        assertThat(fpff.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL);
        UttakResultatPeriode foreldrepengerFørste6Ukene = uttaksperioder.get(1);
        assertThat(foreldrepengerFørste6Ukene.getPeriodeResultatType().kode).isEqualTo("INNVILGET");
        assertThat(foreldrepengerFørste6Ukene.getAktiviteter().get(0).getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(foreldrepengerFørste6Ukene.getAktiviteter().get(0).getTrekkdagerDesimaler()).isGreaterThan(BigDecimal.ZERO);
        assertThat(foreldrepengerFørste6Ukene.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER);
        UttakResultatPeriode foreldrepengerEtterUke6 = uttaksperioder.get(2);
        assertThat(foreldrepengerFørste6Ukene.getPeriodeResultatType().kode).isEqualTo("INNVILGET");
        assertThat(foreldrepengerEtterUke6.getAktiviteter().get(0).getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(foreldrepengerEtterUke6.getAktiviteter().get(0).getTrekkdagerDesimaler()).isEqualByComparingTo(BigDecimal.valueOf(200));
        assertThat(foreldrepengerEtterUke6.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER);
        //Periode søkt mer enn 49 uker er avslått automatisk
        UttakResultatPeriode periodeMerEnn49Uker = uttaksperioder.get(3);
        assertThat(periodeMerEnn49Uker.getPeriodeResultatType().kode).isEqualTo("AVSLÅTT");
        assertThat(periodeMerEnn49Uker.getPeriodeResultatÅrsak().kode).isEqualTo("4002");
        assertThat(periodeMerEnn49Uker.getAktiviteter().get(0).getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(0));
        assertThat(periodeMerEnn49Uker.getAktiviteter().get(0).getTrekkdagerDesimaler()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(periodeMerEnn49Uker.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER);

        verifiser(saksbehandler.valgtBehandling.getSaldoer().getStonadskontoer().size() == 2, "Feil antall stønadskontoer");
        verifiser(saksbehandler.valgtBehandling.getSaldoer().getStonadskontoer().get("FORELDREPENGER_FØR_FØDSEL").getSaldo() >= 0, "FPFF skal ikke være minus.");
        verifiser(saksbehandler.valgtBehandling.getSaldoer().getStonadskontoer().get("FORELDREPENGER").getSaldo() >= 0, "Foreldrepenger skal ikke være i minus.");

        verifiserLikhet(saksbehandler.valgtBehandling.hentBehandlingsresultat(), "INNVILGET");
        verifiserLikhet(saksbehandler.getBehandlingsstatus(), "AVSLU");
        verifiser(saksbehandler.harHistorikkinnslag(HistorikkInnslag.BREV_SENDT));
    }
    
    @Test
    @DisplayName("Mor søker fødsel for 2 barn med 1 barn registrert")
    @Description("Mor søker fødsel for 2 barn med 1 barn registrert. dette fører til aksjonspunkt for bekreftelse av antall barn")
    public void morSøker2Barn1Registrert() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate startDatoForeldrepenger = fødselsdato.minusWeeks(3);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorForeldrepengerFlereBarn(søkerAktørIdent, fødselsdato, 2);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, startDatoForeldrepenger);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        
        saksbehandler.hentAksjonspunktbekreftelse(VurderManglendeFodselBekreftelse.class)
            .bekreftDokumentasjonForeligger(2, fødselsdato);
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderManglendeFodselBekreftelse.class);
        
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);

        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.SJEKK_MANGLENDE_FØDSEL));
        beslutter.fattVedtakOgVentTilAvsluttetBehandling();
    }
    
    @Test
    @DisplayName("Mor søker uregistrert fødsel før det har gått 2 uker")
    @Description("Mor søker uregistrert fødsel før det har gått 2 uker - skal sette behandling på vent")
    public void morSøkerUregistrertEtterFør2Uker() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = LocalDate.now().minusDays(5);
        LocalDate startDatoForeldrepenger = fødselsdato.minusWeeks(3);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, startDatoForeldrepenger);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        
        verifiser(saksbehandler.valgtBehandling.erSattPåVent(), "Behandlingen er ikke satt på vent selv om behandlingen ikke har ventet til 2. uke");
        System.out.println(saksbehandler.valgtBehandling.fristBehandlingPaaVent);
        System.out.println(fødselsdato.plusWeeks(2));
        verifiser(saksbehandler.valgtBehandling.fristBehandlingPaaVent.equals(fødselsdato.plusWeeks(2)), "Behandlingen er satt på vent for lenge");
    }
    
    @Test
    @DisplayName("Mor sender inntektsmelding inn etter behandlet behandling men før foreslå vedtak")
    @Description("Mor sender inntektsmelding inn etter behandlet behandling men før foreslå vedtak - behandling starter på nytt")
    public void morSenderInntektsmeldingEtterInnvilgetMenFørVedtak() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = LocalDate.now().minusWeeks(3);
        LocalDate startDatoForeldrepenger = fødselsdato.minusWeeks(3);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, startDatoForeldrepenger);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        
        saksbehandler.hentAksjonspunktbekreftelse(VurderManglendeFodselBekreftelse.class)
            .bekreftDokumentasjonForeligger(2, fødselsdato);
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderManglendeFodselBekreftelse.class);
        
        inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, startDatoForeldrepenger);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);
        
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilHistorikkinnslag(HistorikkInnslag.BEH_OPPDATERT_NYE_OPPL);
    }

    @Test
    @DisplayName("Utsettelse av forskjellige årsaker")
    @Description("Mor søker fødsel med mange utsettelseperioder. Hensikten er å sjekke at alle årsaker fungerer. Kun arbeid " +
            "(og ferie) skal oppgis i IM. Verifiserer på 0 trekkdager for perioder med utsettelse. Kun perioder som krever " +
            "dokumentasjon skal bli manuelt behandlet i fakta om uttak. Ingen AP i uttak.")
    public void utsettelse_med_avvik() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");

        String søkerIdent = testscenario.getPersonopplysninger().getSøkerIdent();
        String søkerAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødsel = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødsel.minusWeeks(3);

        Fordeling fordeling = new Fordeling();
        fordeling.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, fpStartdato, fødsel.minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødsel, fødsel.plusWeeks(6).minusDays(1)));
        perioder.add(FordelingErketyper.utsettelsesperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødsel.plusWeeks(6), fødsel.plusWeeks(9).minusDays(1), UTSETTELSETYPE_INNLAGTBARN, true));
        perioder.add(FordelingErketyper.utsettelsesperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødsel.plusWeeks(9), fødsel.plusWeeks(12).minusDays(1), UTSETTELSETYPE_INNLAGTSØKER, true));
        perioder.add(FordelingErketyper.utsettelsesperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødsel.plusWeeks(12), fødsel.plusWeeks(15).minusDays(1), UTSETTELSETYPE_SYKDOMSKADE, true));
        perioder.add(FordelingErketyper.utsettelsesperiode(STØNADSKONTOTYPE_FELLESPERIODE, fødsel.plusWeeks(15), fødsel.plusWeeks(18).minusDays(1), UTSETTELSETYPE_ARBEID, true));
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FELLESPERIODE, fødsel.plusWeeks(18), fødsel.plusWeeks(21).minusDays(1)));

        // sender inn søknad
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørId, fordeling, fødsel);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, fpStartdato);
        inntektsmeldinger.get(0).addUtsettelseperiode(UTSETTELSETYPE_ARBEID, fødsel.plusWeeks(15), fødsel.plusWeeks(18).minusDays(1));
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        Kode godkjenningskode = saksbehandler.kodeverk.UttakPeriodeVurderingType.getKode("PERIODE_OK");
        saksbehandler.hentAksjonspunktbekreftelse(AvklarFaktaUttakBekreftelse.AvklarFaktaUttakPerioder.class)
                .godkjennPeriode(fødsel.plusWeeks(6), fødsel.plusWeeks(9).minusDays(1), godkjenningskode)
                .godkjennPeriode(fødsel.plusWeeks(9), fødsel.plusWeeks(12).minusDays(1), godkjenningskode)
                .godkjennPeriode(fødsel.plusWeeks(12), fødsel.plusWeeks(15).minusDays(1), godkjenningskode);
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaUttakBekreftelse.AvklarFaktaUttakPerioder.class);
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);
        Aksjonspunkt ap = beslutter.hentAksjonspunkt(AksjonspunktKoder.AVKLAR_FAKTA_UTTAK);
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(ap);
        beslutter.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);

        verifiser(beslutter.valgtBehandling.hentUttaksperioder().size() == 7, "Feil antall uttaksperioder, skal være 7");
        verifiser(beslutter.valgtBehandling.hentUttaksperiode(2).getAktiviteter().get(0).getTrekkdagerDesimaler().compareTo(BigDecimal.ZERO) == 0, "Feil i antall trekkdager, skal være 0");
        verifiser(beslutter.valgtBehandling.hentUttaksperiode(3).getAktiviteter().get(0).getTrekkdagerDesimaler().compareTo(BigDecimal.ZERO) == 0, "Feil i antall trekkdager, skal være 0");
        verifiser(beslutter.valgtBehandling.hentUttaksperiode(4).getAktiviteter().get(0).getTrekkdagerDesimaler().compareTo(BigDecimal.ZERO) == 0, "Feil i antall trekkdager, skal være 0");
        verifiser(beslutter.valgtBehandling.hentUttaksperiode(5).getAktiviteter().get(0).getTrekkdagerDesimaler().compareTo(BigDecimal.ZERO) == 0, "Feil i antall trekkdager, skal være 0");

    }

    
    private UttakResultatPeriodeAktivitet finnAktivitetForArbeidsgiver(UttakResultatPeriode uttakResultatPeriode, String identifikator) {
        return uttakResultatPeriode.getAktiviteter().stream().filter(a -> a.getArbeidsgiver().getIdentifikator().equals(identifikator)).findFirst().get();
    }

    private void verifiserUttak(int antallAktiviteter, List<UttakResultatPeriode> perioder) {
        assertThat(perioder).hasSize(3);
        verifiserUttaksperiode(perioder.get(0), STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, antallAktiviteter);
        verifiserUttaksperiode(perioder.get(1), STØNADSKONTOTYPE_MØDREKVOTE, antallAktiviteter);
        verifiserUttaksperiode(perioder.get(2), STØNADSKONTOTYPE_MØDREKVOTE, antallAktiviteter);
    }

    private void verifiserUttaksperiode(UttakResultatPeriode uttakResultatPeriode, String stønadskontotype, int antallAktiviteter) {
        assertThat(uttakResultatPeriode.getPeriodeResultatType().kode).isEqualTo("INNVILGET");
        assertThat(uttakResultatPeriode.getUtsettelseType().kode).isEqualTo("-");
        assertThat(uttakResultatPeriode.getAktiviteter()).hasSize(antallAktiviteter);
        for (UttakResultatPeriodeAktivitet aktivitet : uttakResultatPeriode.getAktiviteter()) {
            assertThat(aktivitet.getStønadskontoType().kode).isEqualTo(stønadskontotype);
            assertThat(aktivitet.getTrekkdagerDesimaler()).isGreaterThan(BigDecimal.ZERO);
            assertThat(aktivitet.getUtbetalingsgrad()).isGreaterThan(BigDecimal.ZERO);
        }
    }

    private void verifiserTilkjentYtelse(BeregningsresultatMedUttaksplan beregningResultatForeldrepenger, boolean medFullRefusjon) {
        BeregningsresultatPeriode[] perioder = beregningResultatForeldrepenger.getPerioder();
        assertThat(beregningResultatForeldrepenger.isSokerErMor()).isTrue();
        assertThat(perioder.length).isGreaterThan(0);
        for (var periode : perioder) {
            assertThat(periode.getDagsats()).isGreaterThan(0);
            BeregningsresultatPeriodeAndel[] andeler = periode.getAndeler();
            for (var andel : andeler) {
                String kode = andel.getAktivitetStatus().kode;
                if (kode.equals("AT")) {
                    if (medFullRefusjon) {
                        assertThat(andel.getTilSoker()).isZero();
                        assertThat(andel.getRefusjon()).isGreaterThan(0);
                    } else {
                        assertThat(andel.getTilSoker()).isGreaterThan(0);
                        assertThat(andel.getRefusjon()).isZero();
                    }
                } else if (kode.equals("FL") || kode.equals("SN")) {
                    assertThat(andel.getTilSoker()).isGreaterThan(0);
                    assertThat(andel.getRefusjon()).isZero();
                }
                assertThat(andel.getUttak().isGradering()).isFalse();
                assertThat(andel.getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100));
            }
        }
    }
}
