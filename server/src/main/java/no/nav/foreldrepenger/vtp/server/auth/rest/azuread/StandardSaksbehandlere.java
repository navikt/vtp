package no.nav.foreldrepenger.vtp.server.auth.rest.azuread;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public enum StandardSaksbehandlere {
    SAKSBEHANDLER("saksbeh", "Sara Saksbehandler", true, Set.of()),
    SAKSBEHANDLER_K6("saksbeh6", "Sa6a Saksbehandler", true, Set.of("kode6")),
    SAKSBEHANDLER_K7("saksbeh7", "Sa7a Saksbehandler", true, Set.of("kode7")),
    SAKSBEHANDLER_HAB("saksbha", "SaHa Saksbehandler", true, Set.of("egenansatt")),
    KLAGEBEHANDLER("klageb", "Klara Klagebehandler", true, Set.of()),
    BESLUTTER("beslut", "Birger Beslutter", true, Set.of("beslutter")),
    OVERSTYRER("oversty", "Ove Overstyrer", true, Set.of("overstyrer")),
    OPPGAVESTYRER("oppgs", "Oline Oppgavestyrer", true, Set.of("oppgavestyrer")),
    VEILEDER("veil", "Vegard Veileder", false, Set.of("veileder")),
    DRIFT("drift", "Daniel Drifter", false, Set.of("drift", "veileder"));

    private final String ident;
    private final String navn;
    private final Set<String> grupper;

    StandardSaksbehandlere(String ident, String navn, boolean erSaksbehandler, Set<String> grupper) {
        this.ident = ident;
        this.navn = navn;
        this.grupper = new HashSet<>(grupper);
        if (erSaksbehandler) {
            this.grupper.add("saksbehandler");
        }
    }

    public String getIdent() {
        return ident;
    }

    public String getNavn() {
        return navn;
    }

    public Set<String> getGrupper() {
        return grupper;
    }

    public static StandardSaksbehandlere finnIdent(String ident) {
        for (var v : values()) {
            if (Objects.equals(ident, v.ident)) {
                return v;
            }
        }
        return null;
    }
}
