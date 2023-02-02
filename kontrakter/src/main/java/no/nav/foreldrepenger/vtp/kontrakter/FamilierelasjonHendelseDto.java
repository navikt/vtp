package no.nav.foreldrepenger.vtp.kontrakter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "FamilierelasjonHendelseDto")
@JsonIgnoreProperties(ignoreUnknown = true)
public record FamilierelasjonHendelseDto(@ApiModelProperty String endringstype,
                                         @ApiModelProperty String fnr,
                                         @ApiModelProperty String relatertPersonsFnr,
                                         @ApiModelProperty String relatertPersonsRolle,
                                         @ApiModelProperty String minRolleForPerson) implements PersonhendelseDto {

}
