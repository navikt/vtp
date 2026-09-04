package no.nav.vtp.person.personopplysninger;

import java.time.LocalDate;

public record Medlemskap(LocalDate fom,
                         LocalDate tom,
                         String land,
                         DekningsType trygdedekning) {

    public enum DekningsType {
        IHT_AVTALE,
        FULL,
    }
}
