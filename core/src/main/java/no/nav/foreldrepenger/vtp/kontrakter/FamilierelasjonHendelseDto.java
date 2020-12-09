package no.nav.foreldrepenger.vtp.kontrakter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "FamilierelasjonHendelseDto")
@JsonDeserialize(as = FamilierelasjonHendelseDto.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record FamilierelasjonHendelseDto(@JsonProperty("type") String type,
                                         @ApiModelProperty @JsonProperty("endringstype") String endringstype,
                                         @ApiModelProperty @JsonProperty("fnr") String fnr,
                                         @ApiModelProperty  @JsonProperty("relatertPersonsFnr") String relatertPersonsFnr,
                                         @ApiModelProperty @JsonProperty("relatertPersonsRolle") String relatertPersonsRolle,
                                         @ApiModelProperty @JsonProperty("minRolleForPerson") String minRolleForPerson)
        implements PersonhendelseDto {

    public FamilierelasjonHendelseDto(String endringstype, String fnr, String relatertPersonsFnr, String relatertPersonsRolle,String minRolleForPerson) {
        this("familierelasjonshendelse", endringstype, fnr, relatertPersonsFnr, relatertPersonsRolle, minRolleForPerson);
    }

    @Override
    public String getType(){return type;}

}
