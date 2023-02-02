package no.nav.foreldrepenger.vtp.testmodell.ansatt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class AnsatteIndeks {

    private final Map<String, NAVAnsatt> ansatte = new ConcurrentHashMap<>();

    public void leggTil(List<NAVAnsatt> ansatte) {
        ansatte.forEach(ansatt -> this.ansatte.putIfAbsent(ansatt.cn(), ansatt));
    }

    public Collection<NAVAnsatt> alleAnsatte() {
        return ansatte.values();
    }

    public NAVAnsatt findByCn(String cn) {
        return ansatte.get(cn);
    }
}
