package no.nav.foreldrepenger.vtp.testmodell.organisasjon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OrganisasjonIndeks {
    private Map<String, OrganisasjonModell> organisasjoner = new HashMap<>();
    private Map<String, OrganisasjonAdresseModell> organisasjonerAdresse = new HashMap<>();

    public synchronized Optional<OrganisasjonModell> getModellForIdent(String orgnr) {
        return Optional.ofNullable(organisasjoner.get(orgnr));
    }

    public synchronized Optional<OrganisasjonAdresseModell> getAdresseModellForIdent(String orgnr) {
        return Optional.ofNullable(organisasjonerAdresse.get(orgnr));
    }

    public synchronized void leggTil(List<OrganisasjonModell> modeller) {
        modeller.forEach(o -> organisasjoner.put(o.getOrgnummer(), o));
    }
    public synchronized void leggTilAdresse(List<OrganisasjonAdresseModell> modeller){
        modeller.forEach(o -> organisasjonerAdresse.put(o.getOrganisasjonsnummer(), o));
    }
}
