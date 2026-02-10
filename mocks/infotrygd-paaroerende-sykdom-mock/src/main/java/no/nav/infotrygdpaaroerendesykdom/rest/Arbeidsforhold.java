package no.nav.infotrygdpaaroerendesykdom.rest;

import java.math.BigDecimal;


public record Arbeidsforhold(String arbeidsgiverOrgnr, BigDecimal inntektForPerioden, Kodeverdi inntektsperiode, Boolean refusjon) {

}
