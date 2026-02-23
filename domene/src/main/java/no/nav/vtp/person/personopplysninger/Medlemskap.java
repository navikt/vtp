package no.nav.vtp.person.personopplysninger;

import java.time.LocalDate;

import com.neovisionaries.i18n.CountryCode;

public record Medlemskap(LocalDate fom,
                         LocalDate tom,
                         CountryCode land,
                         DekningsType trygdedekning) {

    public enum DekningsType {
        IHT_AVTALE,
        FULL,
    }
}
