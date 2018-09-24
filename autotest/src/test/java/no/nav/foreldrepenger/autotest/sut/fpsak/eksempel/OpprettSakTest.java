package no.nav.foreldrepenger.autotest.sut.fpsak.eksempel;

import java.io.IOException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.sut.fpsak.FpsakTestBase;

@Tag("smokeBLA")
public class OpprettSakTest extends FpsakTestBase{
    
    @Test
    public void opprettInntektsmeldingForeldrepenger() throws Exception {
        fordel.erLoggetInnMedRolle("Saksbehandler");
        
        long saksnummer = fordel.sendInnInntektsmelding(null, "1000104117747");
        
        saksbehandler.erLoggetInnMedRolle("Saksbehandler");
        saksbehandler.hentFagsak(saksnummer);
        
        verifiser(saksbehandler.valgtFagsak.saksnummer == saksnummer, "Kunne ikke hente fagsak");
        
        System.out.println("Saksnummer: " + saksnummer);
    }

}
