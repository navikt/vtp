package no.nav.foreldrepenger.vtp.kontrakter;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "DødfødselhendelseDto")
@JsonIgnoreProperties(ignoreUnknown = true)
public record DødfødselhendelseDto(@Schema String endringstype,
                                   @Schema String tidligereHendelseId,
                                   @Schema String fnr,
                                   @Schema LocalDate doedfoedselsdato) implements PersonhendelseDto {
}
