package no.nav.foreldrepenger.vtp.server.auth.rest.texas;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record AuthorizationDetails(
    String type,
    Consumer systemuser_org,
    List<String> systemuser_id,
    String system_id
) {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public record Consumer(
        String authority,
        @JsonProperty("ID") String id
    ) {}

}
