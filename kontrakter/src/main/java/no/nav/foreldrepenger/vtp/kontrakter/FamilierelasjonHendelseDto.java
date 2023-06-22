package no.nav.foreldrepenger.vtp.kontrakter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "FamilierelasjonHendelseDto")
@JsonIgnoreProperties(ignoreUnknown = true)
public record FamilierelasjonHendelseDto(@Schema String endringstype,
                                         @Schema String fnr,
                                         @Schema String relatertPersonsFnr,
                                         @Schema String relatertPersonsRolle,
                                         @Schema String minRolleForPerson) implements PersonhendelseDto {

}
