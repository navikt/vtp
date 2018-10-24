package no.nav.foreldrepenger.autotest.klienter.spberegning.selftest;

import java.io.IOException;

import no.nav.foreldrepenger.autotest.klienter.spberegning.SpBeregningKlient;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.StatusRange;

public class SelftestKlient extends SpBeregningKlient {

    public static final String SELFTEST_URL = "/selftest";
    
    public SelftestKlient(HttpSession session) {
        super(session);
    }
    
    public void selftest() throws IOException {
        String url = hentRestRotUrl() + SELFTEST_URL;
        ValidateResponse(get(url), StatusRange.STATUS_SUCCESS);
    }
}
