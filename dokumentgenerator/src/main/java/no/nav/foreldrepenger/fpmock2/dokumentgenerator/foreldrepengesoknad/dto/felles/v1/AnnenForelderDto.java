package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "annenForelderDtoType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AnnenForelderMedNorskIdentDto.class),
        @JsonSubTypes.Type(value = AnnenForelderUtenNorskIdentDto.class),
        @JsonSubTypes.Type(value = UkjentForelderDto.class)
})

public abstract class AnnenForelderDto {
}
