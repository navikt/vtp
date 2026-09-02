package no.nav.vtp.person.personopplysninger;

import java.time.LocalDate;

public record Adresse(AdresseType adresseType,
                      String matrikkelId, // Brukes eksempelvis til å registere forskjellig adresse på barn/forelder eller på foreldre.
                      String land,
                      LocalDate fom,
                      LocalDate tom) {

    public enum AdresseType {
        BOSTEDSADRESSE,
        POSTADRESSE
    }
}
