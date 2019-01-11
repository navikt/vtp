package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

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

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("skatteOgAvgiftsregel")
    private String skatteOgAvgiftsregel;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("inngaarIGrunnlagForTrekk")
    private Boolean inngaarIGrunnlagForTrekk;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("utloeserArbeidsgiveravgift")
    private Boolean utloeserArbeidsgiveravgift;

    public Inntektsperiode(){ }

    public Inntektsperiode(LocalDate fom, LocalDate tom, Integer beløp, String orgnr, InntektType inntektType, InntektFordel inntektFordel,
                           String beskrivelse, String skatteOgAvgiftsregel, Boolean inngaarIGrunnlagForTrekk, Boolean utloeserArbeidsgiveravgift){
        this.fom = fom;
        this.tom = tom;
        this.beløp = beløp;
        this.orgnr = orgnr;
        this.type = inntektType;
        this.fordel = inntektFordel;
        this.beskrivelse = beskrivelse;
        this.skatteOgAvgiftsregel = skatteOgAvgiftsregel;
        this.inngaarIGrunnlagForTrekk = inngaarIGrunnlagForTrekk;
        this.utloeserArbeidsgiveravgift = utloeserArbeidsgiveravgift;
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

    public String getSkatteOgAvgiftsregel() { return skatteOgAvgiftsregel;}

    public void setSkatteOgAvgiftsregel(String skatteOgAvgiftsregel) { this.skatteOgAvgiftsregel = skatteOgAvgiftsregel;}

    public Boolean getInngaarIGrunnlagForTrekk() { return inngaarIGrunnlagForTrekk;}

    public void setInngaarIGrunnlagForTrekk(Boolean inngaarIGrunnlagForTrekk) { this.inngaarIGrunnlagForTrekk = inngaarIGrunnlagForTrekk;}

    public Boolean getUtloeserArbeidsgiveravgift() { return utloeserArbeidsgiveravgift;}

    public void setUtloeserArbeidsgiveravgift(Boolean utloeserArbeidsgiveravgift) { this.utloeserArbeidsgiveravgift = utloeserArbeidsgiveravgift; }
}
