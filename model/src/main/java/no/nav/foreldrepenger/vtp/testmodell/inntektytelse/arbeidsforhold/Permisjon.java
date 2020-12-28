package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Permisjon(Integer stillingsprosent,
                        LocalDate fomGyldighetsperiode,
                        LocalDate tomGyldighetsperiode,
                        @JsonProperty("permisjonType") Permisjonstype permisjonstype) {
}
