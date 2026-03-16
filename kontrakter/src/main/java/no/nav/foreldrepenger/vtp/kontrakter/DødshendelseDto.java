package no.nav.foreldrepenger.vtp.kontrakter;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public record DødshendelseDto(String endringstype,
                              String tidligereHendelseId,
                              String fnr,
                              LocalDate doedsdato) implements PersonhendelseDto {

}
