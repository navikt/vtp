package no.nav.tjeneste.virksomhet.arbeidsforhold.rs;

import java.util.List;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.ArbeidsforholdIdNav;
import no.nav.vtp.person.arbeidsforhold.Arbeidsavtale;
import no.nav.vtp.person.arbeidsforhold.Arbeidsforhold;
import no.nav.vtp.person.arbeidsforhold.Arbeidsforholdstype;
import no.nav.vtp.person.arbeidsforhold.Organisasjon;
import no.nav.vtp.person.arbeidsforhold.Permisjon;
import no.nav.vtp.person.arbeidsforhold.PrivatArbeidsgiver;

public class ArbeidsforholdMapper {
    private ArbeidsforholdMapper() {}

    public static ArbeidsforholdRS tilArbeidsforholdRS(Arbeidsforhold arbeidsforhold) {
        return new ArbeidsforholdRS(
                arbeidsforhold.arbeidsgiver() instanceof Organisasjon organisasjon ? organisasjon.arbeidsforholdId() : null,
                ArbeidsforholdIdNav.next(),
                opplysningspliktFra(arbeidsforhold),
                new AnsettelsesperiodeRS(new PeriodeRS(arbeidsforhold.ansettelsesperiodeFom(), arbeidsforhold.ansettelsesperiodeTom())),
                tilArbeidsavtaler(arbeidsforhold), tilPermisjoner(arbeidsforhold),
                arbeidsforhold.ansettelsesperiodeFom(),
                tilArbeidsforholdtypeAareg(arbeidsforhold.arbeidsforholdstype())
        );
    }

    public static String tilArbeidsforholdtypeAareg(Arbeidsforholdstype arbeidsforholdstype) {
        return switch (arbeidsforholdstype) {
            case ORDINÆRT_ARBEIDSFORHOLD -> "ordinaertArbeidsforhold";
            case MARITIMT_ARBEIDSFORHOLD -> "maritimtArbeidsforhold";
            case FRILANSER_OPPDRAGSTAKER_MED_MER -> "frilanserOppdragstakerHonorarPersonerMm";
            case FORENKLET_OPPGJØRSORDNING -> "forenkletOppgjoersordning";
            case null -> null;
        };
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
                p.permisjonstype() != null ? tilPersonstypeAareg(p) : null);
    }

    private static String tilPersonstypeAareg(Permisjon p) {
        return switch (p.permisjonstype()) {
            case PERMISJON -> "permisjon";
            case PERMISJON_MED_FORELDREPENGER -> "permisjonMedForeldrepenger";
            case PERMISJON_VED_MILITÆRTJENESTE -> "permisjonVedMilitaertjeneste";
            case PERMITTERING -> "permittering";
            case UTDANNINGSPERMISJON -> "utdanningspermisjon";
            case UTDANNINGSPERMISJON_IKKE_LOVFESTET -> "utdanningspermisjonIkkeLovfestet";
            case UTDANNINGSPERMISJON_LOVFESTET -> "utdanningspermisjonLovfestet";
            case VELFERDSPERMISJON -> "velferdspermisjon";
            case ANNEN_PERMISJON_IKKE_LOVFESTET -> "andreIkkeLovfestedePermisjoner";
            case ANNEN_PERMISJON_LOVFESTET -> "andreLovfestedePermisjoner";
        };
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
                new PeriodeRS(a.fom(), a.tom()), "8269102");
    }

    private static OpplysningspliktigArbeidsgiverRS opplysningspliktFra(Arbeidsforhold arbeidsforhold) {
        if (arbeidsforhold.arbeidsgiver() instanceof Organisasjon organisasjon) {
            return new OpplysningspliktigArbeidsgiverRS(OpplysningspliktigArbeidsgiverRS.Type.Organisasjon, organisasjon.orgnummer().value(), null, null);
        }
        var privatArbeidsgiver = (PrivatArbeidsgiver) arbeidsforhold.arbeidsgiver();
        return new OpplysningspliktigArbeidsgiverRS(OpplysningspliktigArbeidsgiverRS.Type.Person, null, privatArbeidsgiver.ident().aktørId(),
                privatArbeidsgiver.ident().fnr());
    }
}
