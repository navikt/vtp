package no.nav.foreldrepenger.vtp.kontrakter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "FamilierelasjonHendelseDto")
@JsonDeserialize(as = FamilierelasjonHendelseDto.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FamilierelasjonHendelseDto implements PersonhendelseDto {
    @JsonProperty("type")
    private String type = "familierelasjonshendelse";

    @ApiModelProperty
    @JsonProperty("endringstype")
    private String endringstype;

    @ApiModelProperty
    @JsonProperty("fnr")
    private String fnr;

    @ApiModelProperty
    @JsonProperty("relatertPersonsFnr")
    private String relatertPersonsFnr;

    @ApiModelProperty
    @JsonProperty("relatertPersonsRolle")
    private String relatertPersonsRolle;

    @ApiModelProperty
    @JsonProperty("minRolleForPerson")
    private String minRolleForPerson;

    public FamilierelasjonHendelseDto() {
    }

    @Override
    public String getType(){return type;}

    public String getEndringstype() {
        return endringstype;
    }

    public String getFnr() {
        return fnr;
    }

    public void setFnr(String fnr) {
        this.fnr = fnr;
    }

    public String getRelatertPersonsFnr() {
        return relatertPersonsFnr;
    }

    public void setRelatertPersonsFnr(String relatertPersonsFnr) {
        this.relatertPersonsFnr = relatertPersonsFnr;
    }

    public String getRelatertPersonsRolle() {
        return relatertPersonsRolle;
    }

    public void setRelatertPersonsRolle(String relatertPersonsRolle) {
        this.relatertPersonsRolle = relatertPersonsRolle;
    }

    public String getMinRolleForPerson() {
        return minRolleForPerson;
    }

    public void setMinRolleForPerson(String minRolleForPerson) {
        this.minRolleForPerson = minRolleForPerson;
    }
}
