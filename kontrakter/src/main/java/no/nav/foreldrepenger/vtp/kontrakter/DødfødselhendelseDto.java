package no.nav.foreldrepenger.vtp.kontrakter;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "DødfødselhendelseDto")
@JsonIgnoreProperties(ignoreUnknown = true)
public record DødfødselhendelseDto(@ApiModelProperty String endringstype,
                                   @ApiModelProperty String tidligereHendelseId,
                                   @ApiModelProperty String fnr,
                                   @ApiModelProperty LocalDate doedfoedselsdato) implements PersonhendelseDto {
}
