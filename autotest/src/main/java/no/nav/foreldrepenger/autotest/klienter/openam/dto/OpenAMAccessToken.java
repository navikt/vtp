package no.nav.foreldrepenger.autotest.klienter.openam.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenAMAccessToken {
    public String access_token;
    public String refresh_token;
    public String id_token;
    public String scope;
}