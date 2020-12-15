package no.nav.foreldrepenger.vtp.testmodell.felles;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import no.nav.foreldrepenger.vtp.testmodell.util.ProsentDeserializer;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonDeserialize(using = ProsentDeserializer.class)
public record Prosent(Integer prosent) {

    @JsonValue
    public Integer prosent() {
        return prosent;
    }
}
