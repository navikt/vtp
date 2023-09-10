package no.nav.foreldrepenger.vtp.testmodell.personopplysning;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GateadresseModell extends AdresseModell {
    private String gatenavn;
    private Integer gatenummer;
    private String husbokstav;
    private Integer husnummer;
    private String postnummer;
    private String kommunenummer;

    public GateadresseModell(GateadresseModell g) {
        this(g.getFom(), g.getTom(), g.getEndringstype(), g.getEndringstidspunkt(), g.getAdresseType(), g.getLand(), g.getMatrikkelId(),
                g.getGatenavn(), g.getGatenummer(), g.getHusbokstav(), g.getHusnummer(), g.getPostnummer(), g.getKommunenummer());
    }

    @JsonCreator
    public GateadresseModell(@JsonProperty("fom") LocalDate fom,
                             @JsonProperty("tom") LocalDate tom,
                             @JsonProperty("endringstype") Endringstype endringstype,
                             @JsonProperty("endringstidspunkt") LocalDate endringstidspunkt,
                             @JsonProperty("adresseType") AdresseType adresseType,
                             @JsonProperty("land") Landkode land,
                             @JsonProperty("matrikkelId") String matrikkelId,
                             @JsonProperty("gatenavn")  String gatenavn,
                             @JsonProperty("gatenummer") Integer gatenummer,
                             @JsonProperty("husbokstav") String husbokstav,
                             @JsonProperty("husnummer") Integer husnummer,
                             @JsonProperty("postnummer") String postnummer,
                             @JsonProperty("kommunenummer") String kommunenummer) {
        super(fom, tom, endringstype, endringstidspunkt, adresseType, land, matrikkelId);
        this.gatenavn = gatenavn;
        this.gatenummer = gatenummer;
        this.husbokstav = husbokstav;
        this.husnummer = husnummer;
        this.postnummer = postnummer;
        this.kommunenummer = kommunenummer;
    }

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

}
