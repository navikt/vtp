package no.nav.foreldrepenger.vtp.kontrakter.person.v2;

import java.time.LocalDate;

public record AdresseDto(AdresseType adresseType,
                         String land,
                         String matrikkelId,
                         LocalDate fom,
                         LocalDate tom) {

    public enum AdresseType {
        BOSTEDSADRESSE,
        POSTADRESSE
    }
}
