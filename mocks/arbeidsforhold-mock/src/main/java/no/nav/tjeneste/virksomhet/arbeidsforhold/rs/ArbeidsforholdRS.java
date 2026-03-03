package no.nav.tjeneste.virksomhet.arbeidsforhold.rs;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Arbeidsforhold;

public record ArbeidsforholdRS(String arbeidsforholdId,
                               Long navArbeidsforholdId,
                               OpplysningspliktigArbeidsgiverRS arbeidsgiver,
                               AnsettelsesperiodeRS ansettelsesperiode,
                               List<ArbeidsavtaleRS> arbeidsavtaler,
                               List<PermisjonPermitteringRS> permisjonPermitteringer,
                               LocalDate registrert,
                               String type) {

    public static ArbeidsforholdRS fra(Arbeidsforhold arbeidsforhold) {
        return new ArbeidsforholdRS(
                arbeidsforhold.arbeidsforholdId(),
                arbeidsforhold.arbeidsforholdIdnav(),
                OpplysningspliktigArbeidsgiverRS.fraOrgnrEllerAktørId(arbeidsforhold.arbeidsgiverOrgnr(), arbeidsforhold.aktorIDArbeidsgiver()),
                new AnsettelsesperiodeRS(new PeriodeRS(arbeidsforhold.ansettelsesperiodeFom(), arbeidsforhold.ansettelsesperiodeTom())),
                arbeidsforhold.arbeidsavtaler().stream().map(ArbeidsavtaleRS::fra).toList(),
                arbeidsforhold.permisjoner().stream().map(PermisjonPermitteringRS::fra).toList(),
                arbeidsforhold.ansettelsesperiodeFom(),
                arbeidsforhold.arbeidsforholdstype().getKode()
        );
    }
}
