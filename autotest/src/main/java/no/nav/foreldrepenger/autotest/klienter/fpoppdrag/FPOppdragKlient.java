package no.nav.foreldrepenger.autotest.klienter.fpoppdrag;

import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.JsonRest;

public class FPOppdragKlient extends JsonRest {


    public FPOppdragKlient(HttpSession session) {
        super(session);
    }

    @Override
    public String hentRestRotUrl() {
        return System.getProperty("autotest.fpoppdrag.http.routing.api");
    }
}
