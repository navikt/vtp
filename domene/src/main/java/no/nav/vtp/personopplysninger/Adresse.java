package no.nav.vtp.personopplysninger;

import java.time.LocalDate;

import com.neovisionaries.i18n.CountryCode;

public record Adresse(AdresseType adresseType,
                      String matrikkelId, // Brukes eksempelvis til å registere forskjellig adresse på barn/forelder eller på foreldre.
                      CountryCode land,
                      LocalDate fom,
                      LocalDate tom) {

    public enum AdresseType {
        BOSTEDSADRESSE,
        POSTADRESSE
    }
}
