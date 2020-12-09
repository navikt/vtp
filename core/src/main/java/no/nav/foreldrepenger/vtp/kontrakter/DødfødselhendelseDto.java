package no.nav.foreldrepenger.vtp.kontrakter;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "DødfødselhendelseDto")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(as = DødfødselhendelseDto.class)
public record DødfødselhendelseDto(@JsonProperty("type") String type,
                                   @ApiModelProperty @JsonProperty("endringstype") String endringstype,
                                   @ApiModelProperty @JsonProperty("tidligereHendelseId") String tidligereHendelseId,
                                   @ApiModelProperty @JsonProperty("fnr") String fnr,
                                   @ApiModelProperty @JsonProperty("doedfoedselsdato") LocalDate doedfoedselsdato)
        implements PersonhendelseDto {

    public DødfødselhendelseDto(String endringstype, String tidligereHendelseId, String fnr, LocalDate doedfoedselsdato) {
        this("dødfødselhendelse", endringstype, tidligereHendelseId, fnr, doedfoedselsdato);
    }

    @Override
    public String getType() {
        return type;
    }
}
