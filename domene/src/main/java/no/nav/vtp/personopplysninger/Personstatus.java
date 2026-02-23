package no.nav.vtp.personopplysninger;

import java.time.LocalDate;

public record Personstatus(Personstatuser personstatus, LocalDate fom, LocalDate tom) {

      public enum Personstatuser {
        ABNR, ADNR, BOSA, DØD, FOSV, FØDR, UFUL, UREG, UTAN, UTPE, UTVA;
    }
}
