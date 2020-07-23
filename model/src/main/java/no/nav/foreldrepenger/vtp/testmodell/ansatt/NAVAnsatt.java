package no.nav.foreldrepenger.vtp.testmodell.ansatt;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class NAVAnsatt {
    @JsonProperty
    public String cn;
    @JsonProperty
    public String givenname;
    @JsonProperty
    public String sn;
    @JsonProperty
    public String displayName;
    @JsonProperty
    public String email;
    @JsonProperty
    public List<String> groups;
    @JsonProperty
    public List<String> enheter;
}
