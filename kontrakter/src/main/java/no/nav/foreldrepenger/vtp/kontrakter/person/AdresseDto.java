package no.nav.foreldrepenger.vtp.kontrakter.person;

import java.time.LocalDate;

import com.neovisionaries.i18n.CountryCode;

public record AdresseDto(AdresseType adresseType,
                         CountryCode land,
                         String matrikkelId,
                         LocalDate fom,
                         LocalDate tom) {

    public enum AdresseType {
        BOSTEDSADRESSE,
        POSTADRESSE,}
}
