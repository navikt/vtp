package no.nav.vtp.personopplysninger;

import java.time.LocalDate;

public record Personstatus(Type personstatus, LocalDate fom, LocalDate tom) {

      public enum Type {
        ABNR, ADNR, BOSA, DØD, FOSV, FØDR, UFUL, UREG, UTAN, UTPE, UTVA;
    }
}
