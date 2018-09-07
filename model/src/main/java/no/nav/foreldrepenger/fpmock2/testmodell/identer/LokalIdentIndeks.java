package no.nav.foreldrepenger.fpmock2.testmodell.identer;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.BrukerModell.Kjønn;

/** konverterer lokale identer brukt i testcase til utvalgte fødselsnummer hentet fra syntetisk liste. */
public class LokalIdentIndeks {

    private final IdentGenerator identGenerator;
    private final Map<String, String> identer = new ConcurrentHashMap<>(); // NOSONAR
    private String unikScenarioId;

    public LokalIdentIndeks(String unikScenarioId, IdentGenerator identGenerator) {
        this.unikScenarioId = unikScenarioId;
        this.identGenerator = identGenerator;
    }
    
    public Map<String, String> getAlleIdenter(){
        return Collections.unmodifiableMap(identer);
    }

    public String getVoksenIdentForLokalIdent(String lokalIdent, Kjønn kjønn) {
        if (lokalIdent.matches("^\\d+$")) {
            return identer.computeIfAbsent(key(lokalIdent), i -> lokalIdent);
        }
        return identer.computeIfAbsent(key(lokalIdent), i -> kjønn == Kjønn.M ? identGenerator.tilfeldigMannFnr() : identGenerator.tilfeldigKvinneFnr());
    }

    private String key(String lokalIdent) {
        return unikScenarioId + "::" + lokalIdent;
    }

    public String getBarnIdentForLokalIdent(String lokalIdent) {
        if (lokalIdent.matches("^\\d+$")) {
            return identer.computeIfAbsent(key(lokalIdent), i -> lokalIdent);
        }
        // tilfeldig kjønn
        return identer.computeIfAbsent(key(lokalIdent), i -> identGenerator.tilfeldigBarnUnderTreAarFnr());
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
