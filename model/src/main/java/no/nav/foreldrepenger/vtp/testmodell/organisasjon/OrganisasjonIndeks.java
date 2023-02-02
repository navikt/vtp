package no.nav.foreldrepenger.vtp.testmodell.organisasjon;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class OrganisasjonIndeks {
    private final Map<String, OrganisasjonModell> organisasjoner = new ConcurrentHashMap<>();

    public Optional<OrganisasjonModell> getModellForIdent(String orgnr) {
        return Optional.ofNullable(organisasjoner.get(orgnr));
    }

    public void leggTil(List<OrganisasjonModell> modeller) {
        modeller.forEach(o -> organisasjoner.put(o.orgnummer(), o));
    }
}
