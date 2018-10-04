package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.arbeidsforhold;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Arbeidsforhold {

    @JsonProperty("arbeidsavtaler")
    private List<Arbeidsavtale> arbeidsavtaler;

    @JsonProperty("arbeidsforholdId")
    private String arbeidsforholdId;

    private String arbeidsforholdIdnav;

    @JsonProperty("ansettelsesperiodeTom")
    private LocalDate ansettelsesperiodeTom;

    @JsonProperty("ansettelsesperiodeFom")
    private LocalDate ansettelsesperiodeFom;

    @JsonProperty("arbeidsforholdstype")
    private Arbeidsforholdstype arbeidsforholdstype;

    private List<AntallTimerIPerioden> timeposteringer;

    @JsonProperty("arbeidsgiverOrgnr")
    private String arbeidsgiverOrgnr;

    @JsonProperty("opplyserOrgnr")
    private String opplyserOrgnr;



}
