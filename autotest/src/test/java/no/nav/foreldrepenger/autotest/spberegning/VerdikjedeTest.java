package no.nav.foreldrepenger.autotest.spberegning;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.qameta.allure.Description;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.base.SpberegningTestBase;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.BeregningKlient;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning.BeregningDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arbeidsforhold.Arbeidsforhold;
import no.nav.inntektsmelding.xml.kodeliste._20180702.BegrunnelseIngenEllerRedusertUtbetalingKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.NaturalytelseKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.YtelseKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.ÅrsakInnsendingKodeliste;
import no.seres.xsd.nav.inntektsmelding_m._20181211.NaturalytelseDetaljer;
import no.seres.xsd.nav.inntektsmelding_m._20181211.Omsorgspenger;
import no.seres.xsd.nav.inntektsmelding_m._20181211.Periode;
import no.seres.xsd.nav.inntektsmelding_m._20181211.PleiepengerPeriodeListe;

@Tag("spberegning")
public class VerdikjedeTest extends SpberegningTestBase {

    @Test
    @DisplayName("Tema FOR: Motorvei for foreldrepenger ")
    @Description("Hensikten med testen er å validere at status og skjæringstidspunkt blir automatisk fastsatt, samt at beregning kjøres autoamtisk for Tema FOR. Motorvei vil si et arbeidsforhold og en inntektsmelding, men ingen frilansforhold.")
    public void ForMotorveiOver25Avvik() throws Exception {

        //Lag testscenario
        TestscenarioDto testscenario = opprettScenario("110");
        int inntektsmeldingMånedsbeløp = 57000;

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String Tema = "FOR";
        String saksnummer = fordel.opprettSak(testscenario, Tema);

        //Oppretter inntektsmelding for ytelsen foreldrepenger
        InntektsmeldingBuilder inntektsmeldingsBuilder = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløp, testscenario.getPersonopplysninger().getSøkerIdent(), "979191139", "ARB001-002", YtelseKodeliste.FORELDREPENGER, ÅrsakInnsendingKodeliste.NY)
                .setStartdatoForeldrepengeperiodenFOM(LocalDate.of(2018, 10, 5));

        //Inntektsmelding journalføres i Joark
        fordel.journalførInnektsmelding(inntektsmeldingsBuilder, testscenario, Long.parseLong(saksnummer));

