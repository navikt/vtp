package no.nav.foreldrepenger.fpmock2.testmodell.personopplysning;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.BrukerModell.Kjønn;

/** konverterer lokale identer brukt i testcase til utvalgte fødselsnummer hentet fra syntetisk liste. */
public class Identer {

    private static final FiktiveFnr fiktive = new FiktiveFnr(); // NOSONAR
    private final Map<String, String> identer = new ConcurrentHashMap<>(); // NOSONAR
    private String scenario;

    public Identer(String scenario) {
        this.scenario = scenario;
    }
    
    public Map<String, String> getAlleIdenter(){
        return Collections.unmodifiableMap(identer);
    }

    public String getVoksenIdentForLokalIdent(String lokalIdent, Kjønn kjønn) {
        if (lokalIdent.matches("^\\d+$")) {
            return identer.computeIfAbsent(key(lokalIdent), i -> lokalIdent);
        }
        return identer.computeIfAbsent(key(lokalIdent), i -> kjønn == Kjønn.M ? fiktive.nesteMannFnr() : fiktive.nesteKvinneFnr());
    }

    private String key(String lokalIdent) {
        return scenario + "::" + lokalIdent;
    }

    public String getBarnIdentForLokalIdent(String lokalIdent) {
        if (lokalIdent.matches("^\\d+$")) {
            return identer.computeIfAbsent(key(lokalIdent), i -> lokalIdent);
        }
        // tilfeldig kjønn
        return identer.computeIfAbsent(key(lokalIdent), i -> fiktive.nesteBarnFnr());
    }

    public String getIdent(String lokalIdent) {
        String key = key(lokalIdent);
        String ident = identer.get(key);
//        if (ident == null) {
//            throw new IllegalStateException("Kjenner ikke ident for lokal id: " + lokalIdent + ", mulig ikke lastet ennå?");
//        }
        return ident;
    }
}
