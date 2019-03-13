package no.nav.foreldrepenger.autotest.klienter.spberegning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.JsonRest;
import no.nav.foreldrepenger.fpmock2.testmodell.util.JsonMapper;

public abstract class SpBeregningKlient extends JsonRest {

    protected Logger log;

    public SpBeregningKlient(HttpSession session) {
        super(session);
        log = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public String hentRestRotUrl() {
        return System.getProperty("autotest.spberegning.http.routing.api");
    }

    @Override
    protected ObjectMapper hentObjectMapper() {
        return new JsonMapper().lagObjectMapper();
    }
}
