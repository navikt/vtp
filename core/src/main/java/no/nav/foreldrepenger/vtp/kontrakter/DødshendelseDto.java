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
public class DødshendelseDto implements PersonhendelseDto {
    @JsonProperty("type")
    private String type = "dødshendelse";

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
    @JsonProperty("doedsdato")
    private LocalDate doedsdato;


    public DødshendelseDto() {
    }

    @Override
    public String getType(){return type;}

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

    public LocalDate getDoedsdato() {
        return doedsdato;
    }

    public void setDoedsdato(LocalDate doedsdato) {
        this.doedsdato = doedsdato;
    }


}
