package no.nav.vtp.arbeidsforhold;

import java.time.LocalDate;

public record Permisjon(LocalDate fom,
                        LocalDate tom,
                        Integer stillingsprosent,
                        Permisjonstype permisjonstype) {

    public enum Permisjonstype {
        PERMISJON,
        PERMISJON_MED_FORELDREPENGER,
        PERMISJON_VED_MILITÆRTJENESTE,
        PERMITTERING,

        UTDANNINGSPERMISJON,
        UTDANNINGSPERMISJON_IKKE_LOVFESTET,
        UTDANNINGSPERMISJON_LOVFESTET,
        VELFERDSPERMISJON,
        ANNEN_PERMISJON_IKKE_LOVFESTET,
        ANNEN_PERMISJON_LOVFESTET
    }
}
