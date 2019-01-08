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
    public void grunnleggendeSykepenger() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");

        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, LocalDate.now());
        InntektsmeldingBuilder inntektsmeldingsBuilder = inntektsmeldinger.get(0);
        inntektsmeldingsBuilder.addGradertperiode(BigDecimal.TEN, LocalDate.now().plusWeeks(3), LocalDate.now().plusWeeks(5));

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String saksnummer = fordel.opprettSak(testscenario,"SYK");
        fordel.journalførInnektsmelding(inntektsmeldingsBuilder,testscenario,Long.parseLong(saksnummer));


        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.foreslåBeregning(testscenario, saksnummer);

        verifiser(saksbehandler.beregning.getTema().kode.equals("SYK")); 
        
        System.out.println(saksbehandler.beregning.getBeregningsgrunnlag());
    }
    
    @Test
    @Disabled
    public void sykAtFlMedAvvik() throws Exception {
        TestscenarioDto testscenario = opprettScenario("110");
        int inntektsmeldingMånedsbeløp = 37000;
        BigDecimal inntektsmeldingRefusjon = BigDecimal.valueOf(45000);
        LocalDate refusjonOpphørsdato = LocalDate.now().plusMonths(10);
        
        
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String saksnummer = fordel.opprettSak(testscenario,"SYK");
        
        List<Periode> perioder = new ArrayList<>();
        perioder.add(InntektsmeldingBuilder.createPeriode(LocalDate.now().minusDays(15), LocalDate.now().minusDays(10)));
        perioder.add(InntektsmeldingBuilder.createPeriode(LocalDate.now().minusDays(8), LocalDate.now().minusDays(4)));

        InntektsmeldingBuilder inntektsmeldingsBuilder = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløp, testscenario.getPersonopplysninger().getSøkerIdent(), "979191139", "ARB001-002", YtelseKodeliste.SYKEPENGER, ÅrsakInnsendingKodeliste.NY)
                .setRefusjon(InntektsmeldingBuilder.createRefusjon(inntektsmeldingRefusjon, refusjonOpphørsdato, null))
                .setSykepengerIArbeidsgiverperioden(InntektsmeldingBuilder.createSykepengerIArbeidsgiverperioden(
                        BigDecimal.valueOf(0), perioder, BegrunnelseIngenEllerRedusertUtbetalingKodeliste.LOVLIG_FRAVAER));
        inntektsmeldingsBuilder.getOpphoerAvNaturalytelsesList().getOpphoerAvNaturalytelse().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(37000), LocalDate.now().minusMonths(2), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));
        inntektsmeldingsBuilder.getOpphoerAvNaturalytelsesList().getOpphoerAvNaturalytelse().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(37000), LocalDate.now().minusMonths(2), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));
        
        System.out.println("Saksnummer: " + saksnummer);
        fordel.journalførInnektsmelding(inntektsmeldingsBuilder,testscenario, Long.parseLong(saksnummer));
        
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.foreslåBeregning(testscenario, saksnummer);
        
        verifiserLikhet(saksbehandler.getSkjæringstidspunkt(), LocalDate.now().minusDays(8), "Skjæringstidspunkt");
        saksbehandler.oppdaterBeregning(saksbehandler.getSkjæringstidspunkt(), saksbehandler.kodeverk.AktivitetStatus.getKode("Kombinert arbeidstaker og frilanser"));
        
        verifiserLikhet(saksbehandler.beregning.getTema().kode, "SYK", "Beregningstema");
        verifiserLikhet(saksbehandler.beregnetÅrsinntekt(), 1404000D, "Beregnet årsinntekt");
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 864000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 62.5D, "Avvik");
    }

    @Test
    public void forAtAuto() throws Exception {
        TestscenarioDto testscenario = opprettScenario("111");
        int inntektsmeldingMånedsbeløp = 37000;
        BigDecimal inntektsmeldingRefusjon = BigDecimal.valueOf(45000);
        LocalDate refusjonOpphørsdato = LocalDate.now().plusMonths(10);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String saksnummer = fordel.opprettSak(testscenario, "SYK");

        List<Periode> perioder = new ArrayList<>();
        perioder.add(InntektsmeldingBuilder.createPeriode(LocalDate.of(2018, 10, 5), LocalDate.of(2018, 10, 9)));

        InntektsmeldingBuilder inntektsmeldingsBuilder = inntektsmeldingGrunnlag(inntektsmeldingMånedsbeløp, testscenario.getPersonopplysninger().getSøkerIdent(), "979191139", "ARB001-002", YtelseKodeliste.SYKEPENGER, ÅrsakInnsendingKodeliste.NY)
                .setRefusjon(InntektsmeldingBuilder.createRefusjon(inntektsmeldingRefusjon, refusjonOpphørsdato, null))
                .setSykepengerIArbeidsgiverperioden(InntektsmeldingBuilder.createSykepengerIArbeidsgiverperioden(
                        BigDecimal.valueOf(0), perioder, BegrunnelseIngenEllerRedusertUtbetalingKodeliste.LOVLIG_FRAVAER));
        inntektsmeldingsBuilder.getOpphoerAvNaturalytelsesList().getOpphoerAvNaturalytelse().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(450), LocalDate.of(2018, 10, 5), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));

        System.out.println("Inntektsmelding: " + inntektsmeldingsBuilder.createInntektesmeldingXML());
        System.out.println("Saksnummer: " + saksnummer);
        fordel.journalførInnektsmelding(inntektsmeldingsBuilder, testscenario, Long.parseLong(saksnummer));

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.foreslåBeregning(testscenario, saksnummer);

        verifiserLikhet(saksbehandler.getSkjæringstidspunkt(), LocalDate.of(2018, 10, 5), "Skjæringstidspunkt");
        saksbehandler.oppdaterBeregning(saksbehandler.getSkjæringstidspunkt(), saksbehandler.kodeverk.AktivitetStatus.getKode("Kombinert arbeidstaker og frilanser"));

        verifiserLikhet(saksbehandler.beregning.getTema().kode, "SYK", "Beregningstema");
        verifiserLikhet(saksbehandler.beregnetÅrsinntekt(), 1404000D, "Beregnet årsinntekt");
        verifiserLikhet(saksbehandler.BruttoInkludertBortfaltNaturalytelsePrAar(), 1409400D, "Beregnet årsinntekt inkl naturalytelse");
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