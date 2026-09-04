package no.nav.vtp.ansatt;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import no.nav.foreldrepenger.vtp.kontrakter.organisasjon.NavAnsattDto;
import no.nav.foreldrepenger.vtp.kontrakter.organisasjon.NavGruppeDto;

public class AnsatteIndeks {

    private AnsatteIndeks() {
    }

    private static final Map<String, NavAnsatt> ansatteByIdent = new ConcurrentHashMap<>();
    private static final Map<UUID, NavAnsatt> ansatteById = new ConcurrentHashMap<>();

    private static final Map<String, NavGruppe> grupperByNavn = new ConcurrentHashMap<>();
    private static final Map<UUID, NavGruppe> grupperById = new ConcurrentHashMap<>();

    public static Collection<NavAnsatt> alleAnsatte() {
        return ansatteByIdent.values();
    }

    public static NavAnsatt findByIdent(String ident) {
        return ansatteByIdent.get(ident.toLowerCase());
    }

    public static NavAnsatt findById(UUID id) {
        return ansatteById.get(id);
    }

    public static NavGruppe gruppeByNavn(String navn) {
        return grupperByNavn.get(navn.toLowerCase());
    }

    public static NavGruppe gruppeById(UUID id) {
        return grupperById.get(id);
    }

    public static void leggTilGrupper(Collection<NavGruppeDto> grupper, boolean erstatt) {
        if (erstatt) {
            grupperById.clear();
            grupperByNavn.clear();
        }
        grupper.stream()
                .filter(g -> erstatt || grupperById.get(g.oid()) == null)
                .map(g -> new NavGruppe(g.oid(), g.navn()))
                .forEach(g -> {
                    grupperById.put(g.oid(), g);
                    grupperByNavn.put(g.name().toLowerCase(), g);
            });
    }

    public static void leggTilAnsatte(Collection<NavAnsattDto> ansatte, boolean erstatt) {
        if (erstatt) {
            ansatteById.clear();
            ansatteByIdent.clear();
        }
        ansatte.stream()
                .filter(a -> erstatt || ansatteByIdent.get(a.ident()) == null)
                .map(a -> new NavAnsatt(a, a.grupper().stream().map(AnsatteIndeks::gruppeByNavn).toList()))
                .forEach(a -> {
                    ansatteById.put(a.oid(), a);
                    ansatteByIdent.put(a.ident().toLowerCase(), a);
                });
    }

}
