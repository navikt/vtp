package no.nav.foreldrepenger.autotest.spberegning;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.BeregningKlient;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning.AktivitetsAvtaleDto;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning.BeregningsgrunnlagPeriodeDto;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning.BeregningsgrunnlagPrStatusOgAndelDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arbeidsforhold.Arbeidsforhold;
import no.nav.inntektsmelding.xml.kodeliste._20180702.BegrunnelseIngenEllerRedusertUtbetalingKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.NaturalytelseKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.YtelseKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.ÅrsakInnsendingKodeliste;
import no.seres.xsd.nav.inntektsmelding_m._20180924.Periode;

@Tag("spberegning")
public class VerdikjedeTest extends SpberegningTestBase {
    BeregningKlient klient;

    @Test
    @Disabled
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
                .setStartdatoForeldrepengeperiodenFOM(LocalDate.of(2018,    10, 5));
        inntektsmeldingsBuilder.getOpphoerAvNaturalytelsesList().getOpphoerAvNaturalytelse().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(450), LocalDate.of(2018, 10, 5), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));
        inntektsmeldingsBuilder.getGjenopptakelseNaturalytelseListe().getNaturalytelseDetaljer().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(450), LocalDate.of(2018, 12, 31), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));

        System.out.println("Inntektsmelding: " + inntektsmeldingsBuilder.createInntektesmeldingXML());
        System.out.println("Saksnummer: " + saksnummer);

        fordel.journalførInnektsmelding(inntektsmeldingsBuilder, testscenario, Long.parseLong(saksnummer));

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String url = saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);
        System.out.println("Webside: " + url);

        verifiserLikhet(saksbehandler.getSkjæringstidspunkt(), LocalDate.of(2018, 10, 5), "Skjæringstidspunkt");
//        saksbehandler.oppdaterBeregning(saksbehandler.getSkjæringstidspunkt(), saksbehandler.kodeverk.AktivitetStatus.getKode("Kombinert arbeidstaker og frilanser"));
        verifiserLikhet(saksbehandler.beregning.getTema().kode, "FOR", "Beregningstema");
        verifiserLikhet(saksbehandler.beregnetÅrsinntekt(), 684000D, "Sum inntekt");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 689400D, "Beregnet årsinntekt inkl naturalytelse");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 9, 30));
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 444000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 55.3D, "Avvik");

    }

    @Test
    @Disabled
    public void OmsMotorveiUnder25Avvik() throws Exception {
        TestscenarioDto testscenario = opprettScenario("110");
        int inntektsmeldingMånedsbeløp = 37000;
        BigDecimal inntektsmeldingRefusjon = BigDecimal.valueOf(27000);
        LocalDate refusjonOpphørsdato = LocalDate.now().plusMonths(10);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String Tema = "OMS";
        String saksnummer = fordel.opprettSak(testscenario, Tema);
        List<Arbeidsforhold> arbeidsforhold=testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold();

        InntektsmeldingBuilder inntektsmeldingsBuilder = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløp, testscenario.getPersonopplysninger().getSøkerIdent(), "979191139", "ARB001-002", YtelseKodeliste.OMSORGSPENGER, ÅrsakInnsendingKodeliste.NY)
                .setRefusjon(InntektsmeldingBuilder.createRefusjon(inntektsmeldingRefusjon, refusjonOpphørsdato, null))
                .setStartdatoForeldrepengeperiodenFOM(LocalDate.of(2018,    10, 5))
                .setArbeidsforhold(InntektsmeldingBuilder.createArbeidsforhold(null, null, null, null, null, null));

        inntektsmeldingsBuilder.getOpphoerAvNaturalytelsesList().getOpphoerAvNaturalytelse().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(450), LocalDate.of(2018, 10, 6), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));
        inntektsmeldingsBuilder.getGjenopptakelseNaturalytelseListe().getNaturalytelseDetaljer().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(450), LocalDate.of(2018, 12, 31), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));

        System.out.println("Inntektsmelding: " + inntektsmeldingsBuilder.createInntektesmeldingXML());
        System.out.println("Saksnummer: " + saksnummer);

        fordel.journalførInnektsmelding(inntektsmeldingsBuilder, testscenario, Long.parseLong(saksnummer));

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String url = saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);
        System.out.println("Webside: " + url);

        verifiserLikhet(saksbehandler.getSkjæringstidspunkt(), LocalDate.of(2018, 10, 5), "Skjæringstidspunkt");
