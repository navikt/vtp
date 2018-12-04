package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.UTSETTELSETYPE_ARBEID;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FastsettBruttoBeregningsgrunnlagSNBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FatterVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.ForesloVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderBeregnetInntektsAvvikBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderVarigEndringEllerNyoppstartetSNBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaUttakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.papirsoknad.PapirSoknadForeldrepengerBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Aksjonspunkt;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.BeregningsresultatMedUttaksplan;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.BeregningsresultatPeriode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.BeregningsresultatPeriodeAndel;
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
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent.Inntektsperiode;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.Fordeling;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.LukketPeriodeMedVedlegg;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.ObjectFactory;

@Tag("smoke")
@Tag("foreldrepenger")
public class Fodsel extends ForeldrepengerTestBase {

    @Test
    public void morSøkerFødselSomSelvstendingNæringsdrivende_AvvikIBeregning() throws Exception {

        TestscenarioDto testscenario = opprettScenario("48");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorMedEgenNaering(søkerAktørIdent, fpStartdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);

        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

        saksbehandler.hentAksjonspunktbekreftelse(VurderVarigEndringEllerNyoppstartetSNBekreftelse.class)
                .setErVarigEndretNaering(true)
                .setBegrunnelse("Endring eller nyoppstartet begrunnelse");
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderVarigEndringEllerNyoppstartetSNBekreftelse.class);
        saksbehandler.hentAksjonspunktbekreftelse(FastsettBruttoBeregningsgrunnlagSNBekreftelse.class)
                .setBruttoBeregningsgrunnlag(1_000_000)
                .setBegrunnelse("Grunnlag begrunnelse");
        saksbehandler.bekreftAksjonspunktBekreftelse(FastsettBruttoBeregningsgrunnlagSNBekreftelse.class);

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

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fpStartdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        InntektsmeldingBuilder inntektsmeldingBuilder_1 = lagInntektsmeldingBuilder(inntektPrMåned, fnr, fpStartdato, orgNr_1, arbeidsforholdId_1, refusjon);
        InntektsmeldingBuilder inntektsmeldingBuilder_2 = lagInntektsmeldingBuilder(inntektPrMåned, fnr, fpStartdato, orgNr_2, arbeidsforholdId_2, refusjon);
        fordel.sendInnInntektsmeldinger(Arrays.asList(inntektsmeldingBuilder_1, inntektsmeldingBuilder_2), testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

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
    public void morSøkerFødselMedEttArbeidsforhold_AvvikIBeregning() throws Exception {

        TestscenarioDto testscenario = opprettScenario("49");

        String orgNr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        String fnr = testscenario.getPersonopplysninger().getSøkerIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);

        int inntektPrMåned = 15_000;
        BigDecimal refusjon = BigDecimal.valueOf(inntektPrMåned);
        int overstyrtInntekt = inntektPrMåned * 12;

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(testscenario.getPersonopplysninger().getSøkerAktørIdent(), fpStartdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);

        InntektsmeldingBuilder inntektsmeldingBuilder = lagInntektsmeldingBuilder(inntektPrMåned, fnr, fpStartdato, orgNr, Optional.empty(), Optional.of(refusjon));
        fordel.sendInnInntektsmelding(inntektsmeldingBuilder, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

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
    public void morSøkerFødselMedToArbeidsforholdISammeOrganisasjon() throws Exception {

        TestscenarioDto testscenario = opprettScenario("57");

        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);
        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fpStartdato);
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

        hackForÅKommeForbiØkonomi(saksnummer);

        verifiserLikhet(saksbehandler.valgtBehandling.hentBehandlingsresultat(), "Innvilget");
        verifiserLikhet(saksbehandler.getBehandlingsstatus(), "AVSLU");
        verifiser(saksbehandler.harHistorikkinnslag("Brev sendt"));
        verifiserUttak(2, saksbehandler.valgtBehandling.hentUttaksperioder());
        verifiserTilkjentYtelse(saksbehandler.valgtBehandling.beregningResultatForeldrepenger, false);

    }

