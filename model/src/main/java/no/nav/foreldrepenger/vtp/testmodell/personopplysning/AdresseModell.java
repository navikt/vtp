package no.nav.foreldrepenger.vtp.testmodell.personopplysning;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = NAME)
@JsonSubTypes({
        @Type(value = UstrukturertAdresseModell.class, name = "ustrukturert"),
        @Type(value = GateadresseModell.class, name = "gateadresse"),
        @Type(value = AdresseRefModell.class, name = "ref"),
        @Type(value = PostboksadresseModell.class, name = "postboksadresse")
})
public abstract class AdresseModell extends Periodisert {
    private AdresseType adresseType;
    private Landkode land;
    private String matrikkelId;

    protected AdresseModell() {
    }

    protected AdresseModell(LocalDate fom, LocalDate tom, Endringstype endringstype, LocalDate endringstidspunkt,
                            AdresseType adresseType, Landkode land, String matrikkelId) {
        super(fom, tom, endringstype, endringstidspunkt);
        this.adresseType = adresseType;
        this.land = land;
        this.matrikkelId = matrikkelId;
    }

    public AdresseType getAdresseType() {
        return adresseType;
    }

    @JsonIgnore
    public String getLandkode() {
        return land !=null ? land.getKode() : null;
    }

    public Landkode getLand() {
        return land;
    }

    public String getMatrikkelId() {
        return matrikkelId;
    }

    public void setAdresseType(AdresseType adresseType) {
        this.adresseType = adresseType;
    }

    public void setLand(Landkode landkode) {
        this.land = landkode;
    }

    public void setMatrikkelId(String matrikkelId) {
        this.matrikkelId = matrikkelId;
    }
}
