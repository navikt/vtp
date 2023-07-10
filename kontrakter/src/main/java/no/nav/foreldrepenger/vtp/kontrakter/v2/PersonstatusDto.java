package no.nav.foreldrepenger.vtp.kontrakter.v2;

import java.time.LocalDate;

public record PersonstatusDto(Personstatuser personstatus, LocalDate fom, LocalDate tom) {

      public enum Personstatuser {
        ABNR, ADNR, BOSA, DØD, FOSV, FØDR, UFUL, UREG, UTAN, UTPE, UTVA;
    }
}
