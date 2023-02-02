package no.nav.foreldrepenger.vtp.testmodell.virksomhet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** TestscenarioRepositoryImpl av alle virksomheter registrert. Konverterer alle lokale virksomheter til globale nr. */
public class VirksomhetIndeks {

    private final Map<String, VirksomhetModell> virksomheter = new ConcurrentHashMap<>(); // NOSONAR
    private final List<VirksomhetModell> modeller = Collections.synchronizedList(new ArrayList<>());

    public Map<String, VirksomhetModell> getAlleVirksomheter() {
        return Collections.unmodifiableMap(virksomheter);
    }

    public VirksomhetModell getVirksomhet(String orgnr) {
        return this.virksomheter.get(orgnr);
    }

    public void leggTil(List<VirksomhetModell> modeller) {
        this.modeller.addAll(modeller);
        modeller.forEach(v -> virksomheter.put(v.orgnr(), v));
    }

    public VirksomhetModell roundRobin(int round) {
        return modeller.get(round % modeller.size());
    }
}
