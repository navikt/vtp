package no.nav.foreldrepenger.vtp.autotest.personhendelse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;

@ApiModel(value = "DødfødselhendelseDto")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(as = DødfødselhendelseDto.class)
public class DødfødselhendelseDto implements PersonhendelseDto {
    @JsonProperty("type")
    private static String TYPE = "dødfødselhendelse";

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
        return TYPE;
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
