package no.nav.foreldrepenger.vtp.kontrakter.v2;

import java.time.LocalDate;

public record PermisjonDto(Integer stillingsprosent,
                           LocalDate fomGyldighetsperiode,
                           LocalDate tomGyldighetsperiode,
                           Permisjonstype permisjonstype) {
}