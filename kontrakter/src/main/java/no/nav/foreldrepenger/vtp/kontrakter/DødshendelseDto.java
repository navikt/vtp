package no.nav.foreldrepenger.vtp.kontrakter;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "DødshendelseDto")
@JsonIgnoreProperties(ignoreUnknown = true)
public record DødshendelseDto(@ApiModelProperty String endringstype,
                              @ApiModelProperty String tidligereHendelseId,
                              @ApiModelProperty String fnr,
                              @ApiModelProperty LocalDate doedsdato) implements PersonhendelseDto {

}
