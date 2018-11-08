package no.nav.foreldrepenger.autotest.aktoerer.spberegning;

import java.io.IOException;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.BeregningKlient;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.ForeslaaDto;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.ForslagDto;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning.BeregningDto;
import no.nav.foreldrepenger.autotest.klienter.spberegning.kodeverk.KodeverkKlient;
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
        kodeverkKlient.kodeverk();
        //throw new RuntimeException("erLoggetInnMedRolle ikke ferdig implementert");
    }
    
    /*
     * Foreslår og henter forslag fra beregning
     */
    public void foreslåBeregning(TestscenarioDto testscenario, String gosysSakId) throws IOException {
        ForeslaaDto foreslå = new ForeslaaDto("SYK", Long.parseLong(testscenario.getPersonopplysninger().getSøkerAktørIdent()), gosysSakId);
        forslag = beregningKlient.foreslaBeregningPost(foreslå);
        beregning = beregningKlient.hentBeregning(forslag.getBeregningId());
    }
}
