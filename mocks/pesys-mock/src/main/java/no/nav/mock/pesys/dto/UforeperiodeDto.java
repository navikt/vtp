package no.nav.mock.pesys.dto;

import java.util.Date;

public record UforeperiodeDto(Integer uforegrad,
                              Date uforetidspunkt,
                              Date virk,
                              UforeTypeCtiDto uforetype,
                              Date ufgFom,
                              Date ufgTom) {
}
