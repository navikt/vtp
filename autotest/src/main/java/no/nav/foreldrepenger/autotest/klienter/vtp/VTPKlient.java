package no.nav.foreldrepenger.autotest.klienter.vtp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.JsonRest;

public class VTPKlient extends JsonRest{

    protected Logger log;

    public VTPKlient(HttpSession session) {
        super(session);
        log = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public String hentRestRotUrl() {
        if (null != System.getenv("AUTOTEST_VTP_BASE_URL")) {
            return System.getenv("AUTOTEST_VTP_BASE_URL") + "/rest/api";
        } else {
            return System.getProperty("autotest.vtp.url")+":" + System.getProperty("autotest.vtp.port") + "/rest/api";
        }
    }


}
