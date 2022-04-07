package no.nav.mock.pesys.dto;

import java.time.LocalDate;

public record HarUføreGrad(Boolean harUforegrad,
                           LocalDate datoUfor,
                           LocalDate virkDato) {
}
