package no.nav.vtp;

import java.time.LocalDate;
import java.util.List;

import com.neovisionaries.i18n.CountryCode;

import no.nav.vtp.arbeidsforhold.Arbeidsavtale;
import no.nav.vtp.arbeidsforhold.Arbeidsforhold;
import no.nav.vtp.arbeidsforhold.Arbeidsforholdstype;
import no.nav.vtp.arbeidsforhold.Organisasjon;
import no.nav.vtp.ident.Orgnummer;
import no.nav.vtp.ident.PersonIdent;
import no.nav.vtp.inntekt.Inntektsperiode;
import no.nav.vtp.skatt.Skatteopplysning;
import no.nav.vtp.personopplysninger.Adresse;
import no.nav.vtp.personopplysninger.Adressebeskyttelse;
import no.nav.vtp.personopplysninger.Adresser;
import no.nav.vtp.personopplysninger.Familierelasjon;
import no.nav.vtp.personopplysninger.GeografiskTilknytning;
import no.nav.vtp.personopplysninger.Kjønn;
import no.nav.vtp.personopplysninger.Navn;
import no.nav.vtp.personopplysninger.Personopplysninger;
import no.nav.vtp.personopplysninger.Personstatus;
import no.nav.vtp.personopplysninger.Rolle;
import no.nav.vtp.personopplysninger.Sivilstand;
import no.nav.vtp.personopplysninger.Språk;
import no.nav.vtp.personopplysninger.Statsborgerskap;
import no.nav.vtp.ytelse.Beregningsgrunnlag;
import no.nav.vtp.ytelse.Ytelse;
import no.nav.vtp.ytelse.YtelseType;

public class PersonBuilder {

    // Faste identer brukt i test-scenariet
    public static final String SØKER_IDENT        = "01234567890"; // placeholder for ${for1}
    public static final String ANNEN_PART_IDENT   = "09902500097"; // placeholder for ${for2}
    public static final String BARN1_IDENT        = "11111111111"; // placeholder for ${barn1}
    public static final String BARN2_IDENT        = "22222222222"; // placeholder for ${barn2}
    public static final String ORGNR_ARBEIDSGIVER = "994884174";
    public static final String ORGNR_FRILANS      = "973861778";

    /** Returnerer begge personene: [0] = søker, [1] = annen part */
    public static List<Person> lagPersoner() {
        return List.of(lagSøker(), lagAnnenPart());
    }

    // ---------------------------------------------------------------------------
    // Søker – "Dolly Duck", K, 50 år
    // ---------------------------------------------------------------------------
    public static Person lagSøker() {
        return new Person(
                lagSøkerPersonopplysninger(),
                lagArbeidsforhold(),
                lagInntekt(),
                lagSøkerYtelser(),
                lagSkatteopplysninger()
        );
    }

    // ---------------------------------------------------------------------------
    // Annen part – "Donaldo Duck", M, 50 år
    // ---------------------------------------------------------------------------
    public static Person lagAnnenPart() {
        return new Person(
                lagAnnenPartPersonopplysninger(),
                lagArbeidsforhold(),   // samme aareg-data iflg. inntektytelse-annenpart.json
                lagInntekt(),          // samme inntektsdata
                List.of(),
                List.of()
        );
    }

    // ---------------------------------------------------------------------------
    // Personopplysninger søker
    // ---------------------------------------------------------------------------
    private static Personopplysninger lagSøkerPersonopplysninger() {
        var bostedsadresse = new Adresse(Adresse.AdresseType.BOSTEDSADRESSE, "0000001", CountryCode.NO,
                LocalDate.now().minusYears(1), null);
        var postadresse = new Adresse(Adresse.AdresseType.POSTADRESSE, "0000001", CountryCode.NL,
                LocalDate.now().minusYears(4), LocalDate.now().minusYears(1));
        var adresser = new Adresser(List.of(bostedsadresse, postadresse), Adressebeskyttelse.UGRADERT);

        return new Personopplysninger(
                new PersonIdent(SØKER_IDENT),
                Rolle.MOR,
                new Navn("Dolly", null,"Duck"),
                LocalDate.now().minusYears(50),
                null,
                Språk.NB,
                Kjønn.K,
                new GeografiskTilknytning(CountryCode.NO, GeografiskTilknytning.GeografiskTilknytningType.LAND),
                List.of(
                        new Familierelasjon(Familierelasjon.Relasjon.BARN, new PersonIdent(BARN1_IDENT)),
                        new Familierelasjon(Familierelasjon.Relasjon.BARN, new PersonIdent(BARN2_IDENT)),
                        new Familierelasjon(Familierelasjon.Relasjon.EKTE, new PersonIdent(ANNEN_PART_IDENT))
                ),
                List.of(new Statsborgerskap(CountryCode.NO), new Statsborgerskap(CountryCode.SE)),
                List.of(new Sivilstand(Sivilstand.Type.ENKE_ELLER_ENKEMANN, null, null)),
                List.of(
                        new Personstatus(Personstatus.Type.BOSA, LocalDate.now().minusDays(6), null),
                        new Personstatus(Personstatus.Type.UREG, LocalDate.now().minusYears(50), LocalDate.now().minusDays(7))
                ),
                List.of(),
                adresser,
                false
        );
    }

