package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import static no.nav.foreldrepenger.autotest.util.AllureHelper.debugLoggHistorikkinnslag;
import static no.nav.foreldrepenger.autotest.util.AllureHelper.debugLoggBehandling;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.UTSETTELSETYPE_ARBEID;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.qameta.allure.Description;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FastsettBruttoBeregningsgrunnlagSNBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FastsettUttaksperioderManueltBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FatterVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.ForesloVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderBeregnetInntektsAvvikBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderFaktaOmBeregningBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderPerioderOpptjeningBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderVarigEndringEllerNyoppstartetSNBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaAleneomsorgBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaUttakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.papirsoknad.PapirSoknadForeldrepengerBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Aksjonspunkt;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.ArbeidstakerandelUtenIMMottarYtelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.BeregningsresultatMedUttaksplan;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.BeregningsresultatPeriode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.BeregningsresultatPeriodeAndel;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.FaktaOmBeregningTilfelle;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.beregningsgrunnlag.BeregningsgrunnlagPrStatusOgAndelDto;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.papirsøknad.FordelingDto;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.papirsøknad.PermisjonPeriodeDto;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak.UttakResultatPeriode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak.UttakResultatPeriodeAktivitet;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arbeidsforhold.Arbeidsforhold;

@Tag("smoke")
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
                orgNr, Optional.empty(), Optional.of(refusjon));
        
        fordel.sendInnInntektsmelding(inntektsmeldingBuilder, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilHistorikkinnslag("Vedlegg mottatt");

        saksbehandler.hentAksjonspunktbekreftelse(VurderPerioderOpptjeningBekreftelse.class)
                .godkjennAllOpptjening();
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderPerioderOpptjeningBekreftelse.class);

        debugLoggBehandling(saksbehandler.valgtBehandling);
        saksbehandler.hentAksjonspunktbekreftelse(VurderFaktaOmBeregningBekreftelse.class)
                .leggTilFaktaOmBeregningTilfeller(FaktaOmBeregningTilfelle.VURDER_MOTTAR_YTELSE.kode)
                .leggTilMottarYtelse(false, Collections.emptyList());
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderFaktaOmBeregningBekreftelse.class);
        
        //Verifiser Beregningsgrunnlag
        verifiserLikhet(saksbehandler.valgtBehandling.beregningsgrunnlag.antallAktivitetStatus(), 1);//ikke sikker på denne
        verifiserLikhet(saksbehandler.valgtBehandling.beregningsgrunnlag.getAktivitetStatus(0).kode, "AT_FL");
        verifiserLikhet(saksbehandler.valgtBehandling.beregningsgrunnlag.antallBeregningsgrunnlagPeriodeDto(), 1);
        List<BeregningsgrunnlagPrStatusOgAndelDto> andeler = saksbehandler.valgtBehandling.beregningsgrunnlag.getBeregningsgrunnlagPeriode(0).getBeregningsgrunnlagPrStatusOgAndel();
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
        verifiser(!saksbehandler.harAksjonspunkt(AksjonspunktKoder.FASTSETT_BEREGNINGSGRUNNLAG_ARBEIDSTAKER_FRILANS));

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
        beslutter.fattVedtakOgGodkjennØkonomioppdrag();


        verifiserLikhet(beslutter.valgtBehandling.hentBehandlingsresultat(), "Innvilget");
        verifiserLikhet(beslutter.getBehandlingsstatus(), "AVSLU");
        verifiser(beslutter.harHistorikkinnslag("Brev sendt"));
        verifiserUttak(2, beslutter.valgtBehandling.hentUttaksperioder());
        verifiserTilkjentYtelse(beslutter.valgtBehandling.beregningResultatForeldrepenger, true);

    }
    
    @Test
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
        verifiserLikhet(saksbehandler.valgtBehandling.beregningsgrunnlag.getSkjaeringstidspunktBeregning(), fpStartdato);
        
        
        
        
        saksbehandler.hentAksjonspunktbekreftelse(ForesloVedtakBekreftelse.class);
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Aktoer.Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);

        Aksjonspunkt ap1 = beslutter.hentAksjonspunkt(AksjonspunktKoder.VURDER_VARIG_ENDRET_ELLER_NYOPPSTARTET_NÆRING_SELVSTENDIG_NÆRINGSDRIVENDE);
        Aksjonspunkt ap2 = beslutter.hentAksjonspunkt(AksjonspunktKoder.FASTSETT_BEREGNINGSGRUNNLAG_SELVSTENDIG_NÆRINGSDRIVENDE);
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkter(Arrays.asList(ap1, ap2));
        beslutter.fattVedtakOgGodkjennØkonomioppdrag();

        verifiserLikhet(beslutter.valgtBehandling.hentBehandlingsresultat(), "Innvilget");
        verifiserLikhet(beslutter.getBehandlingsstatus(), "AVSLU");
        verifiser(beslutter.harHistorikkinnslag("Brev sendt"));
        verifiserUttak(1, beslutter.valgtBehandling.hentUttaksperioder());
        verifiserTilkjentYtelse(beslutter.valgtBehandling.beregningResultatForeldrepenger, false);

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
        InntektsmeldingBuilder inntektsmeldingBuilder_1 = lagInntektsmeldingBuilder(inntektPrMåned, fnr, fpStartdato, orgNr_1, arbeidsforholdId_1, refusjon);
        InntektsmeldingBuilder inntektsmeldingBuilder_2 = lagInntektsmeldingBuilder(inntektPrMåned, fnr, fpStartdato, orgNr_2, arbeidsforholdId_2, refusjon);
        
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
        beslutter.fattVedtakOgGodkjennØkonomioppdrag();

        verifiserLikhet(beslutter.valgtBehandling.hentBehandlingsresultat(), "Innvilget");
        verifiserLikhet(beslutter.getBehandlingsstatus(), "AVSLU");
        verifiser(beslutter.harHistorikkinnslag("Brev sendt"));
        verifiserUttak(2, beslutter.valgtBehandling.hentUttaksperioder());
        verifiserTilkjentYtelse(beslutter.valgtBehandling.beregningResultatForeldrepenger, true);

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

        InntektsmeldingBuilder inntektsmeldingBuilder = lagInntektsmeldingBuilder(inntektPrMåned, fnr, fpStartdato, orgNr, Optional.empty(), Optional.of(refusjon));
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
        beslutter.fattVedtakOgGodkjennØkonomioppdrag();

        verifiserLikhet(beslutter.valgtBehandling.hentBehandlingsresultat(), "Innvilget");
        verifiserLikhet(beslutter.getBehandlingsstatus(), "AVSLU");
        verifiser(beslutter.harHistorikkinnslag("Brev sendt"));
        verifiserUttak(1, beslutter.valgtBehandling.hentUttaksperioder());
        verifiserTilkjentYtelse(beslutter.valgtBehandling.beregningResultatForeldrepenger, true);

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
                fpStartdato, arbeidsgiverOrgnr_1, Optional.of(arbeidsforhold_1.getArbeidsforholdId()), Optional.empty());
        InntektsmeldingBuilder inntektsmeldingBuilder_2 = lagInntektsmeldingBuilder(inntekter.get(1), fnr,
                fpStartdato, arbeidsgiverOrgnr_2, Optional.of(arbeidsforhold_2.getArbeidsforholdId()), Optional.empty());

        fordel.sendInnInntektsmeldinger(Arrays.asList(inntektsmeldingBuilder_1, inntektsmeldingBuilder_2), testscenario, saksnummer);

        debugLoggBehandling(saksbehandler.valgtBehandling);
        
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilØkonomioppdragFerdigstilles();

        debugLoggBehandling(saksbehandler.valgtBehandling);
        verifiserLikhet(saksbehandler.valgtBehandling.hentBehandlingsresultat(), "Innvilget");
        verifiserLikhet(saksbehandler.getBehandlingsstatus(), "AVSLU");
        verifiser(saksbehandler.harHistorikkinnslag("Brev sendt"));
        verifiserUttak(2, saksbehandler.valgtBehandling.hentUttaksperioder());
        verifiserTilkjentYtelse(saksbehandler.valgtBehandling.beregningResultatForeldrepenger, false);

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
        saksbehandler.ventTilØkonomioppdragFerdigstilles();

        debugLoggBehandling(saksbehandler.valgtBehandling);
        verifiserLikhet(saksbehandler.valgtBehandling.hentBehandlingsresultat(), "Innvilget");
        verifiserLikhet(saksbehandler.getBehandlingsstatus(), "AVSLU");
        verifiser(saksbehandler.harHistorikkinnslag("Brev sendt"));
        verifiserUttak(1, saksbehandler.valgtBehandling.hentUttaksperioder());
        verifiserTilkjentYtelse(saksbehandler.valgtBehandling.beregningResultatForeldrepenger, false);
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
        saksbehandler.ventTilØkonomioppdragFerdigstilles();

        verifiserLikhet(saksbehandler.valgtBehandling.hentBehandlingsresultat(), "Innvilget");
        verifiserLikhet(saksbehandler.getBehandlingsstatus(), "AVSLU");
        verifiser(saksbehandler.harHistorikkinnslag("Brev sendt"));
        verifiserUttak(2, saksbehandler.valgtBehandling.hentUttaksperioder());
        verifiserTilkjentYtelse(saksbehandler.valgtBehandling.beregningResultatForeldrepenger, false);
    }

    @Test
    @DisplayName("Far søker fødsel med 1 arbeidsforhold")
    public void farSøkerFødselMedEttArbeidsforhold() throws Exception {
        TestscenarioDto testscenario = opprettScenario("60");

        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate startDatoForeldrepenger = fødselsdato.plusWeeks(3);
        
        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunFar(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
                
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, startDatoForeldrepenger);
        
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

        saksbehandler.hentAksjonspunktbekreftelse(AvklarFaktaUttakBekreftelse.class).
                godkjennPeriode(startDatoForeldrepenger, startDatoForeldrepenger.plusWeeks(2),
                        saksbehandler.kodeverk.UttakPeriodeVurderingType.getKode("PERIODE_OK"));
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaUttakBekreftelse.class);

        List<UttakResultatPeriode> perioder = saksbehandler.valgtBehandling.hentUttaksperioder();
        verifiserLikhet(perioder.size(), 1);

        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);

        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
            .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.AVKLAR_FAKTA_UTTAK));
        
        beslutter.fattVedtakOgGodkjennØkonomioppdrag();
        
        verifiser(beslutter.harHistorikkinnslag("Vedtak fattet"), "behandling har ikke historikkinslag 'Vedtak fattet'");
        verifiser(beslutter.harHistorikkinnslag("Brev sendt"), "behandling har ikke historikkinslag 'Brev sendt'");

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
                fpStartdato, orgnr, Optional.empty(), Optional.empty());
        
        fordel.sendInnInntektsmelding(inntektsmelding, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.fattVedtakOgGodkjennØkonomioppdrag();

        verifiserLikhet(saksbehandler.valgtBehandling.hentBehandlingsresultat(), "Innvilget");
        verifiserLikhet(saksbehandler.getBehandlingsstatus(), "AVSLU");
        verifiser(saksbehandler.harHistorikkinnslag("Brev sendt"));
        verifiserUttak(1, saksbehandler.valgtBehandling.hentUttaksperioder());
        verifiserTilkjentYtelse(saksbehandler.valgtBehandling.beregningResultatForeldrepenger, false);

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
        saksbehandler.ventTilØkonomioppdragFerdigstilles();

        //verifiserer uttak
        List<UttakResultatPeriode> perioder = saksbehandler.valgtBehandling.hentUttaksperioder();
        assertThat(perioder).hasSize(3);
        verifiserUttaksperiode(perioder.get(0), STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, 1);
        verifiserUttaksperiode(perioder.get(1), STØNADSKONTOTYPE_MØDREKVOTE, 1);
        verifiserUttaksperiode(perioder.get(2), STØNADSKONTOTYPE_MØDREKVOTE, 1);
    }

    @Disabled("Disabler til bug fikset i fpsak")
    @Test
    @DisplayName("Far søker fødsel med aleneomsorg men er gift og bor med annenpart")
    public void farSøkerFødselAleneomsorgMenErGiftOgBorMedAnnenpart() throws Exception {

        TestscenarioDto testscenario = opprettScenario("60");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakFarAleneomsorg(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        LocalDate startdatoForeldrePenger = fødselsdato;
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, startdatoForeldrePenger);

        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        //fikk aleneomsorg aksjonspunkt siden far er gift og bor på sammested med ektefelle
        //saksbehandler bekreftet at han har ikke aleneomsorg
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        AvklarFaktaAleneomsorgBekreftelse bekreftelse = saksbehandler.hentAksjonspunktbekreftelse(AvklarFaktaAleneomsorgBekreftelse.class);
        bekreftelse.bekreftBrukerHarIkkeAleneomsorg();
        saksbehandler.bekreftAksjonspunktbekreftelserer(bekreftelse);

        //verifiserer uttak
        List<UttakResultatPeriode> uttaksperioder = saksbehandler.valgtBehandling.hentUttaksperioder();
        assertThat(uttaksperioder).hasSize(2);

        //uttak gå til manuell behandling
        UttakResultatPeriode foreldrepengerFørste6Ukene = uttaksperioder.get(0);
        assertThat(foreldrepengerFørste6Ukene.getPeriodeResultatType().kode).isEqualTo("MANUELL_BEHANDLING");
        assertThat(foreldrepengerFørste6Ukene.getAktiviteter().get(0).getUtbetalingsgrad()).isNull();
        assertThat(foreldrepengerFørste6Ukene.getAktiviteter().get(0).getTrekkdager()).isEqualTo(0);
        assertThat(foreldrepengerFørste6Ukene.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER);
        UttakResultatPeriode foreldrepengerEtterUke6 = uttaksperioder.get(1);
        assertThat(foreldrepengerEtterUke6.getPeriodeResultatType().kode).isEqualTo("MANUELL_BEHANDLING");
        assertThat(foreldrepengerEtterUke6.getAktiviteter().get(0).getUtbetalingsgrad()).isNull();
        assertThat(foreldrepengerEtterUke6.getAktiviteter().get(0).getTrekkdager()).isGreaterThan(0);
        assertThat(foreldrepengerEtterUke6.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER);

        //saksbehandler avslått alle perioder
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        FastsettUttaksperioderManueltBekreftelse uttaksperioderManueltBekreftelse = saksbehandler.hentAksjonspunktbekreftelse(FastsettUttaksperioderManueltBekreftelse.class);
        for(UttakResultatPeriode periode : uttaksperioder) {
            uttaksperioderManueltBekreftelse.avvisPeriode(periode.getFom(), periode.getTom(), 0);
        }
        saksbehandler.bekreftAksjonspunktbekreftelserer(uttaksperioderManueltBekreftelse);

        //verifiserer uttak
        uttaksperioder = saksbehandler.valgtBehandling.hentUttaksperioder();
        assertThat(uttaksperioder).hasSize(2);

        //Alle periodene er avslått
        foreldrepengerFørste6Ukene = uttaksperioder.get(0);
        assertThat(foreldrepengerFørste6Ukene.getPeriodeResultatType().kode).isEqualTo("AVSLÅTT");
        assertThat(foreldrepengerFørste6Ukene.getAktiviteter().get(0).getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(0));
        assertThat(foreldrepengerFørste6Ukene.getAktiviteter().get(0).getTrekkdager()).isEqualTo(0);
        foreldrepengerEtterUke6 = uttaksperioder.get(1);
        assertThat(foreldrepengerEtterUke6.getPeriodeResultatType().kode).isEqualTo("AVSLÅTT");
        assertThat(foreldrepengerEtterUke6.getAktiviteter().get(0).getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(0));
        assertThat(foreldrepengerEtterUke6.getAktiviteter().get(0).getTrekkdager()).isEqualTo(0);
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
            assertThat(aktivitet.getTrekkdager()).isGreaterThan(0);
            assertThat(aktivitet.getUtbetalingsgrad()).isGreaterThan(BigDecimal.ZERO);
        }
    }

    private void verifiserTilkjentYtelse(BeregningsresultatMedUttaksplan beregningResultatForeldrepenger, boolean medFullRefusjon){
        BeregningsresultatPeriode[] perioder = beregningResultatForeldrepenger.getPerioder();
        assertThat(beregningResultatForeldrepenger.isSokerErMor()).isTrue();
        assertThat(perioder.length).isGreaterThan(0);
        for (var periode : perioder) {
            assertThat(periode.getDagsats()).isGreaterThan(0);
            BeregningsresultatPeriodeAndel[] andeler = periode.getAndeler();
            for (var andel : andeler){
                String kode = andel.getAktivitetStatus().kode;
                if (kode.equals("AT")){
                    if (medFullRefusjon){
                        assertThat(andel.getTilSoker()).isZero();
                        assertThat(andel.getRefusjon()).isGreaterThan(0);
                    } else {
                        assertThat(andel.getTilSoker()).isGreaterThan(0);
                        assertThat(andel.getRefusjon()).isZero();
                    }
                } else if (kode.equals("FL") || kode.equals("SN")){
                    assertThat(andel.getTilSoker()).isGreaterThan(0);
                    assertThat(andel.getRefusjon()).isZero();
                }
                assertThat(andel.getUttak().isGradering()).isFalse();
                assertThat(andel.getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100));
            }
        }
    }
}
