package no.nav.omsorgspenger.rammemeldinger;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class RammemeldingRequest {
    @JsonProperty("identitetsnummer")
    private String identitetsnummer;

    @JsonProperty("fom")
    private LocalDate fom;

    @JsonProperty("tom")
    private LocalDate tom;

    public String getIdentitetsnummer() {
        return identitetsnummer;
    }

    public void setIdentitetsnummer(String identitetsnummer) {
        this.identitetsnummer = identitetsnummer;
    }

    public LocalDate getFom() {
        return fom;
    }

    public void setFom(LocalDate fom) {
        this.fom = fom;
    }

    public LocalDate getTom() {
        return tom;
    }

    public void setTom(LocalDate tom) {
        this.tom = tom;
    }
}
