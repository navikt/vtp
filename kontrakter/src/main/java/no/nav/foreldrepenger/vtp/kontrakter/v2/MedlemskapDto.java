package no.nav.foreldrepenger.vtp.kontrakter.v2;

import java.time.LocalDate;

import com.neovisionaries.i18n.CountryCode;

public record MedlemskapDto(LocalDate fom,
                            LocalDate tom,
                            CountryCode land,
                            DekningsType trygdedekning) {

    public enum DekningsType {
        IHT_AVTALE,
        FULL,
    }
}
