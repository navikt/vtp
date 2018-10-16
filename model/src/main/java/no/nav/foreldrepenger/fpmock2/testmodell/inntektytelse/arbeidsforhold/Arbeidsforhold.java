package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arbeidsforhold;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Arbeidsforhold {

    @JsonProperty("arbeidsavtaler")
    private List<Arbeidsavtale> arbeidsavtaler;

    @JsonProperty("arbeidsforholdId")
    private String arbeidsforholdId;

    @JsonProperty("arbeidsforholdIdNav")
    private Long arbeidsforholdIdnav;

    @JsonProperty("ansettelsesperiodeTom")
    private LocalDate ansettelsesperiodeTom;

    @JsonProperty("ansettelsesperiodeFom")
    private LocalDate ansettelsesperiodeFom;

    @JsonProperty("arbeidsforholdstype")
    private Arbeidsforholdstype arbeidsforholdstype;

    @JsonProperty("timeposteringer")
    private List<AntallTimerIPerioden> timeposteringer;

    @JsonProperty("arbeidsgiverOrgnr")
    private String arbeidsgiverOrgnr;

    @JsonProperty("opplyserOrgnr")
    private String opplyserOrgnr;


    public List<Arbeidsavtale> getArbeidsavtaler() {
        return arbeidsavtaler;
    }

    public void setArbeidsavtaler(List<Arbeidsavtale> arbeidsavtaler) {
        this.arbeidsavtaler = arbeidsavtaler;
    }

    public String getArbeidsforholdId() {
        return arbeidsforholdId;
    }

    public void setArbeidsforholdId(String arbeidsforholdId) {
        this.arbeidsforholdId = arbeidsforholdId;
    }

    public Long getArbeidsforholdIdnav() {
        return arbeidsforholdIdnav;
    }

    public void setArbeidsforholdIdnav(Long arbeidsforholdIdnav) {
        this.arbeidsforholdIdnav = arbeidsforholdIdnav;
    }

    public LocalDate getAnsettelsesperiodeTom() {
        return ansettelsesperiodeTom;
    }

    public void setAnsettelsesperiodeTom(LocalDate ansettelsesperiodeTom) {
        this.ansettelsesperiodeTom = ansettelsesperiodeTom;
    }

    public LocalDate getAnsettelsesperiodeFom() {
        return ansettelsesperiodeFom;
    }

    public void setAnsettelsesperiodeFom(LocalDate ansettelsesperiodeFom) {
        this.ansettelsesperiodeFom = ansettelsesperiodeFom;
    }

    public Arbeidsforholdstype getArbeidsforholdstype() {
        return arbeidsforholdstype;
    }

    public void setArbeidsforholdstype(Arbeidsforholdstype arbeidsforholdstype) {
        this.arbeidsforholdstype = arbeidsforholdstype;
    }

    public List<AntallTimerIPerioden> getTimeposteringer() {
        return timeposteringer;
    }

    public void setTimeposteringer(List<AntallTimerIPerioden> timeposteringer) {
        this.timeposteringer = timeposteringer;
    }

    public String getArbeidsgiverOrgnr() {
        return arbeidsgiverOrgnr;
    }

    public void setArbeidsgiverOrgnr(String arbeidsgiverOrgnr) {
        this.arbeidsgiverOrgnr = arbeidsgiverOrgnr;
    }

    public String getOpplyserOrgnr() {
        return opplyserOrgnr;
    }

    public void setOpplyserOrgnr(String opplyserOrgnr) {
        this.opplyserOrgnr = opplyserOrgnr;
    }
}
