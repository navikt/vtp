package no.nav.foreldrepenger.vtp.testmodell.identer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerModell;

/** konverterer lokale identer brukt i testcase til utvalgte fødselsnummer hentet fra syntetisk liste. */
public class LokalIdentIndeks {

    private static final String REGEX_MATCH_PATTERN = "^\\d+$";

    @JsonIgnore
    private final IdentGenerator identGenerator;
    private final Map<String, String> identer = new ConcurrentHashMap<>(); // NOSONAR
    private final String unikScenarioId;

    public LokalIdentIndeks(String unikScenarioId, IdentGenerator identGenerator) {
        this.unikScenarioId = unikScenarioId;
        this.identGenerator = identGenerator;
    }

    public Map<String, String> getAlleIdenter(){
        return Collections.unmodifiableMap(identer);
    }

    public String getVoksenIdentForLokalIdent(String lokalIdent, BrukerModell.Kjønn kjønn) {
        if (lokalIdent.matches(REGEX_MATCH_PATTERN)) {
            return identer.computeIfAbsent(key(lokalIdent), i -> lokalIdent);
        }
        return identer.computeIfAbsent(key(lokalIdent), i -> kjønn == BrukerModell.Kjønn.M ? identGenerator.tilfeldigMannFnr() : identGenerator.tilfeldigKvinneFnr());
    }

    private String key(String lokalIdent) {
        return unikScenarioId + "::" + lokalIdent;
    }

    public String getBarnIdentForLokalIdent(String lokalIdent) {
        if (lokalIdent.matches(REGEX_MATCH_PATTERN)) {
            return identer.computeIfAbsent(key(lokalIdent), i -> lokalIdent);
        }
        // tilfeldig kjønn
        return identer.computeIfAbsent(key(lokalIdent), i -> identGenerator.tilfeldigBarnUnderTreAarFnr());
    }

    public String getIdentForDødfødsel(String lokalIdent, LocalDate dødsdato) {
        if (lokalIdent.matches(REGEX_MATCH_PATTERN)) {
            return identer.computeIfAbsent(key(lokalIdent), i -> lokalIdent);
        }
        // TODO: Legg til iterator hvis det er tvillinger eller trillinger.
        return identer.computeIfAbsent(key(lokalIdent), i -> dødsdato.format(DateTimeFormatter.ofPattern("ddMMyy")) + "00001");
    }

    public String getIdent(String lokalIdent) {
        String key = key(lokalIdent);
        return identer.get(key);
    }
}
