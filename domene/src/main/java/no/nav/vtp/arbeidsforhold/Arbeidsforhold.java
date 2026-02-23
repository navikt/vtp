package no.nav.vtp.arbeidsforhold;

import java.time.LocalDate;
import java.util.List;

import no.nav.vtp.ident.Identifikator;

public record Arbeidsforhold(Identifikator identifikator,
                             String arbeidsforholdId,
                             LocalDate ansettelsesperiodeFom,
                             LocalDate ansettelsesperiodeTom,
                             Arbeidsforholdstype arbeidsforholdstype,
                             List<Arbeidsavtale> arbeidsavtaler,
                             List<Permisjon> permisjoner) {
}
