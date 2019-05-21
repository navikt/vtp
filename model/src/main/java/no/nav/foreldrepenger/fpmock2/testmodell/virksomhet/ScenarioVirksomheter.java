package no.nav.foreldrepenger.fpmock2.testmodell.virksomhet;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/** konverterer lokale virksomheter orgnr brukt i testcase til utvalgte virksomheter hentet fra syntetisk liste. */
public class ScenarioVirksomheter {

    private final Map<String, VirksomhetModell> virksomheter = new ConcurrentHashMap<>();
    private final String scenario;
    private final VirksomhetIndeks virksomhetIndeks;

    // for å plukke en tilfeldig neste virksomhet hvis ikke initialisert.
    private final AtomicInteger roundRobinIndeks = new AtomicInteger(0);

    public ScenarioVirksomheter(String scenario, VirksomhetIndeks virksomhetIndeks) {
        this.scenario = scenario;
        this.virksomhetIndeks = virksomhetIndeks;
    }

    public Map<String, VirksomhetModell> getAlleVirksomheter() {
        return Collections.unmodifiableMap(virksomheter);
    }

    public VirksomhetModell getVirksomhet(String lokalOrgnr) {
        if (lokalOrgnr.matches("^\\d+$")) {
            return virksomheter.computeIfAbsent(key(lokalOrgnr), i -> {
                if (virksomhetIndeks.getVirksomhet(i) != null) {
                    return virksomhetIndeks.getVirksomhet(i);
                } else {
                    return virksomhetIndeks.roundRobin(roundRobinIndeks.getAndIncrement());
                }
            });
        }
        return virksomheter.computeIfAbsent(key(lokalOrgnr), i -> virksomhetIndeks.roundRobin(roundRobinIndeks.getAndIncrement()));
    }

    private String key(String lokalOrgnr) {
        return scenario + "::" + lokalOrgnr;
    }

}
