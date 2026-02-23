package no.nav.foreldrepenger.vtp.server.api.scenario;

import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.vtp.person.personopplysninger.Kjønn;

public class PersonNavn {
    @JsonProperty("fornavn")
    private String fornavn;

    @JsonProperty("etternavn")
    private String etternavn;

    @JsonProperty("kjønn")
    private Kjønn kjønn;


    public PersonNavn() {

    }

    public PersonNavn(String fornavn, String etternavn, Kjønn kjønn) {
        this.fornavn = fornavn;
        this.etternavn = etternavn;
        this.kjønn = kjønn;
    }


    public String getFornavn() {
        return fornavn;
    }

    public void setFornavn(String fornavn) {
        this.fornavn = fornavn;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public void setEtternavn(String etternavn) {
        this.etternavn = etternavn;
    }

    public Kjønn getKjønn() {
        return kjønn;
    }

    public void setKjønn(Kjønn kjønn) {
        this.kjønn = kjønn;
    }

    public String getFulltnavn() {
        return getFornavn() + " " + getEtternavn();
    }
}
