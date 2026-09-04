package no.nav.foreldrepenger.vtp.kontrakter.person.v2;

import java.time.LocalDate;

public record MedlemskapDto(LocalDate fom,
                            LocalDate tom,
                            String land,
                            DekningsType trygdedekning) {

    public enum DekningsType {
        IHT_AVTALE,
        FULL
    }
}
