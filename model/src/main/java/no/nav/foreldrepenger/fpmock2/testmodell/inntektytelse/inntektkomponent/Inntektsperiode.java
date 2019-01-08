package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Inntektsperiode {


    @JsonProperty("fom")
    private LocalDate fom;

    @JsonProperty("tom")
    private LocalDate tom;

    @JsonProperty("beløp")
    private Integer beløp;

    @JsonProperty("orgnr")
    private String orgnr;

    @JsonProperty("type")
    private InntektType type;

    @JsonProperty("fordel")
    private InntektFordel fordel;

    @JsonProperty("beskrivelse")
    private String beskrivelse;

    public Inntektsperiode(){ }

    public Inntektsperiode(LocalDate fom, LocalDate tom, Integer beløp, String orgnr, InntektType inntektType, InntektFordel inntektFordel, String beskrivelse){
        this.fom = fom;
        this.tom = tom;
        this.beløp = beløp;
        this.orgnr = orgnr;
        this.type = inntektType;
        this.fordel = inntektFordel;
        this.beskrivelse = beskrivelse;
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

    public Integer getBeløp() {
        return beløp;
    }

    public void setBeløp(Integer beløp) {
        this.beløp = beløp;
    }

    public String getOrgnr() {
        return orgnr;
    }

    public void setOrgnr(String orgnr) {
        this.orgnr = orgnr;
    }

    public InntektType getType() {
        return type;
    }

    public void setType(InntektType type) {
        this.type = type;
    }

    public InntektFordel getFordel() {
        return fordel;
    }

    public void setFordel(InntektFordel fordel) {
        this.fordel = fordel;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }
}
