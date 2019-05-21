package no.nav.foreldrepenger.fpmock2.testmodell.personopplysning;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("postboksadresse")
public class PostboksadresseModell extends AdresseModell{
    
    @JsonProperty("postboksnummer")
    private String postboksnummer;
    
    @JsonProperty("poststed")
    private String poststed;
    
    @JsonProperty("postboksanlegg")
    private String postboksanlegg;

    public String getPostboksanlegg() {
        return postboksanlegg;
    }

    public String getPostboksnummer() {
        return postboksnummer;
    }

    public String getPoststed() {
        return poststed;
    }

    public void setPostboksanlegg(String postboksanlegg) {
        this.postboksanlegg = postboksanlegg;
    }

    public void setPostboksnummer(String postnummer) {
        this.postboksnummer = postnummer;
    }

    public void setPoststed(String poststed) {
        this.poststed = poststed;
    }

}
