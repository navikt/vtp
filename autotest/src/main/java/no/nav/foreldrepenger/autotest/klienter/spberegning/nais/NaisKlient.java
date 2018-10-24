package no.nav.foreldrepenger.autotest.klienter.spberegning.nais;

import no.nav.foreldrepenger.autotest.klienter.spberegning.SpBeregningKlient;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;

public class NaisKlient extends SpBeregningKlient {
    
    public static final String IS_READY_URL = "/isReady";
    public static final String PRE_STOP_URL = "/preStop";
    public static final String IS_ALIVE_URL = "/isAlive";
    
    public NaisKlient(HttpSession session) {
        super(session);
    }
}
