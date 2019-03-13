package no.nav.foreldrepenger.autotest.klienter.fpoppdrag;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.JsonRest;
import no.nav.foreldrepenger.fpmock2.testmodell.util.JsonMapper;

public class FPOppdragKlient extends JsonRest {

    public FPOppdragKlient(HttpSession session) {
        super(session);
    }

    @Override
    protected ObjectMapper hentObjectMapper() {
        return new JsonMapper().lagObjectMapper();
    }

    @Override
    public String hentRestRotUrl() {
        return System.getProperty("autotest.fpoppdrag.http.routing.api");
    }
}
