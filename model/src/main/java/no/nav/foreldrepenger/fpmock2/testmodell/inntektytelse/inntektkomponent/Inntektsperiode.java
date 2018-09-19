package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
/*
            <inntektListe xsi:type="ns4:Loennsinntekt" xmlns:ns4="http://nav.no/tjeneste/virksomhet/inntekt/v3/informasjon/inntekt" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                    <beloep>0</beloep>
                    <fordel>naturalytelse</fordel>
                    <inntektskilde>A-ordningen</inntektskilde>
                    <inntektsperiodetype>Maaned</inntektsperiodetype>
                    <inntektsstatus>LoependeInnrapportert</inntektsstatus>
                    <levereringstidspunkt>2018-09-03T14:23:38.324+02:00</levereringstidspunkt>
                    <opptjeningsperiode>
                    <startDato>2017-01-01+01:00</startDato>
                    </opptjeningsperiode>
                    <utbetaltIPeriode>2017-01</utbetaltIPeriode>
                    <opplysningspliktig xsi:type="ns4:OrganisasjonModell">
                    <orgnummer>864950582</orgnummer>
                    </opplysningspliktig>
                    <virksomhet xsi:type="ns4:OrganisasjonModell">
                    <orgnummer>972223794</orgnummer>
                    </virksomhet>
                    <inntektsmottaker xsi:type="ns4:PersonIdent">
                    <personIdent>26049726650</personIdent>
                    </inntektsmottaker>
                    <inngaarIGrunnlagForTrekk>true</inngaarIGrunnlagForTrekk>
                    <utloeserArbeidsgiveravgift>true</utloeserArbeidsgiveravgift>
                    <informasjonsstatus>InngaarAlltid</informasjonsstatus>
                    <beskrivelse>skattepliktigDelForsikringer</beskrivelse>
                    </inntektListe>
*/


public class Inntektsperiode {


    @JsonProperty("fom")
    private LocalDateTime fom;

    @JsonProperty("tom")
    private LocalDateTime tom;

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

    public Inntektsperiode(LocalDateTime fom, LocalDateTime tom, Integer beløp, String orgnr, InntektType inntektType, InntektFordel inntektFordel, String beskrivelse){
        this.fom = fom;
        this.tom = tom;
        this.beløp = beløp;
        this.orgnr = orgnr;
        this.type = inntektType;
        this.fordel = inntektFordel;
        this.beskrivelse = beskrivelse;
    }

    public LocalDateTime getFom() {
        return fom;
    }

    public void setFom(LocalDateTime fom) {
        this.fom = fom;
    }

    public LocalDateTime getTom() {
        return tom;
    }

    public void setTom(LocalDateTime tom) {
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
