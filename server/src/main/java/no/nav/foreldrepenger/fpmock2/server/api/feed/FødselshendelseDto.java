package no.nav.foreldrepenger.fpmock2.server.api.feed;

import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "FødselshendelseDto")
public class FødselshendelseDto implements PersonhendelseDto {
    private String type = "fødselshendelse";

    @ApiModelProperty
    private String fnrMor;
    @ApiModelProperty
    private String fnrFar;
    @ApiModelProperty
    private String fnrBarn;
    @ApiModelProperty
    private LocalDate fødselsdato;

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