    @Test
    public void morSøkerFødselMedEttArbeidsforhold() throws Exception {

        TestscenarioDto testscenario = opprettScenario("49");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate startDatoForeldrepenger = fødselsdato.minusWeeks(3);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, startDatoForeldrepenger);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, startDatoForeldrepenger);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        hackForÅKommeForbiØkonomi(saksnummer);

        verifiserLikhet(saksbehandler.valgtBehandling.hentBehandlingsresultat(), "Innvilget");
        verifiserLikhet(saksbehandler.getBehandlingsstatus(), "AVSLU");
        verifiser(saksbehandler.harHistorikkinnslag("Brev sendt"));
        verifiserUttak(1, saksbehandler.valgtBehandling.hentUttaksperioder());
        verifiserTilkjentYtelse(saksbehandler.valgtBehandling.beregningResultatForeldrepenger, false);

    }

    @Test
    public void morSøkerFødselMedToArbeidsforhold() throws Exception {

        TestscenarioDto testscenario = opprettScenario("56");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fpStartdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, fpStartdato);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        hackForÅKommeForbiØkonomi(saksnummer);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

        verifiserLikhet(saksbehandler.valgtBehandling.hentBehandlingsresultat(), "Innvilget");
        verifiserLikhet(saksbehandler.getBehandlingsstatus(), "AVSLU");
        verifiser(saksbehandler.harHistorikkinnslag("Brev sendt"));
        verifiserUttak(2, saksbehandler.valgtBehandling.hentUttaksperioder());
        verifiserTilkjentYtelse(saksbehandler.valgtBehandling.beregningResultatForeldrepenger, false);

    }

    @Test
    public void farSøkerFødselMedEttArbeidsforhold() throws Exception {

        TestscenarioDto testscenario = opprettScenario("60");

        LocalDate startDatoForeldrepenger = testscenario.getPersonopplysninger().getFødselsdato().plusWeeks(3);
        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunFar(søkerAktørIdent, startDatoForeldrepenger);
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
    @Disabled
    public void farOgMorSøkerFødselMedEttArbeidsforhold() throws Exception {
        TestscenarioDto testscenario = opprettScenario("60");
        
        behandleSøknadForMor(testscenario);
        behandleSøknadForFar(testscenario);
    }
    
    public void behandleSøknadForMor(TestscenarioDto testscenario) throws Exception {
        String søkerAktørid = testscenario.getPersonopplysninger().getAnnenpartIdent();
        String annenPartAktørid = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate startDatoForeldrepenger = fødselsdato.minusWeeks(3);
        
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedMorMedFar(søkerAktørid, annenPartAktørid, startDatoForeldrepenger);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, startDatoForeldrepenger);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.ikkeVentPåStatus = true;
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ikkeVentPåStatus = false;
        saksbehandler.ventOgGodkjennØkonomioppdrag();
    }
    
    public void behandleSøknadForFar(TestscenarioDto testscenario) throws Exception {
        String søkerAktørid = testscenario.getPersonopplysninger().getSøkerAktørIdent(); //Aktørid far
        String annenPartAktørid = testscenario.getPersonopplysninger().getAnnenpartIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate startDatoForeldrepenger = fødselsdato.minusWeeks(3);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedFarMedMor(søkerAktørid, annenPartAktørid, startDatoForeldrepenger);
    }

    @Test
    public void morSøkerFødselMedToArbeidsforholdISammeOrganisasjonEnInntektsmelding() throws Exception {

        TestscenarioDto testscenario = opprettScenario("57");

        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);
        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fpStartdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        String fnr = testscenario.getPersonopplysninger().getSøkerIdent();

        List<Integer> inntekter = sorterteInntektsbeløp(testscenario);

        String orgnr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        InntektsmeldingBuilder inntektsmelding = lagInntektsmeldingBuilder(inntekter.get(0) + inntekter.get(1), fnr,
                fpStartdato, orgnr, Optional.empty(), Optional.empty());

        fordel.sendInnInntektsmelding(inntektsmelding, testscenario, saksnummer);

        hackForÅKommeForbiØkonomi(saksnummer);

        verifiserLikhet(saksbehandler.valgtBehandling.hentBehandlingsresultat(), "Innvilget");
        verifiserLikhet(saksbehandler.getBehandlingsstatus(), "AVSLU");
        verifiser(saksbehandler.harHistorikkinnslag("Brev sendt"));
        verifiserUttak(1, saksbehandler.valgtBehandling.hentUttaksperioder());
        verifiserTilkjentYtelse(saksbehandler.valgtBehandling.beregningResultatForeldrepenger, false);

    }

    @Disabled
    @Test
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

        saksbehandler.ikkeVentPåStatus = true;
        saksbehandler.bekreftAksjonspunktBekreftelse(aksjonspunktBekreftelse);
        saksbehandler.ventOgGodkjennØkonomioppdrag();
        saksbehandler.ikkeVentPåStatus = false;

        //verifiserer uttak
        List<UttakResultatPeriode> perioder = saksbehandler.valgtBehandling.hentUttaksperioder();
        assertThat(perioder).hasSize(3);
        verifiserUttaksperiode(perioder.get(0), STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, 1);
        verifiserUttaksperiode(perioder.get(1), STØNADSKONTOTYPE_MØDREKVOTE, 1);
        verifiserUttaksperiode(perioder.get(2), STØNADSKONTOTYPE_MØDREKVOTE, 1);
    }

    @Test
    public void morSøkerGraderingOgUtsettelseMedToArbeidsforhold_utenAvvikendeInntektsmeldinger() throws Exception {

        TestscenarioDto testscenario = opprettScenario("58");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();

        Fordeling fordeling = new ObjectFactory().createFordeling();
        fordeling.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
        LocalDate startdatoForeldrePenger = fødselsdato.minusWeeks(3);
        perioder.add(FordelingErketyper.uttaksperiode(FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, startdatoForeldrePenger, fødselsdato.minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato, fødselsdato.plusWeeks(10)));
        String gradetArbeidsgiver = "979191138";
        LocalDate graderingFom = fødselsdato.plusWeeks(10).plusDays(1);
        LocalDate graderingTom = fødselsdato.plusWeeks(12);
        BigDecimal arbeidstidsprosent = BigDecimal.TEN;
        perioder.add(FordelingErketyper.graderingPeriode(FordelingErketyper.STØNADSKONTOTYPE_FELLESPERIODE, graderingFom, graderingTom, gradetArbeidsgiver,
                arbeidstidsprosent.doubleValue()));
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

        hackForÅKommeForbiØkonomi(saksnummer);

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
        assertThat(fpff.getAktiviteter().get(0).getTrekkdager()).isGreaterThan(0);
        assertThat(fpff.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL);
        assertThat(fpff.getAktiviteter().get(1).getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(fpff.getAktiviteter().get(1).getTrekkdager()).isGreaterThan(0);
        assertThat(fpff.getAktiviteter().get(1).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL);
        UttakResultatPeriode mødrekvoteFørste6Ukene = uttaksperioder.get(1);
        assertThat(mødrekvoteFørste6Ukene.getAktiviteter().get(0).getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(mødrekvoteFørste6Ukene.getAktiviteter().get(0).getTrekkdager()).isGreaterThan(0);
        assertThat(mødrekvoteFørste6Ukene.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE);
        assertThat(mødrekvoteFørste6Ukene.getAktiviteter().get(1).getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(mødrekvoteFørste6Ukene.getAktiviteter().get(1).getTrekkdager()).isGreaterThan(0);
        assertThat(mødrekvoteFørste6Ukene.getAktiviteter().get(1).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE);
        UttakResultatPeriode mødrekvoteEtterUke6 = uttaksperioder.get(2);
        assertThat(mødrekvoteEtterUke6.getAktiviteter().get(0).getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(mødrekvoteEtterUke6.getAktiviteter().get(0).getTrekkdager()).isGreaterThan(0);
        assertThat(mødrekvoteEtterUke6.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE);
        assertThat(mødrekvoteEtterUke6.getAktiviteter().get(1).getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(mødrekvoteEtterUke6.getAktiviteter().get(1).getTrekkdager()).isGreaterThan(0);
        assertThat(mødrekvoteEtterUke6.getAktiviteter().get(1).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE);
        UttakResultatPeriode gradering = uttaksperioder.get(3);
        assertThat(gradering.getGraderingAvslagÅrsak().kode).isEqualTo("-");
        assertThat(gradering.getGraderingInnvilget()).isTrue();
        assertThat(gradering.getGradertArbeidsprosent()).isEqualTo(arbeidstidsprosent);
        assertThat(gradering.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_FELLESPERIODE);
        assertThat(gradering.getAktiviteter().get(1).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_FELLESPERIODE);
        UttakResultatPeriodeAktivitet gradertAktivitet = finnAktivitetForArbeidsgiver(gradering, gradetArbeidsgiver);
        assertThat(gradertAktivitet.getTrekkdager()).isGreaterThan(0);
        assertThat(gradertAktivitet.getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100).subtract(arbeidstidsprosent));
        assertThat(gradertAktivitet.getProsentArbeid()).isEqualTo(arbeidstidsprosent);
        UttakResultatPeriode utsettelse = uttaksperioder.get(4);
        assertThat(utsettelse.getUtsettelseType().kode).isEqualTo(UTSETTELSETYPE_ARBEID);
        assertThat(utsettelse.getAktiviteter().get(0).getUtbetalingsgrad()).isEqualTo(BigDecimal.ZERO);
        assertThat(utsettelse.getAktiviteter().get(0).getTrekkdager()).isEqualTo(0);
        assertThat(utsettelse.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE);
        assertThat(utsettelse.getAktiviteter().get(1).getUtbetalingsgrad()).isEqualTo(BigDecimal.ZERO);
        assertThat(utsettelse.getAktiviteter().get(1).getTrekkdager()).isEqualTo(0);
        assertThat(utsettelse.getAktiviteter().get(1).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE);

        verifiserLikhet(saksbehandler.valgtBehandling.hentBehandlingsresultat(), "Innvilget");
        verifiserLikhet(saksbehandler.getBehandlingsstatus(), "AVSLU");
        verifiser(saksbehandler.harHistorikkinnslag("Brev sendt"));
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
            assertThat(aktivitet.getTrekkdager()).isGreaterThan(0);
            assertThat(aktivitet.getUtbetalingsgrad()).isGreaterThan(BigDecimal.ZERO);
        }
    }

    private void verifiserTilkjentYtelse(BeregningsresultatMedUttaksplan beregningResultatForeldrepenger, boolean medRefusjon){
        BeregningsresultatPeriode[] perioder = beregningResultatForeldrepenger.getPerioder();
        assertThat(beregningResultatForeldrepenger.isSokerErMor()).isTrue();
        assertThat(perioder.length).isGreaterThan(0);
        for (var periode : perioder) {
            assertThat(periode.getDagsats()).isGreaterThan(0);
            BeregningsresultatPeriodeAndel[] andeler = periode.getAndeler();
            for (var andel : andeler){
                if (medRefusjon){
                    assertThat(andel.getTilSoker()).isZero();
                    assertThat(andel.getRefusjon()).isGreaterThan(0);
                } else {
                    assertThat(andel.getTilSoker()).isGreaterThan(0);
                    assertThat(andel.getRefusjon()).isZero();
                }
                assertThat(andel.getUttak().isGradering()).isFalse();
                assertThat(andel.getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100));
            }
        }
    }

    private void hackForÅKommeForbiØkonomi(long saksnummer) throws Exception {
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.ikkeVentPåStatus = true;
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventOgGodkjennØkonomioppdrag();
        saksbehandler.ikkeVentPåStatus = false;
    }

    private List<Integer> sorterteInntektsbeløp(TestscenarioDto testscenario) {
        return testscenario.getScenariodata().getInntektskomponentModell().getInntektsperioder().stream()
                .map(Inntektsperiode::getBeløp)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

}
