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
public class FødselshendelseDto implements PersonhendelseDto {
    @JsonProperty("type")
    private String type = "fødselshendelse";

    @ApiModelProperty
    @JsonProperty("fnrMor")
    private String fnrMor;

    @ApiModelProperty
    @JsonProperty("fnrFar")
    private String fnrFar;

    @ApiModelProperty
    @JsonProperty("fnrBarn")
    private String fnrBarn;

    @ApiModelProperty
    @JsonProperty("foedselsdato")
    private LocalDate fødselsdato;



    public FødselshendelseDto() {
    }

    @Override
    public String getType(){return type;}

    public String getFnrMor() {
        return fnrMor;
    }

    public void setFnrMor(String fnrMor) {
        this.fnrMor = fnrMor;
    }

    public String getFnrFar() {
        return fnrFar;
    }

    public void setFnrFar(String fnrFar) {
        this.fnrFar = fnrFar;
    }

    public String getFnrBarn() {
        return fnrBarn;
    }

    public void setFnrBarn(String fnrBarn) {
        this.fnrBarn = fnrBarn;
    }

    public LocalDate getFødselsdato() {
        return fødselsdato;
    }

    public void setFødselsdato(LocalDate fødselsdato) {
        this.fødselsdato = fødselsdato;
    }
}
