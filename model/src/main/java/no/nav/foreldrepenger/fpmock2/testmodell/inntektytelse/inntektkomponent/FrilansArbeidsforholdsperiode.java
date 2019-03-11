package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FrilansArbeidsforholdsperiode {

    private static final String FRILANS_ARBEIDSFORHOLDSTYPE = "frilanserOppdragstakerHonorarPersonerMm";

    @JsonProperty("frilansFom")
    private LocalDate frilansFom;

    @JsonProperty("frilansTom")
    private LocalDate frilansTom;

    @JsonProperty("orgnr")
    private String orgnr;

    @JsonProperty("aktorId")
    private String aktorId;

    @JsonProperty("stillingsprosent")
    private Integer stillingsprosent;

    public FrilansArbeidsforholdsperiode(){}

    public FrilansArbeidsforholdsperiode(LocalDate frilansFom, LocalDate frilansTom, String orgnr, Integer stillingsprosent, String aktorId){
        this.frilansFom = frilansFom;
        this.frilansTom = frilansTom;
        this.orgnr = orgnr;
        this.stillingsprosent = stillingsprosent;
        this.aktorId = aktorId;
    }

    public LocalDate getFrilansFom() {
        return frilansFom;
    }

    public void setFrilansFom(LocalDate frilansFom) {
        this.frilansFom = frilansFom;
    }

    public LocalDate getFrilansTom() {
        return frilansTom;
    }

    public void setFrilansTom(LocalDate frilansTom) {
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

    public String getArbeidsforholdstype() {
        return FRILANS_ARBEIDSFORHOLDSTYPE;
    }

    public String getAktorId() {
        return aktorId;
    }

    public void setAktorId(String aktorId) {
        this.aktorId = aktorId;
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