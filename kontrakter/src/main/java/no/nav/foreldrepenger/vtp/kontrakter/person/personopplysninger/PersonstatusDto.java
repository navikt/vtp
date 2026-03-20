package no.nav.foreldrepenger.vtp.kontrakter.person.personopplysninger;

import java.time.LocalDate;

public record PersonstatusDto(Type personstatus, LocalDate fom, LocalDate tom) {
    public enum Type {
        ABNR, ADNR, BOSA, DØD, FOSV, FØDR, UFUL, UREG, UTAN, UTPE, UTVA
    }
}

