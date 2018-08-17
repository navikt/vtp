package no.nav.foreldrepenger.fpmock2.testmodell.personopplysning;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("ustrukturert")
public class UstrukturertAdresseModell extends AdresseModell {

    @JsonProperty("adresseLinje1")
    private String adresseLinje1;

    @JsonProperty("adresseLinje2")
    private String adresseLinje2;

    @JsonProperty("adresseLinje3")
    private String adresseLinje3;

    @JsonProperty("adresseLinje4")
    private String adresseLinje4;

    @JsonProperty("postNr")
    private String postNr;

    @JsonProperty("poststed")
    private String poststed;

    public String getAdresseLinje1() {
        return adresseLinje1;
    }

    public String getAdresseLinje2() {
        return adresseLinje2;
    }

    public String getAdresseLinje3() {
        return adresseLinje3;
    }

    public String getAdresseLinje4() {
        return adresseLinje4;
    }

    public String getPostNr() {
        return postNr;
    }

    public String getPoststed() {
        return poststed;
    }
   
    public void setAdresseLinje1(String adresseLinje1) {
        this.adresseLinje1 = adresseLinje1;
    }

    public void setAdresseLinje2(String adresseLinje2) {
        this.adresseLinje2 = adresseLinje2;
    }

    public void setAdresseLinje3(String adresseLinje3) {
        this.adresseLinje3 = adresseLinje3;
    }

    public void setAdresseLinje4(String adresseLinje4) {
        this.adresseLinje4 = adresseLinje4;
    }

    public void setPostNr(String postNr) {
        this.postNr = postNr;
    }

    public void setPoststed(String poststed) {
        this.poststed = poststed;
    }

}
