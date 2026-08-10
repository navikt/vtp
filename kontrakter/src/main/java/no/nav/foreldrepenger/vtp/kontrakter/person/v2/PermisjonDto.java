package no.nav.foreldrepenger.vtp.kontrakter.person.v2;

import java.time.LocalDate;

import no.nav.foreldrepenger.vtp.kontrakter.person.Permisjonstype;

public record PermisjonDto(Integer stillingsprosent,
                           LocalDate fomGyldighetsperiode,
                           LocalDate tomGyldighetsperiode,
                           Permisjonstype permisjonstype) {
}
