package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Arbeidsforhold(List<Arbeidsavtale> arbeidsavtaler,
                             List<Permisjon> permisjoner,
                             String arbeidsforholdId,
                             @JsonProperty("arbeidsforholdIdNav") Long arbeidsforholdIdnav,
                             LocalDate ansettelsesperiodeTom,
                             LocalDate ansettelsesperiodeFom,
                             Arbeidsforholdstype arbeidsforholdstype,
                             List<AntallTimerIPerioden> timeposteringer,
                             String arbeidsgiverOrgnr,
                             String opplyserOrgnr,
                             String arbeidsgiverAktorId) {

    public Arbeidsforhold(List<Arbeidsavtale> arbeidsavtaler, List<Permisjon> permisjoner, String arbeidsforholdId,
                          @JsonProperty("arbeidsforholdIdNav") Long arbeidsforholdIdnav, LocalDate ansettelsesperiodeTom,
                          LocalDate ansettelsesperiodeFom, Arbeidsforholdstype arbeidsforholdstype,
                          List<AntallTimerIPerioden> timeposteringer, String arbeidsgiverOrgnr, String opplyserOrgnr,
                          String arbeidsgiverAktorId) {
        this.arbeidsavtaler = arbeidsavtaler;
        this.permisjoner = Optional.ofNullable(permisjoner).orElse(List.of());
        this.arbeidsforholdId = arbeidsforholdId;
        this.arbeidsforholdIdnav = Optional.ofNullable(arbeidsforholdIdnav).orElse(ArbeidsforholdIdNav.next());
        this.ansettelsesperiodeTom = ansettelsesperiodeTom;
        this.ansettelsesperiodeFom = ansettelsesperiodeFom;
        this.arbeidsforholdstype = arbeidsforholdstype;
        this.timeposteringer = timeposteringer;
        this.arbeidsgiverOrgnr = arbeidsgiverOrgnr;
        this.opplyserOrgnr = opplyserOrgnr;
        this.arbeidsgiverAktorId = arbeidsgiverAktorId;
    }
}