        //SB åpner beregningsmodulen fra "Gosys" og logger inn
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);

        //Validerer beregning
        verifiserLikhet(saksbehandler.getSkjæringstidspunkt(), LocalDate.of(2018, 10, 5), "Skjæringstidspunkt");
        verifiserLikhet(saksbehandler.beregning.getTema().kode, "FOR", "Beregningstema");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 684000D, "Beregnet årsinntekt inkl naturalytelse");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 9, 30), "TOM dato for innhenting av sammenligningsgrunnlag");
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 444000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 54.05D, "Avvik");
    }

    @Test
    @DisplayName("Tema SYK: Motorvei for sykepenger")
    @Description("Hensikten med testen er å validere at status og skjæringstidspunkt blir automatisk fastsatt, samt at beregning kjøres autoamtisk for Tema SYK. Motorvei vil si et arbeidsforhold og en inntektsmelding, men ingen frilansforhold.")
    public void SykMotorveiOver25Avvik() throws Exception {

        //Lag testscenario
        TestscenarioDto testscenario = opprettScenario("110");
        int inntektsmeldingMånedsbeløp = 57000;

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String Tema = "SYK";
        String saksnummer = fordel.opprettSak(testscenario, Tema);

        //Liste over arbeidsgiverperioder som sendes i inntektsmeldingen
        List<Periode> perioder = new ArrayList<>();
        perioder.add(InntektsmeldingBuilder.createPeriode(LocalDate.of(2018, 10, 5), LocalDate.of(2018, 10, 9)));

        //Oppretter inntektsmelding for ytelsen sykepenger
        InntektsmeldingBuilder inntektsmeldingsBuilder = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløp, testscenario.getPersonopplysninger().getSøkerIdent(), "979191139", "ARB001-002", YtelseKodeliste.SYKEPENGER, ÅrsakInnsendingKodeliste.NY)
                .setSykepengerIArbeidsgiverperioden(InntektsmeldingBuilder.createSykepengerIArbeidsgiverperioden(
                        BigDecimal.valueOf(57000), perioder, BegrunnelseIngenEllerRedusertUtbetalingKodeliste.LOVLIG_FRAVAER));

        //Inntektsmelding journalføres i Joark
        fordel.journalførInnektsmelding(inntektsmeldingsBuilder, testscenario, Long.parseLong(saksnummer));

        //SB åpner beregningsmodulen fra "Gosys" og logger inn
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);

        //Validerer beregning
        verifiserLikhet(saksbehandler.getSkjæringstidspunkt(), LocalDate.of(2018, 10, 5), "Skjæringstidspunkt");
        verifiserLikhet(saksbehandler.beregning.getTema().kode, "SYK", "Beregningstema");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 684000D, "Beregnet årsinntekt inkl naturalytelse");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 9, 30), "TOM dato for innhenting av sammenligningsgrunnlag");
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 444000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 54.05D, "Avvik");
    }

    @Test
    @DisplayName("Tema OMS: Motorvei for Omsorgspenger")
    @Description("Hensikten med testen er å validere at status og skjæringstidspunkt blir automatisk fastsatt, samt at beregning kjøres autoamtisk for Tema OMS. Motorvei vil si et arbeidsforhold og en inntektsmelding, men ingen frilansforhold.")
    public void OmsMotorveiOver25AvvikOmsorgspenger() throws Exception {

        //Lag testscenario
        TestscenarioDto testscenario = opprettScenario("110");
        int inntektsmeldingMånedsbeløp = 57000;

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String Tema = "OMS";
        String saksnummer = fordel.opprettSak(testscenario, Tema);

        //Oppretter inntektsmelding for ytelsen sykepenger
        InntektsmeldingBuilder inntektsmeldingBuilder = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløp, testscenario.getPersonopplysninger().getSøkerIdent(), "979191139", "ARB001-002", YtelseKodeliste.OMSORGSPENGER, ÅrsakInnsendingKodeliste.NY);

        List<Periode> perioder = new ArrayList<Periode>();
        perioder.add(inntektsmeldingBuilder.createInntektsmeldingPeriode(LocalDate.of(2018, 10, 5), LocalDate.of(2018, 10, 12)));
        Omsorgspenger omsorgspenger = inntektsmeldingBuilder.createOmsorgspenger(true);
        inntektsmeldingBuilder.setOmsorgspenger(omsorgspenger);
        inntektsmeldingBuilder.setFravaersPeriodeListeOmsorgspenger(perioder);

        //Inntektsmelding journalføres i Joark
        fordel.journalførInnektsmelding(inntektsmeldingBuilder, testscenario, Long.parseLong(saksnummer));

        //SB åpner beregningsmodulen fra "Gosys" og logger inn
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);

        //Validerer beregning
        verifiserLikhet(saksbehandler.getSkjæringstidspunkt(), LocalDate.of(2018, 10, 5), "Skjæringstidspunkt");
        verifiserLikhet(saksbehandler.beregning.getTema().kode, "OMS", "Beregningstema");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 684000D, "Beregnet årsinntekt");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 9, 30));
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 444000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 54.05D, "Avvik");
    }

    @Test
    @Disabled //TODO YtelseKodeliste må oppdateres til PLEIEPENGER
    @DisplayName("Motorvei for Tema OMS - Pleiepenger")
    @Description("Skjæringstidspunkt og status blir automatisk satt. Oppretter nøkkeloppgave grunnet avvik over 25%")
    public void OmsMotorveiOver25AvvikPleiepenger() throws Exception {

        //Lag testscenario
        TestscenarioDto testscenario = opprettScenario("110");
        int inntektsmeldingMånedsbeløp = 37000;
        BigDecimal inntektsmeldingRefusjon = BigDecimal.valueOf(45000);
        LocalDate refusjonOpphørsdato = LocalDate.now().plusMonths(10);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String Tema = "OMS";
        String saksnummer = fordel.opprettSak(testscenario, Tema);

        //Oppretter inntektsmelding for ytelsen pleiepenger
        InntektsmeldingBuilder inntektsmeldingBuilder = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløp, testscenario.getPersonopplysninger().getSøkerIdent(), "979191139", "ARB001-002", YtelseKodeliste.PLEIEPENGER_BARN, ÅrsakInnsendingKodeliste.NY)
                .setRefusjon(InntektsmeldingBuilder.createRefusjon(inntektsmeldingRefusjon, refusjonOpphørsdato, null));
        List<Periode> perioder = new ArrayList<Periode>();
        perioder.add(inntektsmeldingBuilder.createInntektsmeldingPeriode(LocalDate.of(2018, 10, 5), LocalDate.of(2018, 10, 12)));
        PleiepengerPeriodeListe pleiepengerPeriodeListe = inntektsmeldingBuilder.createPleiepenger(perioder);
        inntektsmeldingBuilder.setPleiepengerPeriodeListe(pleiepengerPeriodeListe);
        inntektsmeldingBuilder.getOpphoerAvNaturalytelsesList().getOpphoerAvNaturalytelse().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(450), LocalDate.of(2018, 10, 5), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));
        inntektsmeldingBuilder.getGjenopptakelseNaturalytelseListe().getNaturalytelseDetaljer().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(450), LocalDate.of(2018, 12, 31), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));

        //Inntektsmelding journalføres i Joark
        fordel.journalførInnektsmelding(inntektsmeldingBuilder, testscenario, Long.parseLong(saksnummer));

        //SB åpner beregningsmodulen fra "Gosys" og logger inn
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        BeregningDto beregning = saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);

        //Validerer beregning
        verifiserLikhet(saksbehandler.getSkjæringstidspunkt(), LocalDate.of(2018, 10, 5), "Skjæringstidspunkt");
        verifiserLikhet(saksbehandler.beregning.getTema().kode, "OMS", "Beregningstema");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 449400D, "Beregnet årsinntekt");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 9, 30));
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 444000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 1.2D, "Avvik");

    }

    @Test
    @DisplayName("Tema FOR: Flere arbeidsforhold og privat arbeidsgiver med IM")
    @Description("Bruker er sjømann, har et arbeidsforhold hos privatperson hvor det er mottatt inntektsmelding uten arbeidsforholdID, samtlige arbeidsforhold opphører etter september. Naturalytelse er medregnet i inntekt. Avviket er over 25% og det skal opprettes nøkkelkontroll oppgave.")
    public void For3AtOver25AvvikPrivatArbeidsforhold() throws Exception {
        //Lag privat arbeidsgiver
        TestscenarioDto arbeidsgiverScenario = opprettScenario("110");
        String arbeidsgiverFnr = arbeidsgiverScenario.getPersonopplysninger().getSøkerIdent();

        //Lag testscenario
        TestscenarioDto testscenario = opprettScenarioMedPrivatArbeidsgiver("111", arbeidsgiverFnr);

        int inntektsmeldingMånedsbeløp = 43000;
        BigDecimal inntektsmeldingRefusjon = BigDecimal.valueOf(43000);
        LocalDate refusjonOpphørsdato = LocalDate.now().plusMonths(10);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String Tema = "FOR";
        String saksnummer = fordel.opprettSak(testscenario, Tema);

        //Oppretter inntektsmelding for ytelsen Foreldrepenger
        InntektsmeldingBuilder inntektsmeldingsBuilder = inntektsmeldingGrunnlagPrivatperson(inntektsmeldingMånedsbeløp, testscenario.getPersonopplysninger().getSøkerIdent(), arbeidsgiverFnr, null, YtelseKodeliste.FORELDREPENGER, ÅrsakInnsendingKodeliste.NY)
                .setRefusjon(InntektsmeldingBuilder.createRefusjon(inntektsmeldingRefusjon, refusjonOpphørsdato, null))
                .setStartdatoForeldrepengeperiodenFOM(LocalDate.of(2018, 9, 15));
                inntektsmeldingsBuilder.setNaaerRelasjon(true);
        inntektsmeldingsBuilder.addGradertperiode(BigDecimal.valueOf(50), LocalDate.of(2018, 12,10), LocalDate.of(2018,12,15));
        inntektsmeldingsBuilder.addUtsettelseperiode("ARBEID", LocalDate.of(2018,12,10),LocalDate.of(2018,12,15));
        inntektsmeldingsBuilder.addUtsettelseperiode("LOVBESTEMT_FERIE", LocalDate.of(2018, 7, 1), LocalDate.of(2018, 7, 30));
        inntektsmeldingsBuilder.getOpphoerAvNaturalytelsesList().getOpphoerAvNaturalytelse().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(450), LocalDate.of(2018, 9, 16), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));
        inntektsmeldingsBuilder.getGjenopptakelseNaturalytelseListe().getNaturalytelseDetaljer().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(450), LocalDate.of(2018, 12, 31), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));

        //Inntektsmelding journalføres i Joark
        fordel.journalførInnektsmelding(inntektsmeldingsBuilder, testscenario, Long.parseLong(saksnummer));

        //SB åpner beregningsmodulen fra "Gosys" og logger inn
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        BeregningDto beregning = saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);

        //SB Verifiserer skjæringstidspunkt og setter status
        verifiserLikhet(saksbehandler.getSkjæringstidspunkt(), LocalDate.of(2018, 9, 15), "Skjæringstidspunkt");
        saksbehandler.oppdaterBeregning(saksbehandler.getSkjæringstidspunkt(), "Arbeidstaker");

        //SB skriver og lagrer notat
        saksbehandler.lagreNotat(beregning, "Hei på deg", beregning.getBeregningsgrunnlag().getId());

        //Validerer beregning
        verifiserLikhet(saksbehandler.beregning.getTema().kode, "FOR", "Forventet Tema");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 756000D, "Forventet beregnet årsinntekt");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 8, 31), "TOM dato for innhenting av sammenligningsgrunnlag");
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 720000D, "Forventet sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 5.0D, "Forventet avvik");
        verifiserLikhet(saksbehandler.getSjømann(), true, "Søker er sjømann");
    }

    @Test
    @DisplayName("Tema SYK: Avsluttet arbeidsforhold")
    @Description("Flere arbeidsforhold, privatperson, sjømann og avsluttet arbeidsforhold")
    public void Syk2AtAvsluttetArbeidsforholdSjømann() throws Exception {
        //Lag privat arbeidsgiver
        TestscenarioDto arbeidsgiverScenario = opprettScenario("110");
        String arbeidsgiverFnr = arbeidsgiverScenario.getPersonopplysninger().getSøkerIdent();

        //Lag testscenario
        TestscenarioDto testscenario = opprettScenarioMedPrivatArbeidsgiver("111", arbeidsgiverFnr);
        int inntektsmeldingMånedsbeløp = 37000;
        BigDecimal inntektsmeldingRefusjon = BigDecimal.valueOf(45000);
        LocalDate refusjonOpphørsdato = LocalDate.now().plusMonths(10);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String Tema = "SYK";
        String saksnummer = fordel.opprettSak(testscenario, Tema);

        //Arbeidsgiverperioder til inntektsmelding
        List<Periode> perioder = new ArrayList<>();
        perioder.add(InntektsmeldingBuilder.createPeriode(LocalDate.of(2018, 10, 5), LocalDate.of(2018, 10, 9)));
        perioder.add(InntektsmeldingBuilder.createPeriode(LocalDate.of(2018, 10, 15), LocalDate.of(2018, 10, 20)));
        perioder.add(InntektsmeldingBuilder.createPeriode(LocalDate.of(2018, 11, 1), LocalDate.of(2018, 11, 9)));

        //Naturalytelser som opphører til inntektsmelding
        List<NaturalytelseDetaljer> opphørNaturalytelseList = Arrays.asList(
                InntektsmeldingBuilder.createNaturalytelseDetaljer(
                        BigDecimal.valueOf(450), LocalDate.of(2018, 10, 5), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON),
                InntektsmeldingBuilder.createNaturalytelseDetaljer(
                        BigDecimal.valueOf(600), LocalDate.of(2018, 10, 18), NaturalytelseKodeliste.FRI_TRANSPORT),
                InntektsmeldingBuilder.createNaturalytelseDetaljer(
                        BigDecimal.valueOf(800), LocalDate.of(2018, 10, 22), NaturalytelseKodeliste.KOST_DAGER
                ));

        //Oppretter inntektsmelding for ytelsen Sykepenger
        InntektsmeldingBuilder inntektsmeldingsBuilder = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløp, testscenario.getPersonopplysninger().getSøkerIdent(), "979191139", "ARB001-002", YtelseKodeliste.SYKEPENGER, ÅrsakInnsendingKodeliste.NY)
                .setRefusjon(InntektsmeldingBuilder.createRefusjon(inntektsmeldingRefusjon, refusjonOpphørsdato, null))
                .setSykepengerIArbeidsgiverperioden(InntektsmeldingBuilder.createSykepengerIArbeidsgiverperioden(
                        BigDecimal.valueOf(0), perioder, BegrunnelseIngenEllerRedusertUtbetalingKodeliste.LOVLIG_FRAVAER));

        List<Periode> feriePerioder = new ArrayList<>();
        feriePerioder.add(InntektsmeldingBuilder.createPeriode(LocalDate.of(2018, 12, 5), LocalDate.of(2018, 12, 9)));
        inntektsmeldingsBuilder.addAvtaltFerie(feriePerioder);

        List<NaturalytelseDetaljer> opphørNaturalYtelseListe = inntektsmeldingsBuilder.getOpphoerAvNaturalytelsesList().getOpphoerAvNaturalytelse();
        opphørNaturalYtelseListe.addAll(opphørNaturalytelseList);
        inntektsmeldingsBuilder.getGjenopptakelseNaturalytelseListe().getNaturalytelseDetaljer().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(800), LocalDate.of(2018, 12, 31), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));

        //Inntektsmelding journalføres i Joark
        fordel.journalførInnektsmelding(inntektsmeldingsBuilder, testscenario, Long.parseLong(saksnummer));

        //SB åpner beregningsmodulen fra "Gosys" og logger inn
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        BeregningDto beregning = saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);
        saksbehandler.lagreNotat(beregning, "Hei på deg", beregning.getBeregningsgrunnlag().getId());

        //SB Verifiserer skjæringstidspunkt og setter status
        verifiserLikhet(saksbehandler.getSkjæringstidspunkt(), LocalDate.of(2018, 11, 1), "Skjæringstidspunkt");
        saksbehandler.oppdaterBeregning(saksbehandler.getSkjæringstidspunkt(), "Kombinert arbeidstaker og frilanser");

        //Validerer beregning
        verifiserLikhet(saksbehandler.beregning.getTema().kode, "SYK", "Beregningstema");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 696600D, "Beregnet årsinntekt");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 10, 31));
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 710000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 1.89D, "Avvik");
        verifiserLikhet(saksbehandler.getSjømann(), true);
    }

    @Test
    @DisplayName("Tema OMS: Flere arbeidsforhold og manuelt fastsatt skjæringstidspunkt")
    @Description("Tester manuell fastsettelse av skjæringstidspunkt og inntektsmelding uten perioder")
    public void OmsATOver25AvvikAvsluttetArbeidsforhold() throws Exception {
        //Lag privat arbeidsgiver
        TestscenarioDto arbeidsgiverScenario = opprettScenario("110");
        String arbeidsgiverFnr = arbeidsgiverScenario.getPersonopplysninger().getSøkerIdent();

        //Lag testscenario
        TestscenarioDto testscenario = opprettScenarioMedPrivatArbeidsgiver("111", arbeidsgiverFnr);
        int inntektsmeldingMånedsbeløp = 37000;
        BigDecimal inntektsmeldingRefusjon = BigDecimal.valueOf(27000);
        LocalDate refusjonOpphørsdato = LocalDate.now().plusMonths(10);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String Tema = "OMS";
        String saksnummer = fordel.opprettSak(testscenario, Tema);


        //Oppretter inntektsmelding for ytelsen Omsorgspenger
        InntektsmeldingBuilder inntektsmeldingsBuilder = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløp, testscenario.getPersonopplysninger().getSøkerIdent(), "979191139", "ARB001-002", YtelseKodeliste.OMSORGSPENGER, ÅrsakInnsendingKodeliste.NY)
                .setRefusjon(InntektsmeldingBuilder.createRefusjon(inntektsmeldingRefusjon, refusjonOpphørsdato, null));
        inntektsmeldingsBuilder.getOpphoerAvNaturalytelsesList().getOpphoerAvNaturalytelse().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(450), LocalDate.of(2018, 10, 6), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));
        inntektsmeldingsBuilder.getGjenopptakelseNaturalytelseListe().getNaturalytelseDetaljer().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(450), LocalDate.of(2018, 12, 31), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));

        //Inntektsmelding journalføres i Joark
        fordel.journalførInnektsmelding(inntektsmeldingsBuilder, testscenario, Long.parseLong(saksnummer));

        //SB åpner beregningsmodulen fra "Gosys" og logger inn
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        BeregningDto beregning = saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);

        //SB setter skjæringstidspunkt og status
        saksbehandler.oppdaterBeregning(LocalDate.of(2018, 11, 30), "Arbeidstaker");

        //Validerer beregning
        verifiserLikhet(saksbehandler.beregning.getTema().kode, "OMS", "Beregningstema");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 444000D, "Beregnet årsinntekt");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 10, 31));
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 710000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 37.46D, "Avvik");
        verifiserLikhet(saksbehandler.getSjømann(), true); //Skal endres til "false" siden arbeidsforholdet bruker er sjømann i er avsluttet
    }

    @Test
    @DisplayName("Tema FOR: Komplekse beregningsregler")
    @Description("Tester komplekse beregningsregler med restpost, flere arbeidsforhold, frilanser og arbeidstaker i samme selskap etc.")
    public void FlereATFLiSammeVirksomhet() throws Exception {
        TestscenarioDto testscenario = opprettScenario("113");
        int inntektsmeldingMånedsbeløpNr1 = 15000;
        int inntektsmeldingMånedsbeløpNr2 = 40000;
        BigDecimal inntektsmeldingRefusjon = BigDecimal.valueOf(45000);
        LocalDate refusjonOpphørsdato = LocalDate.now().plusMonths(10);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String Tema = "FOR";
        String saksnummer = fordel.opprettSak(testscenario, Tema);

        //Oppretter inntektsmelding for arbeidsforhold 1
        InntektsmeldingBuilder inntektsmeldingsBuilder1 = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløpNr1, testscenario.getPersonopplysninger().getSøkerIdent(), "979191138", "ARB001-001", YtelseKodeliste.FORELDREPENGER, ÅrsakInnsendingKodeliste.NY)
                .setRefusjon(InntektsmeldingBuilder.createRefusjon(inntektsmeldingRefusjon, refusjonOpphørsdato, null))
                .setStartdatoForeldrepengeperiodenFOM(LocalDate.of(2018, 10, 6));

        //Oppretter inntektsmelding for arbeidsforhold 2
        InntektsmeldingBuilder inntektsmeldingsBuilder2 = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløpNr2, testscenario.getPersonopplysninger().getSøkerIdent(), "979191139", "ARB001-001", YtelseKodeliste.FORELDREPENGER, ÅrsakInnsendingKodeliste.NY)
                .setRefusjon(InntektsmeldingBuilder.createRefusjon(inntektsmeldingRefusjon, refusjonOpphørsdato, null))
                .setStartdatoForeldrepengeperiodenFOM(LocalDate.of(2018, 10, 6));

        //Inntektsmelding journalføres i Joark
        fordel.journalførInnektsmelding(inntektsmeldingsBuilder1, testscenario, Long.parseLong(saksnummer));
        fordel.journalførInnektsmelding(inntektsmeldingsBuilder2, testscenario, Long.parseLong(saksnummer));

        //SB åpner beregningsmodulen fra "Gosys" og logger inn
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);

        //SB validerer skjæringstidspunkt og setter status
        verifiserLikhet(saksbehandler.getSkjæringstidspunkt(), LocalDate.of(2018, 10, 6), "Skjæringstidspunkt");
        saksbehandler.oppdaterBeregning(saksbehandler.getSkjæringstidspunkt(), "Kombinert arbeidstaker og frilanser");

        //Validerer beregning
        verifiserLikhet(saksbehandler.beregning.getTema().kode, "FOR", "Beregningstema");
        verifiserLikhet(saksbehandler.beregnetÅrsinntekt(), 2160000D, "Sum inntekt");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 2160000D, "Beregnet årsinntekt inkl naturalytelse");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 9, 30));
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 2100000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 2.86D, "Avvik");
    }

    @Test
    @DisplayName("Tema SYK: Inntektsmeldinger uten arbeidsforholdID og mange arbeidsforhold")
    @Description("Det sendes inn to inntektsmeldinger som ikke inneholder arbeidsforhold ID. Testen sjekker korrekt fastsettelse av skjæringstidspunkt, at naturalytelse tas med i beregning når det opphører i en arbeidsgiverperiode, beregning med flere inntektsmeldinger og avvik når frilansinntekt ikke medregnes grunnet valgt status(arbeidstaker) ")
    public void SykKompleksBeregning() throws Exception {
        TestscenarioDto testscenario = opprettScenario("113");
        int inntektsmeldingMånedsbeløpNr1 = 15000;
        int inntektsmeldingMånedsbeløpNr2 = 40000;
        BigDecimal inntektsmeldingRefusjon = BigDecimal.valueOf(45000);
        LocalDate refusjonOpphørsdato = LocalDate.now().plusMonths(10);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String Tema = "SYK";
        String saksnummer = fordel.opprettSak(testscenario, Tema);

        List<Periode> perioderNr1 = new ArrayList<>(); //Arbeidsgiverperiode for inntektsmelding nr 1
        perioderNr1.add(InntektsmeldingBuilder.createPeriode(LocalDate.of(2018, 10, 3), LocalDate.of(2018, 10, 9)));
        perioderNr1.add(InntektsmeldingBuilder.createPeriode(LocalDate.of(2018, 10, 12), LocalDate.of(2018, 10, 20)));
        perioderNr1.add(InntektsmeldingBuilder.createPeriode(LocalDate.of(2018, 10, 25), LocalDate.of(2018, 11, 9)));

        List<Periode> perioderNr2 = new ArrayList<>(); //Arbeidsgiverperiode for inntektsmelding nr 2
        perioderNr1.add(InntektsmeldingBuilder.createPeriode(LocalDate.of(2018, 10, 5), LocalDate.of(2018, 10, 9)));
        perioderNr1.add(InntektsmeldingBuilder.createPeriode(LocalDate.of(2018, 10, 15), LocalDate.of(2018, 10, 20)));
        perioderNr1.add(InntektsmeldingBuilder.createPeriode(LocalDate.of(2018, 11, 1), LocalDate.of(2018, 11, 9)));

        List<NaturalytelseDetaljer> opphørNaturalytelseListe1 = Arrays.asList(
                InntektsmeldingBuilder.createNaturalytelseDetaljer(
                        BigDecimal.valueOf(450), LocalDate.of(2018, 10, 5), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON),
                InntektsmeldingBuilder.createNaturalytelseDetaljer(
                        BigDecimal.valueOf(600), LocalDate.of(2018, 10, 18), NaturalytelseKodeliste.FRI_TRANSPORT),
                InntektsmeldingBuilder.createNaturalytelseDetaljer(
                        BigDecimal.valueOf(800), LocalDate.of(2018, 10, 22), NaturalytelseKodeliste.KOST_DAGER));

        List<NaturalytelseDetaljer> opphørNaturalytelseListe2 = Arrays.asList(
                InntektsmeldingBuilder.createNaturalytelseDetaljer(
                        BigDecimal.valueOf(450), LocalDate.of(2018, 10, 5), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON),
                InntektsmeldingBuilder.createNaturalytelseDetaljer(
                        BigDecimal.valueOf(600), LocalDate.of(2018, 10, 18), NaturalytelseKodeliste.FRI_TRANSPORT),
                InntektsmeldingBuilder.createNaturalytelseDetaljer(
                        BigDecimal.valueOf(800), LocalDate.of(2018, 10, 22), NaturalytelseKodeliste.KOST_DAGER));

        //Innteksmelding nr 1 - Inneholder ikke arbeidsforholdID så dekker derfor samtlige arbeidsforhold i org. 979191139
        InntektsmeldingBuilder inntektsmeldingsBuilder1 = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløpNr1, testscenario.getPersonopplysninger().getSøkerIdent(), "979191138", null, YtelseKodeliste.SYKEPENGER, ÅrsakInnsendingKodeliste.NY)
                .setRefusjon(InntektsmeldingBuilder.createRefusjon(inntektsmeldingRefusjon, refusjonOpphørsdato, null))
                .setSykepengerIArbeidsgiverperioden(InntektsmeldingBuilder.createSykepengerIArbeidsgiverperioden(
                        BigDecimal.valueOf(37000), perioderNr1, BegrunnelseIngenEllerRedusertUtbetalingKodeliste.LOVLIG_FRAVAER));

        List<Periode> feriePerioder1 = new ArrayList<>();
        feriePerioder1.add(InntektsmeldingBuilder.createPeriode(LocalDate.of(2018, 12, 5), LocalDate.of(2018, 12, 9)));
        inntektsmeldingsBuilder1.addAvtaltFerie(feriePerioder1);

        List<NaturalytelseDetaljer> opphørNaturalYtelseListe1 = inntektsmeldingsBuilder1.getOpphoerAvNaturalytelsesList().getOpphoerAvNaturalytelse();
        opphørNaturalYtelseListe1.addAll(opphørNaturalytelseListe1);
        inntektsmeldingsBuilder1.getGjenopptakelseNaturalytelseListe().getNaturalytelseDetaljer().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(800), LocalDate.of(2018, 12, 31), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));

        //Innteksmelding nr 2 - Inneholder ikke arbeidsforholdID så dekker derfor samtlige arbeidsforhold i org. 979191139
        InntektsmeldingBuilder inntektsmeldingsBuilder2 = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløpNr2, testscenario.getPersonopplysninger().getSøkerIdent(), "979191139", null, YtelseKodeliste.SYKEPENGER, ÅrsakInnsendingKodeliste.NY)
                .setRefusjon(InntektsmeldingBuilder.createRefusjon(inntektsmeldingRefusjon, refusjonOpphørsdato, null))
                .setSykepengerIArbeidsgiverperioden(InntektsmeldingBuilder.createSykepengerIArbeidsgiverperioden(
                        BigDecimal.valueOf(0), perioderNr2, null));

        List<Periode> feriePerioder2 = new ArrayList<>();
        feriePerioder2.add(InntektsmeldingBuilder.createPeriode(LocalDate.of(2018, 12, 5), LocalDate.of(2018, 12, 9)));
        inntektsmeldingsBuilder2.addAvtaltFerie(feriePerioder2);

        List<NaturalytelseDetaljer> opphørNaturalYtelseListe2 = inntektsmeldingsBuilder2.getOpphoerAvNaturalytelsesList().getOpphoerAvNaturalytelse();
        opphørNaturalYtelseListe2.addAll(opphørNaturalytelseListe2);
        inntektsmeldingsBuilder2.getGjenopptakelseNaturalytelseListe().getNaturalytelseDetaljer().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(800), LocalDate.of(2018, 12, 31), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));


        //Inntektsmelding journalføres i Joark
        fordel.journalførInnektsmelding(inntektsmeldingsBuilder1, testscenario, Long.parseLong(saksnummer));
        fordel.journalførInnektsmelding(inntektsmeldingsBuilder2, testscenario, Long.parseLong(saksnummer));

        //SB åpner beregningsmodulen fra "Gosys" og logger inn
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);

        //SB validerer skjæringstidspunkt og setter status
        verifiserLikhet(saksbehandler.getSkjæringstidspunkt(), LocalDate.of(2018, 11, 1), "Skjæringstidspunkt");
        saksbehandler.oppdaterBeregning(saksbehandler.getSkjæringstidspunkt(), "Arbeidstaker");

        //Validerer beregning
        verifiserLikhet(saksbehandler.beregning.getTema().kode, "SYK", "Beregningstema");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 1549200D, "Beregnet årsinntekt inkl naturalytelse");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 10, 31));
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 2100000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 26.23D, "Avvik");
    }

    @Test
    @DisplayName("Tema FOR: Svangerskapspenger")
    @Description("Kombinasjon AT/FL, Status arbeidstaker, Full inntektsmelding for Svangerskapspenger")
    public void ForATFLSvangerskapsPenger() throws Exception {
        TestscenarioDto testscenario = opprettScenario("114");
        int inntektsmeldingMånedsbeløp = 37000;
        BigDecimal inntektsmeldingRefusjon = BigDecimal.valueOf(37000);
        LocalDate refusjonOpphørsdato = LocalDate.now().plusMonths(10);


        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String Tema = "FOR";
        String saksnummer = fordel.opprettSak(testscenario, Tema);

        //Oppretter inntektsmelding for yteslsen Svangerskapspenger
        InntektsmeldingBuilder inntektsmeldingsBuilder = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløp, testscenario.getPersonopplysninger().getSøkerIdent(), "979191138", "ARB001-001", YtelseKodeliste.SVANGERSKAPSPENGER, ÅrsakInnsendingKodeliste.NY)
                .setRefusjon(InntektsmeldingBuilder.createRefusjon(inntektsmeldingRefusjon, refusjonOpphørsdato, null))
                .setFoersfravaersdag(LocalDate.of(2018, 10, 6));
        List<Periode> feriePerioder = new ArrayList<>();
        feriePerioder.add(InntektsmeldingBuilder.createPeriode(LocalDate.of(2018, 12, 5), LocalDate.of(2018, 12, 9)));
        inntektsmeldingsBuilder.addAvtaltFerie(feriePerioder);
        inntektsmeldingsBuilder.getOpphoerAvNaturalytelsesList().getOpphoerAvNaturalytelse().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(450), LocalDate.of(2018, 10, 5), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));
        inntektsmeldingsBuilder.getGjenopptakelseNaturalytelseListe().getNaturalytelseDetaljer().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(450), LocalDate.of(2018, 12, 31), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));

        //Inntektsmelding journalføres i Joark
        fordel.journalførInnektsmelding(inntektsmeldingsBuilder, testscenario, Long.parseLong(saksnummer));

        //SB åpner beregningsmodulen fra "Gosys" og logger inn
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);

        //SB setter skjæringstidspunkt og status
        saksbehandler.oppdaterBeregning(LocalDate.of(2018, 10, 5), "Arbeidstaker");

        //Validerer beregning
        verifiserLikhet(saksbehandler.beregning.getTema().kode, "FOR", "Beregningstema");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 449400D, "Beregnet årsinntekt inkl naturalytelse");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 9, 30));
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 600000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 25.1D, "Avvik");
    }

    @Test
    @DisplayName("Tema FOR: Kombinasjon arbeidstaker og frilanser")
    @Description("Kombinasjon AT/FL, Inntektsmelding inkl gradering og utsettelse")
    public void ForATFLUnder25Avvik() throws Exception {
        TestscenarioDto testscenario = opprettScenario("114");
        int inntektsmeldingMånedsbeløp = 20000;
        BigDecimal inntektsmeldingRefusjon = BigDecimal.valueOf(45000);
        LocalDate refusjonOpphørsdato = LocalDate.now().plusMonths(10);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String Tema = "FOR";
        String saksnummer = fordel.opprettSak(testscenario, Tema);


        InntektsmeldingBuilder inntektsmeldingsBuilder = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløp, testscenario.getPersonopplysninger().getSøkerIdent(), "979191138", "ARB001-001", YtelseKodeliste.FORELDREPENGER, ÅrsakInnsendingKodeliste.NY)
                .setRefusjon(InntektsmeldingBuilder.createRefusjon(inntektsmeldingRefusjon, refusjonOpphørsdato, null))
                .setStartdatoForeldrepengeperiodenFOM(LocalDate.of(2018, 10, 6));
                inntektsmeldingsBuilder.addGradertperiode(BigDecimal.valueOf(60), LocalDate.of(2018, 12,10), LocalDate.of(2018,12,15));
                inntektsmeldingsBuilder.addUtsettelseperiode("ARBEID", LocalDate.of(2019,1,10),LocalDate.of(2019,1,15));
                inntektsmeldingsBuilder.addUtsettelseperiode("LOVBESTEMT_FERIE", LocalDate.of(2019,4,10),LocalDate.of(2019,4,15));
                inntektsmeldingsBuilder.getOpphoerAvNaturalytelsesList().getOpphoerAvNaturalytelse().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                    BigDecimal.valueOf(450), LocalDate.of(2018, 10, 5), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));
                inntektsmeldingsBuilder.getGjenopptakelseNaturalytelseListe().getNaturalytelseDetaljer().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                    BigDecimal.valueOf(450), LocalDate.of(2018, 12, 31), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));

        fordel.journalførInnektsmelding(inntektsmeldingsBuilder, testscenario, Long.parseLong(saksnummer));

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);

        verifiserLikhet(saksbehandler.getSkjæringstidspunkt(), LocalDate.of(2018, 10, 6), "Skjæringstidspunkt");
        saksbehandler.oppdaterBeregning(saksbehandler.getSkjæringstidspunkt(), "Kombinert arbeidstaker og frilanser");

        verifiserLikhet(saksbehandler.beregning.getTema().kode, "FOR", "Beregningstema");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 360000D, "Beregnet årsinntekt inkl naturalytelse");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 9, 30));
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 600000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 40D, "Avvik");
    }

    @Test
    @DisplayName("Tema SYK: Kombinasjon arbeidstaker og frilanser hos privat arbeidsgiver/oppdragsgiver")
    @Description("Privat arbeidsgiver ligger ikke i Aareg, men har sendt inn IM med arbeidsforholdID. Status settes til Arbeidstaker selv om det foreligger frilansinntekt. Dette tas med i sammenligningsgrunnlaget, men skal ikke med i beregnet årsinntekt")
    public void SykATFLPrivatPerson() throws Exception {
        //Lag privat arbeidsgiver
        TestscenarioDto arbeidsgiverScenario = opprettScenario("110");
        String arbeidsgiverFnr = arbeidsgiverScenario.getPersonopplysninger().getSøkerIdent();

        //Lag testscenario
        TestscenarioDto testscenario = opprettScenarioMedPrivatArbeidsgiver("116", arbeidsgiverFnr);
        int inntektsmeldingMånedsbeløp = 20000;
        BigDecimal inntektsmeldingRefusjon = BigDecimal.valueOf(45000);
        LocalDate refusjonOpphørsdato = LocalDate.now().plusMonths(10);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String Tema = "SYK";
        String saksnummer = fordel.opprettSak(testscenario, Tema);

        List<Periode> perioder = new ArrayList<>();
        perioder.add(InntektsmeldingBuilder.createPeriode(LocalDate.of(2018, 10, 5), LocalDate.of(2018, 10, 9)));

        //Oppretter inntektsmelding for yteslsen Sykepenger
        InntektsmeldingBuilder inntektsmeldingsBuilder = inntektsmeldingGrunnlagPrivatperson(inntektsmeldingMånedsbeløp, testscenario.getPersonopplysninger().getSøkerIdent(),arbeidsgiverFnr, "ARB001-003", YtelseKodeliste.SYKEPENGER, ÅrsakInnsendingKodeliste.NY)
                .setRefusjon(InntektsmeldingBuilder.createRefusjon(inntektsmeldingRefusjon, refusjonOpphørsdato, null))
                .setSykepengerIArbeidsgiverperioden(InntektsmeldingBuilder.createSykepengerIArbeidsgiverperioden(
                        BigDecimal.valueOf(20000), perioder, BegrunnelseIngenEllerRedusertUtbetalingKodeliste.LOVLIG_FRAVAER));

        //Inntektsmelding journalføres i Joark
        fordel.journalførInnektsmelding(inntektsmeldingsBuilder, testscenario, Long.parseLong(saksnummer));

        //SB åpner beregningsmodulen fra "Gosys" og logger inn
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);

        //SB verifiserer skjæringstidspunkt og setter status
        verifiserLikhet(saksbehandler.getSkjæringstidspunkt(), LocalDate.of(2018, 10, 5), "Skjæringstidspunkt");
        saksbehandler.oppdaterBeregning(saksbehandler.getSkjæringstidspunkt(), "Kombinert arbeidstaker og frilanser");

        //Validerer beregning
        verifiserLikhet(saksbehandler.beregning.getTema().kode, "SYK", "Beregningstema");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 720000D, "Beregnet årsinntekt inkl naturalytelse");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 9, 30));
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 480000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 50.0D, "Avvik");
    }


    @Test //TODO Støtter ikke inntektsfilter i VTP. Utvide med støtte for filter 8-28 og 8-30
    @DisplayName("Tema SYK: Kombinasjon arbeidstaker og frilanser")
    @Description("Feriepenger skal ikke med i beregnet inntekt(§8-28), endring i arbeidsforhold, sluttet/startet i samme arbeidsforhold og IM på feil ytelse skal ikke med i beregning")
    public void SykATFLFeriepenger() throws Exception {
        TestscenarioDto testscenario = opprettScenario("114");
        int inntektsmeldingMånedsbeløp = 20000;

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String Tema = "SYK";
        String saksnummer = fordel.opprettSak(testscenario, Tema);

        //Oppretter inntektsmelding for yteslsen Foreldrepenger
        InntektsmeldingBuilder inntektsmeldingsBuilder = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløp, testscenario.getPersonopplysninger().getSøkerIdent(), "979191138", "ARB001-003", YtelseKodeliste.FORELDREPENGER, ÅrsakInnsendingKodeliste.NY)
                .setStartdatoForeldrepengeperiodenFOM(LocalDate.of(2018, 10, 6));

        //Inntektsmelding journalføres i Joark
        fordel.journalførInnektsmelding(inntektsmeldingsBuilder, testscenario, Long.parseLong(saksnummer));

        //SB åpner beregningsmodulen fra "Gosys" og logger inn
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);

        //SB setter skjæringstidspunkt og status
       saksbehandler.oppdaterBeregning(LocalDate.of(2018, 12, 20),  "Kombinert arbeidstaker og frilanser");

        //Validerer beregning
        verifiserLikhet(saksbehandler.beregning.getTema().kode, "SYK", "Beregningstema");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 600000D, "Beregnet årsinntekt inkl naturalytelse");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 11, 30));
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 600000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 0D, "Avvik");
    }

    @Test
    @DisplayName("Tema FOS: Test ved kun frilanserforhold")
    @Description("Bruker har kun inntekt som frilanser. Skjæringstidspunkt og status blir manuelt satt")
    public void FosFLUnder25Avvik() throws Exception {
        TestscenarioDto testscenario = opprettScenario("115");
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String Tema = "FOS";
        String saksnummer = fordel.opprettSak(testscenario, Tema);

        //SB åpner beregningsmodulen fra "Gosys" og logger inn
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);

        //SB setter skjæringstidspunkt og status
        saksbehandler.oppdaterBeregning(LocalDate.of(2018, 10, 5), "Frilanser");

        //Validerer beregning
        verifiserLikhet(saksbehandler.beregning.getTema().kode, "FOS", "Beregningstema");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 120000D, "Beregnet årsinntekt inkl naturalytelse");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 9, 30));
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 120000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 0D, "Avvik");
    }

    @Test
    @DisplayName("Tema SYK: Test bruker med frilansoppdrag hos privatperson")
    @Description("Bruker har kun inntekt som frilanser. Skjæringstidspunkt og status blir manuelt satt")
    public void FosFLPrivatOppdragsgiver() throws Exception {
        //Lag privat arbeidsgiver
        TestscenarioDto arbeidsgiverScenario = opprettScenario("110");
        String arbeidsgiverFnr = arbeidsgiverScenario.getPersonopplysninger().getSøkerIdent();

        //Lag testscenario
        TestscenarioDto testscenario = opprettScenarioMedPrivatArbeidsgiver("116", arbeidsgiverFnr);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String Tema = "SYK";
        String saksnummer = fordel.opprettSak(testscenario, Tema);

        //SB åpner beregningsmodulen fra "Gosys" og logger inn
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);

        //SB setter skjæringstidspunkt og status
        saksbehandler.oppdaterBeregning(LocalDate.of(2018, 10, 5), "Frilanser");

        //Validerer beregning
        verifiserLikhet(saksbehandler.beregning.getTema().kode, "SYK", "Beregningstema");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 120000D, "Beregnet årsinntekt inkl naturalytelse");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 9, 30));
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 480000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 75.0D, "Avvik");
    }
}