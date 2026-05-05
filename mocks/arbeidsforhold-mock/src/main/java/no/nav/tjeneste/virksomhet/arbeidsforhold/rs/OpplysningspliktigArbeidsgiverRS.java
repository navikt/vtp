package no.nav.tjeneste.virksomhet.arbeidsforhold.rs;

public record OpplysningspliktigArbeidsgiverRS(Type type, String organisasjonsnummer, String aktoerId, String offentligIdent) {

    public enum Type {
        Organisasjon,
        Person
    }
}
