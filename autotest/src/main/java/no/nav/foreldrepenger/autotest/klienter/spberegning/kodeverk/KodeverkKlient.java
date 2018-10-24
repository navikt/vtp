package no.nav.foreldrepenger.autotest.klienter.spberegning.kodeverk;

import java.io.IOException;

import no.nav.foreldrepenger.autotest.klienter.spberegning.SpBeregningKlient;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.StatusRange;

public class KodeverkKlient extends SpBeregningKlient {

    public static final String KODEVERK_URL = "/kodeverk";
    
    public KodeverkKlient(HttpSession session) {
        super(session);
    }
    
    //Trenger returverdi
    public void kodeverk() throws IOException {
        String url = hentRestRotUrl() + KODEVERK_URL;
        ValidateResponse(get(url), StatusRange.STATUS_SUCCESS);
        throw new RuntimeException("Ikke ferdig implementert: kodeverk");
    }

}
