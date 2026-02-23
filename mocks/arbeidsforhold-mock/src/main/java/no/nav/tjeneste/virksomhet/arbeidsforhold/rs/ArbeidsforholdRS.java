package no.nav.tjeneste.virksomhet.arbeidsforhold.rs;

import java.time.LocalDate;
import java.util.List;

public record ArbeidsforholdRS(String arbeidsforholdId,
                               Long navArbeidsforholdId,
                               OpplysningspliktigArbeidsgiverRS arbeidsgiver,
                               AnsettelsesperiodeRS ansettelsesperiode,
                               List<ArbeidsavtaleRS> arbeidsavtaler,
                               List<PermisjonPermitteringRS> permisjonPermitteringer,
                               LocalDate registrert,
                               String type) {
}
