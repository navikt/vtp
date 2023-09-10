package no.nav.foreldrepenger.vtp.testmodell.personopplysning;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UstrukturertAdresseModell extends AdresseModell {
    private String adresseLinje1;
    private String adresseLinje2;
    private String adresseLinje3;
    private String adresseLinje4;
    private String postNr;
    private String poststed;

    public UstrukturertAdresseModell(UstrukturertAdresseModell u) {
        this(u.getFom(), u.getTom(), u.getEndringstype(), u.getEndringstidspunkt(), u.getAdresseType(), u.getLand(), u.getMatrikkelId(),
                u.getAdresseLinje1(), u.getAdresseLinje2(), u.getAdresseLinje3(), u.getAdresseLinje4(), u.getPostNr(), u.getPoststed());
    }

    @JsonCreator
    public UstrukturertAdresseModell(@JsonProperty("fom") LocalDate fom,
                                     @JsonProperty("tom") LocalDate tom,
                                     @JsonProperty("endringstype") Endringstype endringstype,
                                     @JsonProperty("endringstidspunkt") LocalDate endringstidspunkt,
                                     @JsonProperty("adresseType") AdresseType adresseType,
                                     @JsonProperty("land") Landkode land,
                                     @JsonProperty("matrikkelId") String matrikkelId,
                                     @JsonProperty("adresseLinje1") String adresseLinje1,
                                     @JsonProperty("adresseLinje2") String adresseLinje2,
                                     @JsonProperty("adresseLinje3") String adresseLinje3,
                                     @JsonProperty("adresseLinje4") String adresseLinje4,
                                     @JsonProperty("postNr") String postNr,
                                     @JsonProperty("poststed") String poststed) {
        super(fom, tom, endringstype, endringstidspunkt, adresseType, land, matrikkelId);
        this.adresseLinje1 = adresseLinje1;
        this.adresseLinje2 = adresseLinje2;
        this.adresseLinje3 = adresseLinje3;
        this.adresseLinje4 = adresseLinje4;
        this.postNr = postNr;
        this.poststed = poststed;
    }

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
