package no.nav.foreldrepenger.fpmock2.testmodell.personopplysning;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("gateadresse")
public class GateadresseModell extends AdresseModell {

    @JsonProperty("gatenavn")
    private String gatenavn;

    @JsonProperty("gatenummer")
    private Integer gatenummer;

    @JsonProperty("husbokstav")
    private String husbokstav;

    @JsonProperty("husnummer")
    private Integer husnummer;

    @JsonProperty("postnummer")
    private String postnummer;

    @JsonProperty("kommunenummer")
    private String kommunenummer;

    @JsonProperty("tilleggsadresse")
    private String tilleggsadresse;

    @JsonProperty("tilleggsadresseType")
    private String tilleggsadresseType;

    public String getGatenavn() {
        return gatenavn;
    }

    public void setGatenavn(String gatenavn) {
        this.gatenavn = gatenavn;
    }

    public Integer getGatenummer() {
        return gatenummer;
    }

    public void setGatenummer(Integer gatenummer) {
        this.gatenummer = gatenummer;
    }

    public String getHusbokstav() {
        return husbokstav;
    }

    public void setHusbokstav(String husbokstav) {
        this.husbokstav = husbokstav;
    }

    public Integer getHusnummer() {
        return husnummer;
    }

    public void setHusnummer(Integer husnummer) {
        this.husnummer = husnummer;
    }

    public String getPostnummer() {
        return postnummer;
    }

    public void setPostnummer(String postnummer) {
        this.postnummer = postnummer;
    }

    public String getKommunenummer() {
        return kommunenummer;
    }

    public void setKommunenummer(String kommunenummer) {
        this.kommunenummer = kommunenummer;
    }

    public String getTilleggsadresse() {
        return tilleggsadresse;
    }

    public String getTilleggsadresseType() {
        return tilleggsadresseType;
    }
}
