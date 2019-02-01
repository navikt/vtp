package no.nav.foreldrepenger.autotest.spberegning;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.qameta.allure.Description;
import no.seres.xsd.nav.inntektsmelding_m._20181211.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.core.Local;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.BeregningKlient;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning.AktivitetsAvtaleDto;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning.BeregningDto;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning.BeregningsgrunnlagPeriodeDto;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning.BeregningsgrunnlagPrStatusOgAndelDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arbeidsforhold.Arbeidsforhold;
import no.nav.inntektsmelding.xml.kodeliste._20180702.BegrunnelseIngenEllerRedusertUtbetalingKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.NaturalytelseKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.YtelseKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.ÅrsakInnsendingKodeliste;

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
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 55.3D, "Avvik");

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
                        BigDecimal.valueOf(0), perioder, BegrunnelseIngenEllerRedusertUtbetalingKodeliste.LOVLIG_FRAVAER));
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
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 55.3D, "Avvik");

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
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 1.2D, "Avvik");

    }

    @Test
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

        InntektsmeldingBuilder inntektsmeldingBuilder = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløp, testscenario.getPersonopplysninger().getSøkerIdent(), "979191139", "ARB001-002", YtelseKodeliste.PLEIEPENGER_BARN, ÅrsakInnsendingKodeliste.NY) //TODO YtelseKodeliste må oppdateres til PLEIEPENGER
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
    @Disabled
    @DisplayName("Tema FOR: Flere arbeidsforhold")
    @Description("Bruker er sjømann, arbeidsforhold hos privatperson, opphørt arbeidsforhold og naturalytelse medregnet i inntekt. Avvik over 25%")
    public void For3AtOver25AvvikPrivatArbeidsforhold() throws Exception {
        TestscenarioDto testscenario = opprettScenario("111");
        int inntektsmeldingMånedsbeløp = 43000;
        BigDecimal inntektsmeldingRefusjon = BigDecimal.valueOf(45000);
        LocalDate refusjonOpphørsdato = LocalDate.now().plusMonths(10);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String Tema = "FOR";
        String saksnummer = fordel.opprettSak(testscenario, Tema);


        InntektsmeldingBuilder inntektsmeldingsBuilder = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløp, testscenario.getPersonopplysninger().getSøkerIdent(), "979191139", "ARB001-002", YtelseKodeliste.FORELDREPENGER, ÅrsakInnsendingKodeliste.NY)
                .setRefusjon(InntektsmeldingBuilder.createRefusjon(inntektsmeldingRefusjon, refusjonOpphørsdato, null))
                .setStartdatoForeldrepengeperiodenFOM(LocalDate.of(2018, 9, 15));
        inntektsmeldingsBuilder.getOpphoerAvNaturalytelsesList().getOpphoerAvNaturalytelse().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(450), LocalDate.of(2018, 10, 5), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));
        inntektsmeldingsBuilder.getGjenopptakelseNaturalytelseListe().getNaturalytelseDetaljer().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(450), LocalDate.of(2018, 12, 31), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));
        //inntektsmeldingsBuilder.getArbeidsforhold().getGraderingIForeldrepengerListe().add(InntektsmeldingBuilder.createGraderingIForeldrepenger(
        //      BigDecimal.valueOf(50), perode));

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
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 1.9D, "Avvik");
        verifiserLikhet(saksbehandler.getSjømann(), true);
//        verifiserPerioder(saksbehandler.beregning.getBeregningsgrunnlag().getBeregningsgrunnlagPeriode(), testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold());

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
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 37.5D, "Avvik");
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
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 2.9D, "Avvik");
    }

    @Test
    @DisplayName("Tema FOR: Kombinasjon arbeidstaker og frilanser")
    @Description("Kombinasjon AT/FL, Inntektsmelding uten Arb.ID og test med feriepenger")
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
        verifiserLikhet(saksbehandler.beregnetÅrsinntekt(), 360000D, "Sum inntekt");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 360000D, "Beregnet årsinntekt inkl naturalytelse");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 9, 30));
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 600000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 40D, "Avvik");
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

    public void verifiserPerioder(List<BeregningsgrunnlagPeriodeDto> perioder, List<Arbeidsforhold> arbeidsforholdListe) {
        for (BeregningsgrunnlagPeriodeDto periode : perioder) {
            verifiserBeregningsgrunnlagPrStatusOgAndel(periode.getBeregningsgrunnlagPrStatusOgAndel(), arbeidsforholdListe);
        }
    }

    public void verifiserBeregningsgrunnlagPrStatusOgAndel(List<BeregningsgrunnlagPrStatusOgAndelDto> grunnlagListe, List<Arbeidsforhold> arbeidsforholdListe) {
        for (BeregningsgrunnlagPrStatusOgAndelDto grunnlag : grunnlagListe) {

            String orgNr = grunnlag.getOrgNummer();

            //Søk frem arbeidsforhold fra testmodellen
            Arbeidsforhold arbeidsforhold = null;
            for (Arbeidsforhold item : arbeidsforholdListe) {
                if (item.getArbeidsgiverOrgnr().equals(orgNr)) {
                    arbeidsforhold = item;
                }
            }

            verifiser(arbeidsforhold != null, "Fant ikke arbeidsforhold i testmodellen: " + orgNr);

            verifiserAktivitetsAvtaler(grunnlag.getAktivitetsAvtaleDto(), arbeidsforhold);
        }
    }

    public void verifiserAktivitetsAvtaler(List<AktivitetsAvtaleDto> arbeidsavtaler, Arbeidsforhold arbeidsforhold) {
        for (AktivitetsAvtaleDto arbeidsavtale : arbeidsavtaler) {
            verifiserLikhet(arbeidsavtale.getArbeidsprosent().intValue(), arbeidsforhold.getArbeidsavtaler().get(0).getStillingsprosent(), "feil arbeidsprosent");
            verifiserLikhet(arbeidsavtale.getOppstartArbeidsforhold(), arbeidsforhold.getAnsettelsesperiodeFom(), "feil Oppstartsdato");
        }
    }
}