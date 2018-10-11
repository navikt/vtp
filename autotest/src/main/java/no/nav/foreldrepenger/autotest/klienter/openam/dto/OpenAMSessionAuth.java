package no.nav.foreldrepenger.autotest.klienter.openam.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenAMSessionAuth {
    public String authId;
}
