package no.nav.vtp.person.ident;

public record PersonIdent(String fnr) implements Identifikator {

    public String aktørId() {
        return "99" + fnr();
    }

    @Override
    public String value() {
        return fnr;
    }
}