    // ---------------------------------------------------------------------------
    // Personopplysninger annen part
    // ---------------------------------------------------------------------------
    private static Personopplysninger lagAnnenPartPersonopplysninger() {
        var bostedsadresse = new Adresse(Adresse.AdresseType.BOSTEDSADRESSE, "0000001", CountryCode.NO,
                LocalDate.now().minusYears(1), null);
        var adresser = new Adresser(List.of(bostedsadresse), Adressebeskyttelse.UGRADERT);

        return new Personopplysninger(
                new PersonIdent(ANNEN_PART_IDENT),
                Rolle.FAR,
                new Navn("Donald", null,"Duck"),
                LocalDate.now().minusYears(50),
                null,
                Språk.NB,
                Kjønn.M,
                new GeografiskTilknytning(CountryCode.NO, GeografiskTilknytning.GeografiskTilknytningType.LAND),
                List.of(
                        new Familierelasjon(Familierelasjon.Relasjon.BARN, new PersonIdent(BARN1_IDENT)),
                        new Familierelasjon(Familierelasjon.Relasjon.BARN, new PersonIdent(BARN2_IDENT)),
                        new Familierelasjon(Familierelasjon.Relasjon.EKTE, new PersonIdent(SØKER_IDENT))
                ),
                List.of(new Statsborgerskap(CountryCode.NO)),
                List.of(new Sivilstand(Sivilstand.Type.GIFT, null, null)),
                List.of(new Personstatus(Personstatus.Type.BOSA, LocalDate.now().minusYears(50), null)),
                List.of(),
                adresser,
                false
        );
    }

    // ---------------------------------------------------------------------------
    // Arbeidsforhold – felles for søker og annen part (fra inntektytelse-annenpart.json)
    // ---------------------------------------------------------------------------
    private static List<Arbeidsforhold> lagArbeidsforhold() {
        // ARB001-001 – ordinært, aktiv, fom now()-P4Y
        var arb1 = new Arbeidsforhold(
                new Organisasjon(new Orgnummer(ORGNR_ARBEIDSGIVER), "ARB001-001", null),
                LocalDate.now().minusYears(4),
                null,
                Arbeidsforholdstype.ORDINÆRT_ARBEIDSFORHOLD,
                List.of(new Arbeidsavtale(40, 100, 40, null, LocalDate.now().minusYears(4), null)),
                List.of()
        );

        // ARB001-002 – ordinært, avsluttet, fom now()-P4Y tom now()-P2Y
        var arb2 = new Arbeidsforhold(
                new Organisasjon(new Orgnummer(ORGNR_ARBEIDSGIVER), "ARB001-002", null),
                LocalDate.now().minusYears(4),
                LocalDate.now().minusYears(2),
                Arbeidsforholdstype.ORDINÆRT_ARBEIDSFORHOLD,
                List.of(new Arbeidsavtale(40, 100, 40, null, LocalDate.now().minusYears(4), null)),
                List.of()
        );

        // ARB001-003 – frilanser, avsluttet, fom now()-P4Y tom now()-P2Y
        var arb3 = new Arbeidsforhold(
                new Organisasjon(new Orgnummer(ORGNR_FRILANS), "ARB001-003", null),
                LocalDate.now().minusYears(4),
                LocalDate.now().minusYears(2),
                Arbeidsforholdstype.FRILANSER_OPPDRAGSTAKER_MED_MER,
                List.of(new Arbeidsavtale(40, 100, 40, LocalDate.now(), LocalDate.now().minusYears(2), null)),
                List.of()
        );

        return List.of(arb1, arb2, arb3);
    }

    // ---------------------------------------------------------------------------
    // Inntekt – felles (fra inntektytelse-annenpart.json)
    // ---------------------------------------------------------------------------
    private static List<Inntektsperiode> lagInntekt() {
        return List.of(new Inntektsperiode(
                new Organisasjon(new Orgnummer(ORGNR_FRILANS), "ARB001-003", null),
                LocalDate.now().minusYears(2),
                LocalDate.now(),
                45_000,
                Inntektsperiode.YtelseType.FASTLØNN,
                Inntektsperiode.FordelType.KONTANTYTELSE
        ));
    }

    // ---------------------------------------------------------------------------
    // Ytelser søker – AAP fra arena-saken (fra inntektytelse-søker.json)
    // ---------------------------------------------------------------------------
    private static List<Ytelse> lagSøkerYtelser() {
        return List.of(new Ytelse(
                YtelseType.ARBEIDSAVKLARINGSPENGER,
                LocalDate.now().minusMonths(12),
                LocalDate.now().plusMonths(2),
                1000,
                10_000,
                List.of(new Beregningsgrunnlag(Beregningsgrunnlag.Kategori.ARBEIDSTAKER, null))
        ));
    }

    // ---------------------------------------------------------------------------
    // Skatteopplysninger søker (fra sigrun)
    // ---------------------------------------------------------------------------
    private static List<Skatteopplysning> lagSkatteopplysninger() {
        return List.of(
                new Skatteopplysning(LocalDate.now().getYear() - 1, 550_000),
                new Skatteopplysning(LocalDate.now().getYear() - 2, 500_000),
                new Skatteopplysning(LocalDate.now().getYear() - 3, 480_000)
        );
    }
}
