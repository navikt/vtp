package no.nav.foreldrepenger.vtp.testmodell.ansatt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
public class AnsatteIndeks {

    private final Map<String, NAVAnsatt> ansatteByIdent = new ConcurrentHashMap<>();
    private final Map<UUID, NAVAnsatt> ansatteById = new ConcurrentHashMap<>();

    public void leggTil(List<NAVAnsatt> ansatte) {
        ansatte.forEach(ansatt -> {
            this.ansatteByIdent.putIfAbsent(ansatt.ident().toLowerCase(), ansatt);
            this.ansatteById.putIfAbsent(ansatt.oid(), ansatt);
        });
    }

    public Collection<NAVAnsatt> alleAnsatte() {
        return ansatteByIdent.values();
    }

    public NAVAnsatt findByIdent(String ident) {
        return ansatteByIdent.get(ident.toLowerCase());
    }

    public NAVAnsatt findById(UUID id) {
        return ansatteById.get(id);
    }
}
