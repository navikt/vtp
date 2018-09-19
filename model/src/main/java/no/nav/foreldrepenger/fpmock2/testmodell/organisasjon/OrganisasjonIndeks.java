package no.nav.foreldrepenger.fpmock2.testmodell.organisasjon;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OrganisasjonIndeks {
    private Map<String, OrganisasjonModell> byOrgnr = new HashMap<>();

    public Optional<OrganisasjonModell> getModellForIdent(String orgnr) {
        return Optional.ofNullable(byOrgnr.get(orgnr));
    }

    public void leggTil(OrganisasjonModell org) {
        byOrgnr.put(org.getOrgnummer(), org);
    }
}