//        saksbehandler.oppdaterBeregning(saksbehandler.getSkjæringstidspunkt(), saksbehandler.kodeverk.AktivitetStatus.getKode("Kombinert arbeidstaker og frilanser"));

        verifiserLikhet(saksbehandler.beregning.getTema().kode, "OMS", "Beregningstema");
        verifiserLikhet(saksbehandler.beregnetÅrsinntekt(), 444000D, "Sum inntekt");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 444000D, "Beregnet årsinntekt inkl naturalytelse");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 9, 30));
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 444000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 0D, "Avvik");
    }
    
    @Test
    public void For2AtOver25Avvik() throws Exception {
        TestscenarioDto testscenario = opprettScenario("111");
        int inntektsmeldingMånedsbeløp = 37000;
        BigDecimal inntektsmeldingRefusjon = BigDecimal.valueOf(45000);
        LocalDate refusjonOpphørsdato = LocalDate.now().plusMonths(10);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String Tema = "FOR";
        String saksnummer = fordel.opprettSak(testscenario, Tema);


        InntektsmeldingBuilder inntektsmeldingsBuilder = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløp, testscenario.getPersonopplysninger().getSøkerIdent(), "979191139", "ARB001-002", YtelseKodeliste.FORELDREPENGER, ÅrsakInnsendingKodeliste.NY)
                .setRefusjon(InntektsmeldingBuilder.createRefusjon(inntektsmeldingRefusjon, refusjonOpphørsdato, null))
                .setStartdatoForeldrepengeperiodenFOM(LocalDate.of(2018,    10, 5));
        inntektsmeldingsBuilder.getOpphoerAvNaturalytelsesList().getOpphoerAvNaturalytelse().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(450), LocalDate.of(2018, 10, 5), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));
        inntektsmeldingsBuilder.getGjenopptakelseNaturalytelseListe().getNaturalytelseDetaljer().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(450), LocalDate.of(2018, 12, 31), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));

        System.out.println("Inntektsmelding: " + inntektsmeldingsBuilder.createInntektesmeldingXML());
        System.out.println("Saksnummer: " + saksnummer);

        fordel.journalførInnektsmelding(inntektsmeldingsBuilder, testscenario, Long.parseLong(saksnummer));

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String url = saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);
        System.out.println("Webside: " + url);

        verifiserLikhet(saksbehandler.getSkjæringstidspunkt(), LocalDate.of(2018, 10, 5), "Skjæringstidspunkt");
        saksbehandler.oppdaterBeregning(saksbehandler.getSkjæringstidspunkt(), saksbehandler.kodeverk.AktivitetStatus.getKode("Kombinert arbeidstaker og frilanser"));

        verifiserLikhet(saksbehandler.beregning.getTema().kode, "FOR", "Beregningstema");
        verifiserLikhet(saksbehandler.beregnetÅrsinntekt(), 1404000D, "Sum inntekt");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 1409400D, "Beregnet årsinntekt inkl naturalytelse");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 9, 30));
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 864000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 63.1D, "Avvik");
    }

    @Test
    public void Syk2AtOver25Avvik() throws Exception {
        TestscenarioDto testscenario = opprettScenario("111");
        int inntektsmeldingMånedsbeløp = 37000;
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

        System.out.println("Inntektsmelding: " + inntektsmeldingsBuilder.createInntektesmeldingXML());
        System.out.println("Saksnummer: " + saksnummer);
        fordel.journalførInnektsmelding(inntektsmeldingsBuilder, testscenario, Long.parseLong(saksnummer));

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String url = saksbehandler.foreslåBeregning(Tema, testscenario, saksnummer);
        System.out.println("Webside: " + url);

        verifiserLikhet(saksbehandler.getSkjæringstidspunkt(), LocalDate.of(2018, 10, 5), "Skjæringstidspunkt");
        saksbehandler.oppdaterBeregning(saksbehandler.getSkjæringstidspunkt(), saksbehandler.kodeverk.AktivitetStatus.getKode("Kombinert arbeidstaker og frilanser"));

        verifiserLikhet(saksbehandler.beregning.getTema().kode, "SYK", "Beregningstema");
        verifiserLikhet(saksbehandler.beregnetÅrsinntekt(), 1404000D, "Sum inntekt");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 1409400D, "Beregnet årsinntekt");
        verifiserLikhet(saksbehandler.sammenligningsperiodeTom(), LocalDate.of(2018, 9, 30));
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 864000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 63.1D, "Avvik");
        
        verifiserPerioder(saksbehandler.beregning.getBeregningsgrunnlag().getBeregningsgrunnlagPeriode(), testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold());

    }
    
    public void verifiserPerioder(List<BeregningsgrunnlagPeriodeDto> perioder, List<Arbeidsforhold> arbeidsforholdListe) {
        for(BeregningsgrunnlagPeriodeDto periode : perioder) {
            verifiserBeregningsgrunnlagPrStatusOgAndel(periode.getBeregningsgrunnlagPrStatusOgAndel(), arbeidsforholdListe);
        }
    }
    
    public void verifiserBeregningsgrunnlagPrStatusOgAndel(List<BeregningsgrunnlagPrStatusOgAndelDto> grunnlagListe, List<Arbeidsforhold> arbeidsforholdListe) {
        for (BeregningsgrunnlagPrStatusOgAndelDto grunnlag : grunnlagListe) {
            
            String orgNr = grunnlag.getOrgNummer();
            
            //Søk frem arbeidsforhold fra testmodellen
            Arbeidsforhold arbeidsforhold = null;
            for (Arbeidsforhold item : arbeidsforholdListe) {
                if(item.getArbeidsgiverOrgnr().equals(orgNr)) {
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