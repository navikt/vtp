package no.nav.foreldrepenger.vtp.kontrakter.person;

import java.time.LocalDate;

public record PermisjonDto(Integer stillingsprosent,
                           LocalDate fomGyldighetsperiode,
                           LocalDate tomGyldighetsperiode,
                           Permisjonstype permisjonstype) {
}
