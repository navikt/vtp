package no.nav.foreldrepenger.vtp.testmodell.personopplysning;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY)
@JsonSubTypes({ @Type(UstrukturertAdresseModell.class), @Type(GateadresseModell.class), @Type(AdresseRefModell.class), @Type(PostboksadresseModell.class) })
public abstract class AdresseModell extends Periodisert implements Cloneable {

    @JsonProperty("adresseType")
    private AdresseType adresseType;

    @JsonProperty("land")
    private Landkode land;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonProperty("matrikkelId")
    private String matrikkelId;

    public AdresseType getAdresseType() {
        return adresseType;
    }

    @JsonIgnore
    public String getLandkode() {
        return land!=null?land.getKode():null;
    }

    public Landkode getLand() {
        return land;
    }

    public void setAdresseType(AdresseType adresseType) {
        this.adresseType = adresseType;
    }

    public void setLand(Landkode landkode) {
        this.land = landkode;
    }

    public String getMatrikkelId() {
        return matrikkelId;
    }

    public void setMatrikkelId(String matrikkelId) {
        this.matrikkelId = matrikkelId;
    }
}
