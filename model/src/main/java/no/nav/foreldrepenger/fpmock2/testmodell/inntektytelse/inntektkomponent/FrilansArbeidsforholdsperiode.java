package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FrilansArbeidsforholdsperiode {


    @JsonProperty("frilansFom")
    private LocalDateTime frilansFom;

    @JsonProperty("frilansTom")
    private LocalDateTime frilansTom;

    @JsonProperty("orgnr")
    private String orgnr;

    @JsonProperty("stillingsprosent")
    private Integer stillingsprosent;

    public LocalDateTime getFrilansFom() {
        return frilansFom;
    }

    public void setFrilansFom(LocalDateTime frilansFom) {
        this.frilansFom = frilansFom;
    }

    public LocalDateTime getFrilansTom() {
        return frilansTom;
    }

    public void setFrilansTom(LocalDateTime frilansTom) {
        this.frilansTom = frilansTom;
    }

    public String getOrgnr() {
        return orgnr;
    }

    public void setOrgnr(String orgnr) {
        this.orgnr = orgnr;
    }

    public Integer getStillingsprosent() {
        return stillingsprosent;
    }

    public void setStillingsprosent(Integer stillingsprosent) {
        this.stillingsprosent = stillingsprosent;
    }
}





/*

    protected Integer antallTimerPerUkeSomEnFullStillingTilsvarer;
    protected Arbeidstidsordning arbeidstidsordning;
    protected Avloenningstyper avloenningstype;
    @XmlSchemaType(
        name = "date"
    )
    protected XMLGregorianCalendar sisteDatoForStillingsprosentendring;
    @XmlSchemaType(
        name = "date"
    )
    protected XMLGregorianCalendar sisteLoennsendring;
    protected AapenPeriode frilansPeriode;
    protected BigDecimal stillingsprosent;
    protected Yrker yrke;
    protected String arbeidsforholdID;
    protected String arbeidsforholdIDnav;
    @XmlElement(
        required = true
    )
    protected Arbeidsforholdstyper arbeidsforholdstype;
    protected Aktoer arbeidsgiver;
    @XmlElement(
        required = true
    )
    protected Aktoer arbeidstaker;
 */