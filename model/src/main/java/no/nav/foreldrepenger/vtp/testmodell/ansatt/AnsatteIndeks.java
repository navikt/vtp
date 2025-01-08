package no.nav.foreldrepenger.vtp.testmodell.ansatt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
public class AnsatteIndeks {

    private final Map<String, NavAnsatt> ansatteByIdent = new ConcurrentHashMap<>();
    private final Map<UUID, NavAnsatt> ansatteById = new ConcurrentHashMap<>();

    public void leggTil(List<NavAnsatt> ansatte) {
        ansatte.forEach(ansatt -> {
            this.ansatteByIdent.putIfAbsent(ansatt.ident().toLowerCase(), ansatt);
            this.ansatteById.putIfAbsent(ansatt.oid(), ansatt);
        });
    }

    public Collection<NavAnsatt> alleAnsatte() {
        return ansatteByIdent.values();
    }

    public NavAnsatt findByIdent(String ident) {
        return ansatteByIdent.get(ident.toLowerCase());
    }

    public NavAnsatt findById(UUID id) {
        return ansatteById.get(id);
    }
}
