package no.nav.foreldrepenger.vtp.kontrakter;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "FødselshendelseDto")
@JsonDeserialize(as = FødselshendelseDto.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record FødselshendelseDto(@JsonProperty("type") String type,
                                 @ApiModelProperty @JsonProperty("endringstype") String endringstype,
                                 @ApiModelProperty @JsonProperty("tidligereHendelseId") String tidligereHendelseId,
                                 @ApiModelProperty @JsonProperty("fnrMor") String fnrMor,
                                 @ApiModelProperty @JsonProperty("fnrFar") String fnrFar,
                                 @ApiModelProperty @JsonProperty("fnrBarn") String fnrBarn,
                                 @ApiModelProperty @JsonProperty("foedselsdato")LocalDate fødselsdato)
        implements PersonhendelseDto {

    public FødselshendelseDto(String endringstype, String tidligereHendelseId, String fnrMor, String fnrFar, String fnrBarn, LocalDate fødselsdato) {
        this("fødselshendelse", endringstype, tidligereHendelseId, fnrMor, fnrFar, fnrBarn, fødselsdato);
    }

    @Override
    public String getType(){return type;}
}

