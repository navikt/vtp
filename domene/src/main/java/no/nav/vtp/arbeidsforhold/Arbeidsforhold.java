package no.nav.vtp.arbeidsforhold;

import java.time.LocalDate;
import java.util.List;

public record Arbeidsforhold(Arbeidsgiver arbeidsgiver,
                             LocalDate ansettelsesperiodeFom,
                             LocalDate ansettelsesperiodeTom,
                             Arbeidsforholdstype arbeidsforholdstype,
                             List<Arbeidsavtale> arbeidsavtaler,
                             List<Permisjon> permisjoner) {
}
