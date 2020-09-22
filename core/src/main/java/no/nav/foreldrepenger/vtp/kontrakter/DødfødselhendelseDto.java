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
public class DødfødselhendelseDto implements PersonhendelseDto {
    @JsonProperty("type")
    private String type = "dødfødselhendelse";

    @ApiModelProperty
    @JsonProperty("endringstype")
    private String endringstype;

    @ApiModelProperty
    @JsonProperty("tidligereHendelseId")
    private String tidligereHendelseId;

    @ApiModelProperty
    @JsonProperty("fnr")
    private String fnr;

    @ApiModelProperty
    @JsonProperty("doedfoedselsdato")
    private LocalDate doedfoedselsdato;

    public DødfødselhendelseDto() {
    }

    @Override
    public String getType() {
        return type;
    }

    public String getEndringstype() {
        return endringstype;
    }

    public void setEndringstype(String endringstype) {
        this.endringstype = endringstype;
    }

    public String getTidligereHendelseId() {
        return tidligereHendelseId;
    }

    public void setTidligereHendelseId(String tidligereHendelseId) {
        this.tidligereHendelseId = tidligereHendelseId;
    }
    public String getFnr() {
        return fnr;
    }

    public void setFnr(String fnr) {
        this.fnr = fnr;
    }

    public LocalDate getDoedfoedselsdato() {
        return doedfoedselsdato;
    }

    public void setDoedfoedselsdato(LocalDate doedfoedselsdato) {
        this.doedfoedselsdato = doedfoedselsdato;
    }
}
