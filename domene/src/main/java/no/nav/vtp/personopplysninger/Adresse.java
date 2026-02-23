package no.nav.vtp.personopplysninger;

import java.time.LocalDate;

import com.neovisionaries.i18n.CountryCode;

public record Adresse(AdresseType adresseType,
                      CountryCode land,
                      String matrikkelId,
                      LocalDate fom,
                      LocalDate tom) {

    public enum AdresseType {
        BOSTEDSADRESSE,
        POSTADRESSE,
        MIDLERTIDIG_POSTADRESSE,
        UKJENT_ADRESSE;
    }
}
