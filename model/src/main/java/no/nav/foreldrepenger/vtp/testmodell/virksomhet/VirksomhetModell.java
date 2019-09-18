package no.nav.foreldrepenger.vtp.testmodell.virksomhet;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VirksomhetModell {

    @JsonProperty("orgnr")
    private String orgnr;

    @JsonProperty("navn")
    private String navn;

    public VirksomhetModell() {
    }

    public String getOrgnr() {
        return orgnr;
    }

    public void setOrgnr(String orgnr) {
        this.orgnr = orgnr;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

}
