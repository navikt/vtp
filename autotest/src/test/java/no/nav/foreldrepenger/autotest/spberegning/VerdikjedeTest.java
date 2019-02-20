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
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.BeregningKlient;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning.BeregningDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
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
    BeregningKlient klient;

    @Test
    @DisplayName("Motorvei for Tema FOR")
    @Description("Skjæringstidspunkt og status blir automatisk satt. Oppretter nøkkeloppgave ved avvik over 25%")
    public void ForMotorveiOver25Avvik() throws Exception {
        TestscenarioDto testscenario = opprettScenario("110");
        int inntektsmeldingMånedsbeløp = 57000;
        BigDecimal inntektsmeldingRefusjon = BigDecimal.valueOf(45000);
        LocalDate refusjonOpphørsdato = LocalDate.now().plusMonths(10);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String Tema = "FOR";
        String saksnummer = fordel.opprettSak(testscenario, Tema);

        InntektsmeldingBuilder inntektsmeldingsBuilder = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløp, testscenario.getPersonopplysninger().getSøkerIdent(), "979191139", "ARB001-002", YtelseKodeliste.FORELDREPENGER, ÅrsakInnsendingKodeliste.NY)
                .setRefusjon(InntektsmeldingBuilder.createRefusjon(inntektsmeldingRefusjon, refusjonOpphørsdato, null))
                .setStartdatoForeldrepengeperiodenFOM(LocalDate.of(2018, 10, 5));
        inntektsmeldingsBuilder.getOpphoerAvNaturalytelsesList().getOpphoerAvNaturalytelse().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(450), LocalDate.of(2018, 10, 5), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));
        inntektsmeldingsBuilder.getGjenopptakelseNaturalytelseListe().getNaturalytelseDetaljer().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(450), LocalDate.of(2018, 12, 31), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));

        fordel.journalførInnektsmelding(inntektsmeldingsBuilder, testscenario, Long.parseLong(saksnummer));

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);

        verifiserLikhet(saksbehandler.getSkjæringstidspunkt(), LocalDate.of(2018, 10, 5), "Skjæringstidspunkt");
        verifiserLikhet(saksbehandler.beregning.getTema().kode, "FOR", "Beregningstema");
        verifiserLikhet(saksbehandler.beregnetÅrsinntekt(), 684000D, "Sum inntekt");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 689400D, "Beregnet årsinntekt inkl naturalytelse");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 9, 30));
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 444000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 55.27D, "Avvik");

    }

    @Test
    @DisplayName("Motorvei for Tema SYK")
    @Description("Skjæringstidspunkt og status blir automatisk satt. Oppretter nøkkeloppgave grunnet avvik over 25%")
    public void SykMotorveiOver25Avvik() throws Exception {
        TestscenarioDto testscenario = opprettScenario("110");
        int inntektsmeldingMånedsbeløp = 57000;
        BigDecimal inntektsmeldingRefusjon = BigDecimal.valueOf(45000);
        LocalDate refusjonOpphørsdato = LocalDate.now().plusMonths(10);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String Tema = "SYK";
        String saksnummer = fordel.opprettSak(testscenario, Tema);

        List<Periode> perioder = new ArrayList<>();
        perioder.add(InntektsmeldingBuilder.createPeriode(LocalDate.of(2018, 10, 5), LocalDate.of(2018, 10, 9)));

        InntektsmeldingBuilder inntektsmeldingsBuilder = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløp, testscenario.getPersonopplysninger().getSøkerIdent(), "979191139", "ARB001-002", YtelseKodeliste.SYKEPENGER, ÅrsakInnsendingKodeliste.NY)
                .setRefusjon(InntektsmeldingBuilder.createRefusjon(inntektsmeldingRefusjon, refusjonOpphørsdato, null))
                .setSykepengerIArbeidsgiverperioden(InntektsmeldingBuilder.createSykepengerIArbeidsgiverperioden(
                        BigDecimal.valueOf(1234567891), perioder, BegrunnelseIngenEllerRedusertUtbetalingKodeliste.LOVLIG_FRAVAER));
        inntektsmeldingsBuilder.getOpphoerAvNaturalytelsesList().getOpphoerAvNaturalytelse().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(450), LocalDate.of(2018, 10, 5), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));
        inntektsmeldingsBuilder.getGjenopptakelseNaturalytelseListe().getNaturalytelseDetaljer().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(450), LocalDate.of(2018, 12, 31), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));

        fordel.journalførInnektsmelding(inntektsmeldingsBuilder, testscenario, Long.parseLong(saksnummer));

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);

        verifiserLikhet(saksbehandler.getSkjæringstidspunkt(), LocalDate.of(2018, 10, 5), "Skjæringstidspunkt");
        verifiserLikhet(saksbehandler.beregning.getTema().kode, "SYK", "Beregningstema");
        verifiserLikhet(saksbehandler.beregnetÅrsinntekt(), 684000D, "Sum inntekt");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 689400D, "Beregnet årsinntekt inkl naturalytelse");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 9, 30));
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 444000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 55.27D, "Avvik");

    }

    @Test
    @DisplayName("Motorvei for Tema OMS - Omsorgspenger")
    @Description("Skjæringstidspunkt og status blir automatisk satt. Oppretter nøkkeloppgave grunnet avvik over 25%")
    public void OmsMotorveiOver25AvvikOmsorgspenger() throws Exception {
        TestscenarioDto testscenario = opprettScenario("110");
        int inntektsmeldingMånedsbeløp = 37000;
        BigDecimal inntektsmeldingRefusjon = BigDecimal.valueOf(45000);
        LocalDate refusjonOpphørsdato = LocalDate.now().plusMonths(10);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String Tema = "OMS";
        String saksnummer = fordel.opprettSak(testscenario, Tema);


        InntektsmeldingBuilder inntektsmeldingBuilder = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløp, testscenario.getPersonopplysninger().getSøkerIdent(), "979191139", "ARB001-002", YtelseKodeliste.OMSORGSPENGER, ÅrsakInnsendingKodeliste.NY)
                .setRefusjon(InntektsmeldingBuilder.createRefusjon(inntektsmeldingRefusjon, refusjonOpphørsdato, null));

        List<Periode> perioder = new ArrayList<Periode>();
        perioder.add(inntektsmeldingBuilder.createInntektsmeldingPeriode(LocalDate.of(2018, 10, 5), LocalDate.of(2018, 10, 12)));

        Omsorgspenger omsorgspenger = inntektsmeldingBuilder.createOmsorgspenger(true);
        inntektsmeldingBuilder.setOmsorgspenger(omsorgspenger);
        inntektsmeldingBuilder.setFravaersPeriodeListeOmsorgspenger(perioder);

        inntektsmeldingBuilder.getOpphoerAvNaturalytelsesList().getOpphoerAvNaturalytelse().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(450), LocalDate.of(2018, 10, 5), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));
        inntektsmeldingBuilder.getGjenopptakelseNaturalytelseListe().getNaturalytelseDetaljer().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(450), LocalDate.of(2018, 12, 31), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));

        System.out.println("Inntektsmelding: " + inntektsmeldingBuilder.createInntektesmeldingXML());
        System.out.println("Saksnummer: " + saksnummer);

        fordel.journalførInnektsmelding(inntektsmeldingBuilder, testscenario, Long.parseLong(saksnummer));

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);

        verifiserLikhet(saksbehandler.getSkjæringstidspunkt(), LocalDate.of(2018, 10, 5), "Skjæringstidspunkt");
        verifiserLikhet(saksbehandler.beregning.getTema().kode, "OMS", "Beregningstema");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 449400D, "Beregnet årsinntekt");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 9, 30));
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 444000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 1.22D, "Avvik");

    }

    @Test
    @Disabled //TODO YtelseKodeliste må oppdateres til PLEIEPENGER
    @DisplayName("Motorvei for Tema OMS - Pleiepenger")
    @Description("Skjæringstidspunkt og status blir automatisk satt. Oppretter nøkkeloppgave grunnet avvik over 25%")
    public void OmsMotorveiOver25AvvikPleiepenger() throws Exception {
        TestscenarioDto testscenario = opprettScenario("110");
        int inntektsmeldingMånedsbeløp = 37000;
        BigDecimal inntektsmeldingRefusjon = BigDecimal.valueOf(45000);
        LocalDate refusjonOpphørsdato = LocalDate.now().plusMonths(10);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String Tema = "OMS";
        String saksnummer = fordel.opprettSak(testscenario, Tema);

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


        System.out.println("Inntektsmelding: " + inntektsmeldingBuilder.createInntektesmeldingXML());
        System.out.println("Saksnummer: " + saksnummer);

        fordel.journalførInnektsmelding(inntektsmeldingBuilder, testscenario, Long.parseLong(saksnummer));

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        BeregningDto beregning = saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);

        verifiserLikhet(saksbehandler.getSkjæringstidspunkt(), LocalDate.of(2018, 10, 5), "Skjæringstidspunkt");
        verifiserLikhet(saksbehandler.beregning.getTema().kode, "OMS", "Beregningstema");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 449400D, "Beregnet årsinntekt");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 9, 30));
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 444000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 1.2D, "Avvik");

    }

    @Test
    @Disabled //TODO VTP mangler støtte for validering av privat arbeidsgiver(TPS mock)
    @DisplayName("Tema FOR: Flere arbeidsforhold")
    @Description("Bruker er sjømann, arbeidsforhold hos privatperson, opphørt arbeidsforhold og naturalytelse medregnet i inntekt. Avvik over 25%")
    public void For3AtOver25AvvikPrivatArbeidsforhold() throws Exception {
        TestscenarioDto testscenario = opprettScenario("111");
        int inntektsmeldingMånedsbeløp = 43000;
        BigDecimal inntektsmeldingRefusjon = BigDecimal.valueOf(43000);
        LocalDate refusjonOpphørsdato = LocalDate.now().plusMonths(10);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String Tema = "FOR";
        String saksnummer = fordel.opprettSak(testscenario, Tema);

        InntektsmeldingBuilder inntektsmeldingsBuilder = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløp, testscenario.getPersonopplysninger().getSøkerIdent(), "979191139", "ARB001-002", YtelseKodeliste.FORELDREPENGER, ÅrsakInnsendingKodeliste.NY)
                .setRefusjon(InntektsmeldingBuilder.createRefusjon(inntektsmeldingRefusjon, refusjonOpphørsdato, null))
                .setStartdatoForeldrepengeperiodenFOM(LocalDate.of(2018, 9, 15));
                inntektsmeldingsBuilder.setNaaerRelasjon(true);
        inntektsmeldingsBuilder.addGradertperiode(BigDecimal.valueOf(50), LocalDate.of(2018, 12,10), LocalDate.of(2018,12,15));
        inntektsmeldingsBuilder.addUtsettelseperiode("ARBEID", LocalDate.of(2018,12,10),LocalDate.of(2018,12,15));

        fordel.journalførInnektsmelding(inntektsmeldingsBuilder, testscenario, Long.parseLong(saksnummer));

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        BeregningDto beregning = saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);

        verifiserLikhet(saksbehandler.getSkjæringstidspunkt(), LocalDate.of(2018, 9, 15), "Skjæringstidspunkt");
        saksbehandler.oppdaterBeregning(saksbehandler.getSkjæringstidspunkt(), "Arbeidstaker");
        saksbehandler.lagreNotat(beregning, "Hei på deg", beregning.getBeregningsgrunnlag().getId());

        verifiserLikhet(saksbehandler.beregning.getTema().kode, "FOR", "Tema");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 876000D, "Beregnet årsinntekt");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 9, 30));
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 720000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 38.3D, "Avvik");
        verifiserLikhet(saksbehandler.getSjømann(), true);
    }

    @Test
    @DisplayName("Tema SYK: Avsluttet arbeidsforhold")
    @Description("Flere arbeidsforhold, privatperson, sjømann og avsluttet arbeidsforhold")
    public void Syk2AtAvsluttetArbeidsforholdSjømann() throws Exception {
        TestscenarioDto testscenario = opprettScenario("111");
        int inntektsmeldingMånedsbeløp = 37000;
        BigDecimal inntektsmeldingRefusjon = BigDecimal.valueOf(45000);
        LocalDate refusjonOpphørsdato = LocalDate.now().plusMonths(10);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String Tema = "SYK";
        String saksnummer = fordel.opprettSak(testscenario, Tema);

        List<Periode> perioder = new ArrayList<>();
        perioder.add(InntektsmeldingBuilder.createPeriode(LocalDate.of(2018, 10, 5), LocalDate.of(2018, 10, 9)));
        perioder.add(InntektsmeldingBuilder.createPeriode(LocalDate.of(2018, 10, 15), LocalDate.of(2018, 10, 20)));
        perioder.add(InntektsmeldingBuilder.createPeriode(LocalDate.of(2018, 11, 1), LocalDate.of(2018, 11, 9)));

        List<NaturalytelseDetaljer> opphørNaturalytelseList = Arrays.asList(
                InntektsmeldingBuilder.createNaturalytelseDetaljer(
                        BigDecimal.valueOf(450), LocalDate.of(2018, 10, 5), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON),
                InntektsmeldingBuilder.createNaturalytelseDetaljer(
                        BigDecimal.valueOf(600), LocalDate.of(2018, 10, 18), NaturalytelseKodeliste.FRI_TRANSPORT),
                InntektsmeldingBuilder.createNaturalytelseDetaljer(
                        BigDecimal.valueOf(800), LocalDate.of(2018, 10, 22), NaturalytelseKodeliste.KOST_DAGER
                ));

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

        fordel.journalførInnektsmelding(inntektsmeldingsBuilder, testscenario, Long.parseLong(saksnummer));

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        BeregningDto beregning = saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);
        saksbehandler.lagreNotat(beregning, "Hei på deg", beregning.getBeregningsgrunnlag().getId());

        verifiserLikhet(saksbehandler.getSkjæringstidspunkt(), LocalDate.of(2018, 11, 1), "Skjæringstidspunkt");
        saksbehandler.oppdaterBeregning(saksbehandler.getSkjæringstidspunkt(), "Kombinert arbeidstaker og frilanser");

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
        TestscenarioDto testscenario = opprettScenario("111");
        int inntektsmeldingMånedsbeløp = 37000;
        BigDecimal inntektsmeldingRefusjon = BigDecimal.valueOf(27000);
        LocalDate refusjonOpphørsdato = LocalDate.now().plusMonths(10);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String Tema = "OMS";
        String saksnummer = fordel.opprettSak(testscenario, Tema);
        List<Arbeidsforhold> arbeidsforhold = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold();

        InntektsmeldingBuilder inntektsmeldingsBuilder = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløp, testscenario.getPersonopplysninger().getSøkerIdent(), "979191139", "ARB001-002", YtelseKodeliste.OMSORGSPENGER, ÅrsakInnsendingKodeliste.NY)
                .setRefusjon(InntektsmeldingBuilder.createRefusjon(inntektsmeldingRefusjon, refusjonOpphørsdato, null));
        inntektsmeldingsBuilder.getOpphoerAvNaturalytelsesList().getOpphoerAvNaturalytelse().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(450), LocalDate.of(2018, 10, 6), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));
        inntektsmeldingsBuilder.getGjenopptakelseNaturalytelseListe().getNaturalytelseDetaljer().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(450), LocalDate.of(2018, 12, 31), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));

        fordel.journalførInnektsmelding(inntektsmeldingsBuilder, testscenario, Long.parseLong(saksnummer));

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        BeregningDto beregning = saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);

        saksbehandler.oppdaterBeregning(LocalDate.of(2018, 11, 30), "Arbeidstaker");
        saksbehandler.lagreNotat(beregning, "Hei på deg", beregning.getBeregningsgrunnlag().getId());

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

        InntektsmeldingBuilder inntektsmeldingsBuilder1 = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløpNr1, testscenario.getPersonopplysninger().getSøkerIdent(), "979191138", "ARB001-001", YtelseKodeliste.FORELDREPENGER, ÅrsakInnsendingKodeliste.NY)
                .setRefusjon(InntektsmeldingBuilder.createRefusjon(inntektsmeldingRefusjon, refusjonOpphørsdato, null))
                .setStartdatoForeldrepengeperiodenFOM(LocalDate.of(2018, 10, 6));

        InntektsmeldingBuilder inntektsmeldingsBuilder2 = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløpNr2, testscenario.getPersonopplysninger().getSøkerIdent(), "979191139", "ARB001-001", YtelseKodeliste.FORELDREPENGER, ÅrsakInnsendingKodeliste.NY)
                .setRefusjon(InntektsmeldingBuilder.createRefusjon(inntektsmeldingRefusjon, refusjonOpphørsdato, null))
                .setStartdatoForeldrepengeperiodenFOM(LocalDate.of(2018, 10, 6));

        fordel.journalførInnektsmelding(inntektsmeldingsBuilder1, testscenario, Long.parseLong(saksnummer));
        fordel.journalførInnektsmelding(inntektsmeldingsBuilder2, testscenario, Long.parseLong(saksnummer));

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);

        verifiserLikhet(saksbehandler.getSkjæringstidspunkt(), LocalDate.of(2018, 10, 6), "Skjæringstidspunkt");
        saksbehandler.oppdaterBeregning(saksbehandler.getSkjæringstidspunkt(), "Kombinert arbeidstaker og frilanser");

        verifiserLikhet(saksbehandler.beregning.getTema().kode, "FOR", "Beregningstema");
        verifiserLikhet(saksbehandler.beregnetÅrsinntekt(), 2160000D, "Sum inntekt");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 2160000D, "Beregnet årsinntekt inkl naturalytelse");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 9, 30));
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 2100000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 2.86D, "Avvik");
    }

    @Test
    @DisplayName("Tema SYK: Komplekse inntektsmeldinger og komplekse beregningsregler")
    @Description("2 forskjellige inntektsmeldinger uten arbeidsforholdID for 2 forskjellige virksomheter")
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
        InntektsmeldingBuilder inntektsmeldingsBuilder2 = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløpNr1, testscenario.getPersonopplysninger().getSøkerIdent(), "979191139", null, YtelseKodeliste.SYKEPENGER, ÅrsakInnsendingKodeliste.NY)
                .setRefusjon(InntektsmeldingBuilder.createRefusjon(inntektsmeldingRefusjon, refusjonOpphørsdato, null))
                .setSykepengerIArbeidsgiverperioden(InntektsmeldingBuilder.createSykepengerIArbeidsgiverperioden(
                        BigDecimal.valueOf(0), perioderNr2, null));

        List<Periode> feriePerioder2 = new ArrayList<>();
        feriePerioder2.add(InntektsmeldingBuilder.createPeriode(LocalDate.of(2018, 12, 5), LocalDate.of(2018, 12, 9)));
        inntektsmeldingsBuilder1.addAvtaltFerie(feriePerioder2);

        List<NaturalytelseDetaljer> opphørNaturalYtelseListe2 = inntektsmeldingsBuilder2.getOpphoerAvNaturalytelsesList().getOpphoerAvNaturalytelse();
        opphørNaturalYtelseListe2.addAll(opphørNaturalytelseListe2);
        inntektsmeldingsBuilder1.getGjenopptakelseNaturalytelseListe().getNaturalytelseDetaljer().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(800), LocalDate.of(2018, 12, 31), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));


        fordel.journalførInnektsmelding(inntektsmeldingsBuilder1, testscenario, Long.parseLong(saksnummer));
        fordel.journalførInnektsmelding(inntektsmeldingsBuilder2, testscenario, Long.parseLong(saksnummer));

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);

        verifiserLikhet(saksbehandler.getSkjæringstidspunkt(), LocalDate.of(2018, 11, 1), "Skjæringstidspunkt");
        saksbehandler.oppdaterBeregning(saksbehandler.getSkjæringstidspunkt(), "Kombinert arbeidstaker og frilanser");

        verifiserLikhet(saksbehandler.beregning.getTema().kode, "SYK", "Beregningstema");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 1885200D, "Beregnet årsinntekt inkl naturalytelse");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 10, 31));
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 2100000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 10.23D, "Avvik");
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

        fordel.journalførInnektsmelding(inntektsmeldingsBuilder, testscenario, Long.parseLong(saksnummer));

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);

        saksbehandler.oppdaterBeregning(LocalDate.of(2018, 10, 5), "Arbeidstaker");

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

    @Test //TODO Støtter ikke inntektsfilter i VTP. Utvide med støtte for filter 8-28 og 8-30
    @DisplayName("Tema SYK: Kombinasjon arbeidstaker og frilanser")
    @Description("Feriepenger skal ikke med i beregnet inntekt(§8-28), endring i arbeidsforhold, sluttet/startet i samme arbeidsforhold og IM på feil ytelse")
    public void SykATFLFeriepenger() throws Exception {
        TestscenarioDto testscenario = opprettScenario("114");
        int inntektsmeldingMånedsbeløp = 20000;
        BigDecimal inntektsmeldingRefusjon = BigDecimal.valueOf(45000);
        LocalDate refusjonOpphørsdato = LocalDate.now().plusMonths(10);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String Tema = "SYK";
        String saksnummer = fordel.opprettSak(testscenario, Tema);


        InntektsmeldingBuilder inntektsmeldingsBuilder = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløp, testscenario.getPersonopplysninger().getSøkerIdent(), "979191138", "ARB001-003", YtelseKodeliste.FORELDREPENGER, ÅrsakInnsendingKodeliste.NY)
                .setRefusjon(InntektsmeldingBuilder.createRefusjon(inntektsmeldingRefusjon, refusjonOpphørsdato, null))
                .setStartdatoForeldrepengeperiodenFOM(LocalDate.of(2018, 10, 6));

        fordel.journalførInnektsmelding(inntektsmeldingsBuilder, testscenario, Long.parseLong(saksnummer));

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);

       saksbehandler.oppdaterBeregning(LocalDate.of(2018, 12, 20),  "Kombinert arbeidstaker og frilanser");

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

        System.out.println("Saksnummer: " + saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);

        saksbehandler.oppdaterBeregning(LocalDate.of(2018, 10, 5), "Frilanser");

        verifiserLikhet(saksbehandler.beregning.getTema().kode, "FOS", "Beregningstema");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 120000D, "Beregnet årsinntekt inkl naturalytelse");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 9, 30));
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 120000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 0D, "Avvik");
    }
}