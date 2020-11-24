package no.nav.pdl.oversetter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SivilstandKoder {
    private static final Map<String, String> SIVILSTANDER;

    static {
        final Map<String, String> sivilstander = new HashMap<>();
        sivilstander.put("ENKE", "ENKE_ELLER_ENKEMANN");
        sivilstander.put("GIFT", "GIFT");
        sivilstander.put("GJPA", "GJENLEVENDE_PARTNER");
        sivilstander.put("GLAD", "UOPPGITT");
        sivilstander.put("REPA", "REGISTRERT_PARTNER");
        sivilstander.put("SAMB", "REGISTRERT_PARTNER");
        sivilstander.put("SEPA", "SEPARERT_PARTNER");
        sivilstander.put("SEPR", "SEPARERT");
        sivilstander.put("SKIL", "SKILT");
        sivilstander.put("SKPA", "SKILT_PARTNER");
        sivilstander.put("UGIF", "UGIFT");
        SIVILSTANDER = Collections.unmodifiableMap(sivilstander);
    }

    public static String tilSivilstandPDL(String sivilstandTPS) {
        return SIVILSTANDER.get(sivilstandTPS);
    }

}
