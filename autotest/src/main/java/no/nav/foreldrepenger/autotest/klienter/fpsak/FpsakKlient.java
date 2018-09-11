package no.nav.foreldrepenger.autotest.klienter.fpsak;

import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.JsonRest;
import no.nav.foreldrepenger.autotest.util.konfigurasjon.MiljoKonfigurasjon;

public class FpsakKlient extends JsonRest{

	public FpsakKlient(HttpSession session) {
		super(session);
	}

	@Override
    public String hentRestRotUrl() {
    	
    	//TODO Hack until env management
    	MiljoKonfigurasjon env = new MiljoKonfigurasjon();
    	env.loadEnv(MiljoKonfigurasjon.hentMiljø());
    	return env.hentProperty(MiljoKonfigurasjon.PROPERTY_FPSAK_API_ROOT);
    }

}
