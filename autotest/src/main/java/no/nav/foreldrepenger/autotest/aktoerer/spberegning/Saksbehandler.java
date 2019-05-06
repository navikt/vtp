package no.nav.foreldrepenger.autotest.aktoerer.spberegning;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import io.qameta.allure.Step;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.BeregningKlient;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.ForeslaaDto;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.ForslagDto;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.LagreNotatDto;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.OppdaterBeregningDto;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning.AktivitetsAvtaleDto;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning.BeregningDto;
import no.nav.foreldrepenger.autotest.klienter.spberegning.kodeverk.KodeverkKlient;
import no.nav.foreldrepenger.autotest.klienter.spberegning.kodeverk.dto.Kode;
import no.nav.foreldrepenger.autotest.klienter.spberegning.kodeverk.dto.Kodeverk;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;

public class Saksbehandler extends Aktoer {

    /*
     * Klienter
     */
    protected KodeverkKlient kodeverkKlient;
    protected BeregningKlient beregningKlient;

    /*
     * Kodeverk
     */
    public Kodeverk kodeverk;

    /*
     * Beregning
     */
    public ForslagDto forslag;
    public BeregningDto beregning;
    public LagreNotatDto lagreNotat;

    public Saksbehandler() {
        kodeverkKlient = new KodeverkKlient(session);
        beregningKlient = new BeregningKlient(session);
    }

    @Override
    public void erLoggetInnMedRolle(Rolle rolle) throws IOException {
        super.erLoggetInnMedRolle(rolle);
        kodeverk = kodeverkKlient.kodeverk();
        //throw new RuntimeException("erLoggetInnMedRolle ikke ferdig implementert");
    }

    /*
     * Foreslår og henter forslag fra beregning
     */
    @Step("Foreslår beregning for Gosyssak {gosysSakId}")
    public BeregningDto foreslåBeregning(String tema, TestscenarioDto testscenario, String gosysSakId) throws IOException {
        ForeslaaDto foreslå = new ForeslaaDto(tema, Long.parseLong(testscenario.getPersonopplysninger().getSøkerAktørIdent()), gosysSakId);
        forslag = beregningKlient.foreslaBeregningPost(foreslå);
        beregning = beregningKlient.hentBeregning(forslag.getBeregningId());
        beregning.getBeregningsgrunnlag().getId();
        System.out.println("Opprettet beregning: " + beregning.getId());
        System.out.println(String.format("http://localhost:9999/#/foresla-beregning/%s/%s/%s/", testscenario.getPersonopplysninger().getSøkerAktørIdent(), gosysSakId, tema));
        return beregning;
    }


    protected void oppdaterBeregning(LocalDate skjæringstidspunkt, Kode status) throws IOException {
        OppdaterBeregningDto request = new OppdaterBeregningDto(beregning.getId());
        request.setSkjæringstidspunkt(skjæringstidspunkt);
        request.setAktivitetStatusKode(status.kode);
        beregningKlient.oppdaterBeregning(request);
        beregning = beregningKlient.hentBeregning(forslag.getBeregningId());
    }

    /** Ikke bruk navn som oppslag i kodeverk. */
    @Deprecated
    public void oppdaterBeregning(LocalDate skjæringstidspunkt, String status) throws IOException {
        try {
            Thread.sleep(5000);
            oppdaterBeregning(skjæringstidspunkt, kodeverk.AktivitetStatus.getKode(status));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    
    public void lagreNotat(BeregningDto beregning, String notat, Long beregningsgrunnlagId) throws IOException {
        LagreNotatDto request = new LagreNotatDto(beregning.getId(), notat, beregningsgrunnlagId);
        beregningKlient.lagrenotat(request);
    }

    public Double beregnetÅrsinntekt() {
        return beregning.getBeregningsgrunnlag().getBeregningsgrunnlagPeriode().get(0).getBeregnetPrAar();
    }

    public Double BruttoInkludertBortfaltNaturalytelsePrAar() {
        return beregning.getBeregningsgrunnlag().getBeregningsgrunnlagPeriode().get(0).getBruttoInkludertBortfaltNaturalytelsePrAar();
    }

    public LocalDate sammenligningsperiodeTom() {
        return beregning.getBeregningsgrunnlag().getSammenligningsgrunnlag().getSammenligningsgrunnlagTom();
    }

    public List<AktivitetsAvtaleDto> getAktivitetsAvtaler() {
        return beregning.getBeregningsgrunnlag().getBeregningsgrunnlagPeriode().get(0).getBeregningsgrunnlagPrStatusOgAndel().get(0).getAktivitetsAvtaleDto();
    }

    public Double getSammenligningsgrunnlag() {
        return beregning.getBeregningsgrunnlag().getSammenligningsgrunnlag().getRapportertPrAar();
    }

    public Double getAvvikIProsent() {
        return beregning.getBeregningsgrunnlag().getSammenligningsgrunnlag().getAvvikProsent();
    }

    public LocalDate getSkjæringstidspunkt() {
        return beregning.getBeregningsgrunnlag().getSkjaeringstidspunktBeregning();
    }

    public Boolean getSjømann() {
        return beregning.getBeregningsgrunnlag().getSjømann();
    }
}
