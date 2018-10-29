package no.nav.foreldrepenger.autotest.klienter.spberegning.kodeverk;

import java.io.IOException;

import no.nav.foreldrepenger.autotest.klienter.spberegning.SpBeregningKlient;
import no.nav.foreldrepenger.autotest.klienter.spberegning.kodeverk.dto.Kodeverk;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.StatusRange;

public class KodeverkKlient extends SpBeregningKlient {

    public static final String KODEVERK_URL = "/kodeverk";
    
    public KodeverkKlient(HttpSession session) {
        super(session);
    }
    
    
    public Kodeverk kodeverk() throws IOException {
        String url = hentRestRotUrl() + KODEVERK_URL;
        return getOgHentJson(url, Kodeverk.class, StatusRange.STATUS_SUCCESS);
    }

}
