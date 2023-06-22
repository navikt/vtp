package no.nav.foreldrepenger.vtp.kontrakter;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "FødselshendelseDto")
@JsonIgnoreProperties(ignoreUnknown = true)
public record FødselshendelseDto(@Schema String endringstype,
                                 @Schema String tidligereHendelseId,
                                 @Schema String fnrMor,
                                 @Schema String fnrFar,
                                 @Schema String fnrBarn,
                                 @Schema @JsonProperty("foedselsdato") LocalDate fødselsdato) implements PersonhendelseDto {
}

