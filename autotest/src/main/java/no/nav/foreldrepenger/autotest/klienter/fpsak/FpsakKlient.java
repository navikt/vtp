package no.nav.foreldrepenger.autotest.klienter.fpsak;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.JsonRest;
import no.nav.foreldrepenger.autotest.util.konfigurasjon.MiljoKonfigurasjon;
import no.nav.foreldrepenger.fpmock2.testmodell.util.JsonMapper;

public class FpsakKlient extends JsonRest{

    protected Logger log;

    public FpsakKlient(HttpSession session) {
        super(session);
        log = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public String hentRestRotUrl() {
        return MiljoKonfigurasjon.getRouteApi();
    }

    @Override
    protected ObjectMapper hentObjectMapper() {
        return new JsonMapper().lagObjectMapper();
    }

}
