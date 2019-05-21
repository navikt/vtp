package no.nav.foreldrepenger.fpmock2.kontrakter;

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
    private static String TYPE = "dødshendelse";

    @ApiModelProperty
    @JsonProperty("fnr")
    private String fnr;

    @ApiModelProperty
    @JsonProperty("doedsdato")
    private LocalDate doedsdato;


    public DødshendelseDto() {
    }


    @Override
    public String getType(){return TYPE;}

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
