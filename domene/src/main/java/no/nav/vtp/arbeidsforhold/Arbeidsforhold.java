package no.nav.vtp.arbeidsforhold;

import java.time.LocalDate;
import java.util.List;

public record Arbeidsforhold(String identifikator,  // Arbeidsgiver identifiator (privat og orgnummer)
                             String arbeidsforholdId,
                             LocalDate ansettelsesperiodeFom,
                             LocalDate ansettelsesperiodeTom,
                             Arbeidsforholdstype arbeidsforholdstype,
                             List<Arbeidsavtale> arbeidsavtaler,
                             List<Permisjon> permisjoner) {
}
