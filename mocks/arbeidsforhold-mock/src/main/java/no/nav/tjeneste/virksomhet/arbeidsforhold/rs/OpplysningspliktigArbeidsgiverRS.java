package no.nav.tjeneste.virksomhet.arbeidsforhold.rs;

public record OpplysningspliktigArbeidsgiverRS(Type type, String organisasjonsnummer, String aktoerId, String offentligIdent) {

    public enum Type {
        Organisasjon,
        Person
    }

    public static OpplysningspliktigArbeidsgiverRS fraOrgnrEllerAktørId(String organisasjonsnummer, String aktoerId) {
        var type = organisasjonsnummer != null ? Type.Organisasjon : Type.Person;
        String aktorIdVerdi = null;
        String offentligIdent = null;
        if (aktoerId != null) {
            if (aktoerId.length() == 13) {
                aktorIdVerdi = aktoerId;
            } else if (aktoerId.length() == 11) {
                offentligIdent = aktoerId;
            }
        }
        return new OpplysningspliktigArbeidsgiverRS(type, organisasjonsnummer, aktorIdVerdi, offentligIdent);
    }
}
