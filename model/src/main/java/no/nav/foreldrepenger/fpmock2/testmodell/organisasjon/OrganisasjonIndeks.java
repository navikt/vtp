package no.nav.foreldrepenger.fpmock2.testmodell.organisasjon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OrganisasjonIndeks {
    private Map<String, OrganisasjonModell> organisasjoner = new HashMap<>();
    private List<OrganisasjonModell> modeller = new ArrayList<>();

    public synchronized Optional<OrganisasjonModell> getModellForIdent(String orgnr) {
        return Optional.ofNullable(organisasjoner.get(orgnr));
    }

    public synchronized OrganisasjonModell roundRobin(int round) {
        return modeller.get(round % modeller.size());
    }

    public synchronized void leggTil(List<OrganisasjonModell> modeller) {
        this.modeller.addAll(modeller);
        modeller.forEach(o -> organisasjoner.put(o.getOrgnummer(), o));
    }
}
