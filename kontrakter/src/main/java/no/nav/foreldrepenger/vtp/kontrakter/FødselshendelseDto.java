package no.nav.foreldrepenger.vtp.kontrakter;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "FødselshendelseDto")
@JsonIgnoreProperties(ignoreUnknown = true)
public record FødselshendelseDto(@ApiModelProperty String endringstype,
                                 @ApiModelProperty String tidligereHendelseId,
                                 @ApiModelProperty String fnrMor,
                                 @ApiModelProperty String fnrFar,
                                 @ApiModelProperty String fnrBarn,
                                 @ApiModelProperty @JsonProperty("foedselsdato") LocalDate fødselsdato) implements PersonhendelseDto {
}

