package no.nav.vtp.arbeidsforhold;

import java.time.LocalDate;
import java.util.List;

public record Arbeidsforhold(String arbeidsforholdId, // Arbeidsgiver identifiator (privat og orgnummer)
                             LocalDate ansettelsesperiodeFom,
                             LocalDate ansettelsesperiodeTom,
                             Arbeidsforholdstype arbeidsforholdstype,
                             List<Arbeidsavtale> arbeidsavtaler,
                             List<Permisjon> permisjoner) {
}
