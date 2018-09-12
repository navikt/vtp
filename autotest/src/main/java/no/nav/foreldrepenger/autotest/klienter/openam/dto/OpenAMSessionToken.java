package no.nav.foreldrepenger.autotest.klienter.openam.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenAMSessionToken {
    public String tokenId;
}