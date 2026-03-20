package no.nav.foreldrepenger.vtp.kontrakter.person.arbeidsforhold;

import java.time.LocalDate;
import java.util.List;

public record ArbeidsforholdDto(ArbeidsgiverDto arbeidsgiver,
                                String arbeidsforholdId,
                                LocalDate ansettelsesperiodeFom,
                                LocalDate ansettelsesperiodeTom,
                                Arbeidsforholdstype arbeidsforholdstype,
                                List<Arbeidsavtale> arbeidsavtaler,
                                List<Permisjon> permisjoner) {
}
