package no.nav.foreldrepenger.vtp.testmodell.personopplysning;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;

public class PostboksadresseModell extends AdresseModell {
    private String postboksnummer;
    private String poststed;
    private String postboksanlegg;

    public PostboksadresseModell(PostboksadresseModell p) {
        this(p.getFom(), p.getTom(), p.getEndringstype(), p.getEndringstidspunkt(), p.getAdresseType(), p.getLand(), p.getMatrikkelId(),
                p.getPostboksnummer(), p.getPoststed(), p.getPostboksanlegg());
    }

    @JsonCreator
    public PostboksadresseModell(LocalDate fom, LocalDate tom, Endringstype endringstype, LocalDate endringstidspunkt, AdresseType adresseType, Landkode land, String matrikkelId,
                                 String postboksnummer, String poststed, String postboksanlegg) {
        super(fom, tom, endringstype, endringstidspunkt, adresseType, land, matrikkelId);
        this.postboksnummer = postboksnummer;
        this.poststed = poststed;
        this.postboksanlegg = postboksanlegg;
    }

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
