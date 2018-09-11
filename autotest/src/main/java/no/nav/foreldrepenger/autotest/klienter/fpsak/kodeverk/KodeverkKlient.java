package no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk;

import java.io.IOException;

import no.nav.foreldrepenger.autotest.klienter.fpsak.FpsakKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kodeverk;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.StatusRange;

public class KodeverkKlient extends FpsakKlient{

    public static String KODEVERK_URL = "/kodeverk";
    
    public static String KODEVERK_BEHANDLENDE_ENHETER_URL = KODEVERK_URL + "/behandlende-enheter";
    
    public KodeverkKlient(HttpSession session) {
        super(session);
    }
    
    
    public Kodeverk getKodeverk() throws IOException{
        String url = hentRestRotUrl() + KODEVERK_URL;
        return getOgHentJson(url, Kodeverk.class, StatusRange.STATUS_SUCCESS);
    }
    
    
}
