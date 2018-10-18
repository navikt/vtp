package no.nav.foreldrepenger.autotest.klienter.fpsak.brev;

import java.io.IOException;

import no.nav.foreldrepenger.autotest.klienter.fpsak.FpsakKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.brev.dto.BestillBrev;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.StatusRange;

public class BrevKlient extends FpsakKlient{
    
    private static String BREV_URL = "/brev";
    private static String BREV_BESTILL_URL = BREV_URL + "/bestill";
    private static String BREV_MOTTAKERE_URL = BREV_URL + "/bestill";
    private static String BREV_MALER_URL = BREV_URL + "/bestill";
    private static String BREV_FORHANDSVIS_URL = BREV_URL + "/bestill";
    private static String BREV_VARSEL_REVURDERING_URL = BREV_URL + "/bestill";
    
    public BrevKlient(HttpSession session) {
        super(session);
    }
    
    public void bestill(BestillBrev brev) throws IOException {
        String url = hentRestRotUrl() + BREV_BESTILL_URL;
        postOgVerifiser(url, brev, StatusRange.STATUS_SUCCESS);
    }
    
    
}
