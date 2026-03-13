package no.nav.foreldrepenger.vtp.kontrakter;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DødfødselhendelseDto(String endringstype,
                                   String tidligereHendelseId,
                                   String fnr,
                                   LocalDate doedfoedselsdato) implements PersonhendelseDto {
}
