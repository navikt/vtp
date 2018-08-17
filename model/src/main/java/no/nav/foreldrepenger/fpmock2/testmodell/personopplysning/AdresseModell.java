package no.nav.foreldrepenger.fpmock2.testmodell.personopplysning;

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

    @JsonProperty("landkode")
    private String landkode;

    public AdresseType getAdresseType() {
        return adresseType;
    }

    public String getLandkode() {
        return landkode;
    }

    public void setAdresseType(AdresseType adresseType) {
        this.adresseType = adresseType;
    }

    public void setLandkode(String landkode) {
        this.landkode = landkode;
    }

}
