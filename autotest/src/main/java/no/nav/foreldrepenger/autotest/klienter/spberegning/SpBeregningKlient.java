package no.nav.foreldrepenger.autotest.klienter.spberegning;

import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.JsonRest;
import no.nav.foreldrepenger.autotest.util.konfigurasjon.MiljoKonfigurasjon;

public abstract class SpBeregningKlient extends JsonRest{

    public SpBeregningKlient(HttpSession session) {
        super(session);
    }

    @Override
    public String hentRestRotUrl() {
    	return System.getProperty("autotest.spberegning.http.routing.api");
    }
}
