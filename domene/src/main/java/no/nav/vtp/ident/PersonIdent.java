package no.nav.vtp.ident;

public record PersonIdent(String ident) implements Identifikator {

    public String aktørId() {
        return "99" + ident();
    }
}
