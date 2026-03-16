package no.nav.foreldrepenger.vtp.kontrakter;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FødselshendelseDto(String endringstype,
                                 String tidligereHendelseId,
                                 String fnrMor,
                                 String fnrFar,
                                 String fnrBarn,
                                 @JsonProperty("foedselsdato") LocalDate fødselsdato) implements PersonhendelseDto {
}

