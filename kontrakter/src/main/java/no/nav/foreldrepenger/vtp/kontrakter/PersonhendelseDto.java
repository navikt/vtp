package no.nav.foreldrepenger.vtp.kontrakter;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = NAME, include = PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FødselshendelseDto.class, name = "fødselshendelse"),
        @JsonSubTypes.Type(value = FamilierelasjonHendelseDto.class, name = "familierelasjonshendelse"),
        @JsonSubTypes.Type(value = DødshendelseDto.class, name = "dødshendelse"),
        @JsonSubTypes.Type(value = DødfødselhendelseDto.class, name = "dødfødselhendelse")
})
public interface PersonhendelseDto {

}
