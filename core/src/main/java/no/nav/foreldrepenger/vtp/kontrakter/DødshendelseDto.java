package no.nav.foreldrepenger.vtp.kontrakter;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "DødshendelseDto")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(as = DødshendelseDto.class)
public record DødshendelseDto(@JsonProperty("type") String type,
                              @ApiModelProperty @JsonProperty("endringstype") String endringstype,
                              @ApiModelProperty @JsonProperty("tidligereHendelseId") String tidligereHendelseId,
                              @ApiModelProperty @JsonProperty("fnr") String fnr,
                              @ApiModelProperty @JsonProperty("doedsdato") LocalDate doedsdato)
        implements PersonhendelseDto {

    public DødshendelseDto(String endringstype, String tidligereHendelseId, String fnr, LocalDate doedsdato) {
        this("dødshendelse", endringstype, tidligereHendelseId, fnr, doedsdato);
    }

    @Override
    public String getType(){return type;}
}
