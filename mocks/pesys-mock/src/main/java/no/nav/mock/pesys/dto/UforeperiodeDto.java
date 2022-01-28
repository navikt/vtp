package no.nav.mock.pesys.dto;

import java.time.LocalDate;

public record UforeperiodeDto(Integer uforegrad,
                              LocalDate uforetidspunkt,
                              LocalDate virk,
                              UforeTypeCtiDto uforetype,
                              LocalDate ufgFom,
                              LocalDate ufgTom) {
}
