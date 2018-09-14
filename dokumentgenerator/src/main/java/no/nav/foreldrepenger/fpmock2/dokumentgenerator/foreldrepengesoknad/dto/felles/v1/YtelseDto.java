package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.endringssoeknad.v1.EndringssoeknadDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.engangsstoenad.v1.EngangsstønadDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.foreldrepenger.v1.ForeldrepengerDto;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "ytelseDtoType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = EndringssoeknadDto.class),
        @JsonSubTypes.Type(value = ForeldrepengerDto.class),
        @JsonSubTypes.Type(value = EngangsstønadDto.class)
})


public abstract class YtelseDto {

}
