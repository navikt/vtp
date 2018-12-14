package no.nav.foreldrepenger.autotest.spberegning;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.BeregningKlient;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.inntektsmelding.xml.kodeliste._20180702.BegrunnelseIngenEllerRedusertUtbetalingKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.NaturalytelseKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.YtelseKodeliste;
import no.seres.xsd.nav.inntektsmelding_m._20180924.Periode;
import no.seres.xsd.nav.inntektsmelding_m._20180924.Refusjon;

@Tag("spberegning")
public class VerdikjedeTest extends SpberegningTestBase {
    BeregningKlient klient;

    @Test
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
    public void sykAtFlMedAvvik() throws Exception {
        TestscenarioDto testscenario = opprettScenario("110");
        
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        String saksnummer = fordel.opprettSak(testscenario,"SYK");
        
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, YtelseKodeliste.SYKEPENGER, LocalDate.now());
        InntektsmeldingBuilder inntektsmeldingsBuilder = inntektsmeldinger.get(1);
        inntektsmeldingsBuilder.setRefusjon(InntektsmeldingBuilder.createRefusjon(BigDecimal.valueOf(45000), LocalDate.now().plusMonths(10), null));
        
        List<Periode> perioder = new ArrayList<>();
        perioder.add(InntektsmeldingBuilder.createPeriode(LocalDate.now().minusDays(5), LocalDate.now()));
        inntektsmeldingsBuilder.setSykepengerIArbeidsgiverperioden(InntektsmeldingBuilder.createSykepengerIArbeidsgiverperioden(
                BigDecimal.valueOf(0), perioder, BegrunnelseIngenEllerRedusertUtbetalingKodeliste.LOVLIG_FRAVAER));
        inntektsmeldingsBuilder.getOpphoerAvNaturalytelsesList().getOpphoerAvNaturalytelse().add(InntektsmeldingBuilder.createNaturalytelseDetaljer(
                BigDecimal.valueOf(37500), LocalDate.now().minusMonths(2), NaturalytelseKodeliste.ELEKTRONISK_KOMMUNIKASJON));
        
        System.out.println("Saksnummer: " + saksnummer);
        fordel.journalførInnektsmelding(inntektsmeldingsBuilder,testscenario, Long.parseLong(saksnummer));
        
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.foreslåBeregning(testscenario, saksnummer);
        
        verifiserLikhet(saksbehandler.getSkjæringstidspunkt(), LocalDate.now().minusDays(5), "Skjæringstidspunkt");
        saksbehandler.oppdaterBeregning(saksbehandler.getSkjæringstidspunkt(), saksbehandler.kodeverk.AktivitetStatus.getKode("Kombinert arbeidstaker og frilanser"));
        
        verifiserLikhet(saksbehandler.beregning.getTema().kode, "SYK", "Beregningstema");
        verifiserLikhet(saksbehandler.beregnetÅrsinntekt(), 444000D, "Beregnet årsinntekt");
        verifiserLikhet(saksbehandler.getSammenligningsgrunnlag(), 1404000D, "Sammenlikningsgrunnlag");
        verifiserLikhet(saksbehandler.getAvvikIProsent(), 68.4D, "Avvik");
    }

}
