package no.nav.foreldrepenger.vtp.testmodell.organisasjon;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
@JsonFormat(
        shape = JsonFormat.Shape.OBJECT
)
@JsonAutoDetect(
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.ANY
)
public class AdresseEReg {
    @JsonProperty("adresselinje1")
    private String adresselinje1;
    @JsonProperty("adresselinje2")
    private String adresselinje2;
    @JsonProperty("adresselinje3")
    private String adresselinje3;
    @JsonProperty("kommunenummer")
    private String kommunenummer;
    @JsonProperty("landkode")
    private String landkode;
    @JsonProperty("postnummer")
    private String postnummer;
    @JsonProperty("poststed")
    private String poststed;

    public AdresseEReg() {
    }

    public String getAdresselinje1() {
        return this.adresselinje1;
    }

    public String getAdresselinje2() {
        return this.adresselinje2;
    }

    public String getAdresselinje3() {
        return this.adresselinje3;
    }

    public String getKommunenummer() {
        return this.kommunenummer;
    }

    public String getLandkode() {
        return this.landkode;
    }

    public String getPostnummer() {
        return this.postnummer;
    }

    public String getPoststed() {
        return this.poststed;
    }

    public String toString() {
        return "Forretningsadresse{adresselinje1='" + this.adresselinje1 + "'}";
    }
}
