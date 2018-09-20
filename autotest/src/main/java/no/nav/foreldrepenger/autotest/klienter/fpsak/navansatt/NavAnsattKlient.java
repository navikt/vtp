package no.nav.foreldrepenger.autotest.klienter.fpsak.navansatt;

import java.io.IOException;

import no.nav.foreldrepenger.autotest.klienter.fpsak.FpsakKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.navansatt.dto.InnloggetNavAnsatt;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.StatusRange;

public class NavAnsattKlient extends FpsakKlient{

    private static final String NAV_ANSATT_URL = "/nav-ansatt";
    
    public NavAnsattKlient(HttpSession session) {
        super(session);
    }
    
    public InnloggetNavAnsatt navAnsatt() throws IOException {
        String url = hentRestRotUrl() + NAV_ANSATT_URL;
        return getOgHentJson(url, InnloggetNavAnsatt.class, StatusRange.STATUS_SUCCESS);
    }
    
}
