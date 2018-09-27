package no.nav.foreldrepenger.autotest.klienter.fpsak.historikk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.FpsakKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.historikk.dto.HistorikkInnslag;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.StatusRange;

public class HistorikkKlient extends FpsakKlient{

    private static String HISTORIKK_URL_FORMAT = "/historikk?saksnummer=%1$s&";
    
    public HistorikkKlient(HttpSession session) {
        super(session);
    }
    
    public List<HistorikkInnslag> hentHistorikk(long saksnummer) throws IOException {
        String url = hentRestRotUrl() + String.format(HISTORIKK_URL_FORMAT, saksnummer);
        return getOgHentJson(url, hentObjectMapper().getTypeFactory().constructCollectionType(ArrayList.class, HistorikkInnslag.class), StatusRange.STATUS_SUCCESS);
    }
    
}
