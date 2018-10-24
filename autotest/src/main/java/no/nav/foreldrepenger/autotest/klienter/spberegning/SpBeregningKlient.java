package no.nav.foreldrepenger.autotest.klienter.spberegning;

import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.JsonRest;

public abstract class SpBeregningKlient extends JsonRest{

    public SpBeregningKlient(HttpSession session) {
        super(session);
    }

    @Override
    public String hentRestRotUrl() {
        // TODO Auto-generated method stub
        return null;
    }
}
