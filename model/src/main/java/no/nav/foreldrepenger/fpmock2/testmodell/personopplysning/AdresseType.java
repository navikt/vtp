package no.nav.foreldrepenger.fpmock2.testmodell.personopplysning;

public enum AdresseType {
    BOSTEDSADRESSE, POSTADRESSE, MIDLERTIDIG_POSTADRESSE, UKJENT_ADRESSE;

    public String getTpsKode(String landKode) {
        if(this==MIDLERTIDIG_POSTADRESSE) {
            return "NOR".equals(landKode)?"MIDLERTIDIG_POSTADRESSE_NORGE": "MIDLERTIDIG_POSTADRESSE_UTLAND";
        } else {
            return name();
        }
    }
}