package no.nav.foreldrepenger.vtp.testmodell.ansatt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnsatteIndeks {
    private List<NAVAnsatt> ansatte = new ArrayList<>();

    public List<NAVAnsatt> hentAlleAnsatte() {
        return ansatte;
    }

    public void leggTil(List<NAVAnsatt> ansatte) throws RuntimeException {
        this.ansatte.addAll(ansatte);
    }

    public Optional<NAVAnsatt> hentNAVAnsatt(String ident) {
        return ansatte.stream().filter(ansatt -> ansatt.cn.equals(ident)).findFirst();
    }
}
