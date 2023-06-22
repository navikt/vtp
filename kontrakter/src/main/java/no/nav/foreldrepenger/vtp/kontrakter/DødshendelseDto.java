package no.nav.foreldrepenger.vtp.kontrakter;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(name = "DødshendelseDto")
@JsonIgnoreProperties(ignoreUnknown = true)
public record DødshendelseDto(@Schema String endringstype,
                              @Schema String tidligereHendelseId,
                              @Schema String fnr,
                              @Schema LocalDate doedsdato) implements PersonhendelseDto {

}
