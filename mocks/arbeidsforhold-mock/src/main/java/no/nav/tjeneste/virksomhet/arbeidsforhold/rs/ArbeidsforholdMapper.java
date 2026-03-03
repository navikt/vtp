package no.nav.tjeneste.virksomhet.arbeidsforhold.rs;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.ArbeidsforholdIdNav;
import no.nav.vtp.arbeidsforhold.Arbeidsavtale;
import no.nav.vtp.arbeidsforhold.Arbeidsforhold;
import no.nav.vtp.arbeidsforhold.Permisjon;
import no.nav.vtp.ident.Orgnummer;
import no.nav.vtp.ident.PersonIdent;

import java.util.List;

public class ArbeidsforholdMapper {
    private ArbeidsforholdMapper() {}

    public static ArbeidsforholdRS tilArbeidsforholdRS(Arbeidsforhold arbeidsforhold) {
        return new ArbeidsforholdRS(
                arbeidsforhold.arbeidsforholdId(),
                ArbeidsforholdIdNav.next(),
                opplysningspliktFra(arbeidsforhold),
                new AnsettelsesperiodeRS(new PeriodeRS(arbeidsforhold.ansettelsesperiodeFom(), arbeidsforhold.ansettelsesperiodeTom())),
                tilArbeidsavtaler(arbeidsforhold), tilPermisjoner(arbeidsforhold),
                arbeidsforhold.ansettelsesperiodeFom(),
                arbeidsforhold.arbeidsforholdstype() != null ? arbeidsforhold.arbeidsforholdstype().getKode() : null
        );
    }

    private static List<PermisjonPermitteringRS> tilPermisjoner(Arbeidsforhold arbeidsforhold) {
        return arbeidsforhold.permisjoner()
                .stream()
                .map(ArbeidsforholdMapper::tilPermisjon)
                .toList();
    }

    private static PermisjonPermitteringRS tilPermisjon(Permisjon p) {
        return new PermisjonPermitteringRS(new PeriodeRS(p.fom(), p.tom()),
                p.stillingsprosent() != null ? p.stillingsprosent().doubleValue() : null,
                p.permisjonstype() != null ? p.permisjonstype().name() : null);
    }

    private static List<ArbeidsavtaleRS> tilArbeidsavtaler(Arbeidsforhold arbeidsforhold) {
        return arbeidsforhold.arbeidsavtaler()
                .stream()
                .map(ArbeidsforholdMapper::tilArbeidsAvtale)
                .toList();
    }

    private static ArbeidsavtaleRS tilArbeidsAvtale(Arbeidsavtale a) {
        return new ArbeidsavtaleRS(a.stillingsprosent() != null ? a.stillingsprosent().doubleValue() : null,
                a.avtaltArbeidstimerPerUke() != null ? a.avtaltArbeidstimerPerUke().doubleValue() : null,
                a.beregnetAntallTimerPerUke() != null ? a.beregnetAntallTimerPerUke().doubleValue() : null, a.sisteLønnsendringsdato(),
                new PeriodeRS(a.fomGyldighetsperiode(), a.tomGyldighetsperiode()), "8269102");
    }

    private static OpplysningspliktigArbeidsgiverRS opplysningspliktFra(Arbeidsforhold arbeidsforhold) {
        if (arbeidsforhold.identifikator() instanceof Orgnummer(String ident)) {
            return new OpplysningspliktigArbeidsgiverRS(OpplysningspliktigArbeidsgiverRS.Type.Organisasjon, ident, null, null);
        }
        var personIdent = (PersonIdent) arbeidsforhold.identifikator();
        return new OpplysningspliktigArbeidsgiverRS(OpplysningspliktigArbeidsgiverRS.Type.Person, null, personIdent.aktørId(),
                personIdent.ident());
    }
}
