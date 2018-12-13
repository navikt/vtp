package no.nav.foreldrepenger.autotest.aktoerer.spberegning;

import java.io.IOException;
import java.time.LocalDate;

import io.qameta.allure.Step;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.BeregningKlient;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.ForeslaaDto;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.ForslagDto;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.OppdaterBeregningDto;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning.BeregningDto;
import no.nav.foreldrepenger.autotest.klienter.spberegning.kodeverk.KodeverkKlient;
import no.nav.foreldrepenger.autotest.klienter.spberegning.kodeverk.dto.Kode;
import no.nav.foreldrepenger.autotest.klienter.spberegning.kodeverk.dto.Kodeverk;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;

public class Saksbehandler extends Aktoer{

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
    public void foreslåBeregning(TestscenarioDto testscenario, String gosysSakId) throws IOException {
        ForeslaaDto foreslå = new ForeslaaDto("SYK", Long.parseLong(testscenario.getPersonopplysninger().getSøkerAktørIdent()), gosysSakId);
        forslag = beregningKlient.foreslaBeregningPost(foreslå);
        beregning = beregningKlient.hentBeregning(forslag.getBeregningId());
        System.out.println("Oprettet beregning: " + beregning.getId());
    }
    
    public void oppdaterBeregning(LocalDate skjæringstidspunkt, Kode status) throws IOException {
        OppdaterBeregningDto request = new OppdaterBeregningDto(beregning.getId());
        request.setSkjæringstidspunkt(skjæringstidspunkt);
        request.setAktivitetStatusKode(status.kode);
        beregningKlient.oppdaterBeregning(request);
        beregning = beregningKlient.hentBeregning(forslag.getBeregningId());
    }

    public Double beregnetÅrsinntekt() {
        return beregning.getBeregningsgrunnlag().getBeregningsgrunnlagPeriode().get(0).getBeregnetPrAar();
    }

    public Double getSammenligningsgrunnlag() {
        return beregning.getBeregningsgrunnlag().getSammenligningsgrunnlag().getRapportertPrAar();
    }

    public Double getAvvikIProsent() {
        return beregning.getBeregningsgrunnlag().getSammenligningsgrunnlag().getAvvikPromille() / 10;
    }
}
