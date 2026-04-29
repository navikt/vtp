package no.nav.vtp;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.neovisionaries.i18n.CountryCode;

import no.nav.foreldrepenger.vtp.kontrakter.FødselsnummerGenerator;
import no.nav.vtp.person.Person;
import no.nav.vtp.person.arbeidsforhold.Arbeidsavtale;
import no.nav.vtp.person.arbeidsforhold.Arbeidsforhold;
import no.nav.vtp.person.arbeidsforhold.Arbeidsforholdstype;
import no.nav.vtp.person.arbeidsforhold.Organisasjon;
import no.nav.vtp.person.ident.Orgnummer;
import no.nav.vtp.person.ident.PersonIdent;
import no.nav.vtp.person.inntekt.Inntektsperiode;
import no.nav.vtp.person.skatt.Skatteopplysning;
import no.nav.vtp.person.personopplysninger.Adresse;
import no.nav.vtp.person.personopplysninger.Adressebeskyttelse;
import no.nav.vtp.person.personopplysninger.Adresser;
import no.nav.vtp.person.personopplysninger.Familierelasjon;
import no.nav.vtp.person.personopplysninger.GeografiskTilknytning;
import no.nav.vtp.person.personopplysninger.Kjønn;
import no.nav.vtp.person.personopplysninger.Navn;
import no.nav.vtp.person.personopplysninger.Personopplysninger;
import no.nav.vtp.person.personopplysninger.Personstatus;
import no.nav.vtp.person.personopplysninger.Rolle;
import no.nav.vtp.person.personopplysninger.Sivilstand;
import no.nav.vtp.person.personopplysninger.Språk;
import no.nav.vtp.person.personopplysninger.Statsborgerskap;
import no.nav.vtp.person.ytelse.Beregningsgrunnlag;
import no.nav.vtp.person.ytelse.Ytelse;
import no.nav.vtp.person.ytelse.YtelseType;

public class PersonBuilder {

    public static final String ORGNR_ARBEIDSGIVER = "994884174";
    public static final String ORGNR_FRILANS = "973861778";

    /**
     * Returnerer et komplett familie-scenarie med søker, annen part og to barn.
     * Alle FNR genereres tilfeldig slik at gjentatte kall aldri kolliderer i PersonRepository.
     */
    public static TestScenario lagPersoner() {
        var søkerIdent = new FødselsnummerGenerator.Builder()
                .kjonn(no.nav.foreldrepenger.vtp.kontrakter.person.Kjønn.K)
                .fodselsdato(LocalDate.now().minusYears(50))
                .buildAndGenerate();
        var annenPartIdent = new FødselsnummerGenerator.Builder()
                .kjonn(no.nav.foreldrepenger.vtp.kontrakter.person.Kjønn.M)
                .fodselsdato(LocalDate.now().minusYears(50))
                .buildAndGenerate();
        var barn1Ident = new FødselsnummerGenerator.Builder()
                .kjonn(no.nav.foreldrepenger.vtp.kontrakter.person.Kjønn.K)
                .fodselsdato(LocalDate.now().minusYears(3))
                .buildAndGenerate();
        var barn2Ident = new FødselsnummerGenerator.Builder()
                .kjonn(no.nav.foreldrepenger.vtp.kontrakter.person.Kjønn.K)
                .fodselsdato(LocalDate.now().minusYears(1))
                .buildAndGenerate();

        var søker = new Person(
                lagSøkerPersonopplysninger(søkerIdent, annenPartIdent, barn1Ident, barn2Ident),
                lagArbeidsforhold(), lagInntekt(), lagSøkerYtelser(), lagSkatteopplysninger());
        var annenPart = new Person(
                lagAnnenPartPersonopplysninger(annenPartIdent, søkerIdent, barn1Ident, barn2Ident),
                lagArbeidsforhold(), lagInntekt(), List.of(), List.of());
        var barn1 = new Person(lagBarnPersonopplysninger(barn1Ident, LocalDate.now().minusYears(3), søkerIdent, annenPartIdent),
                List.of(), List.of(), List.of(), List.of());
        var barn2 = new Person(lagBarnPersonopplysninger(barn2Ident, LocalDate.now().minusYears(1), søkerIdent, annenPartIdent),
                List.of(), List.of(), List.of(), List.of());
        return new TestScenario(søker, annenPart, barn1, barn2);
    }

    public record TestScenario(Person søker, Person annenPart, Person barn1, Person barn2) {
        public List<Person> allePersoner() {
            return List.of(søker, annenPart, barn1, barn2);
        }
        public String søkerIdent() {
            return søker.personopplysninger().identifikator().value();
        }
        public String annenPartIdent() {
            return annenPart.personopplysninger().identifikator().value();
        }
        public String barn1Ident() {
            return barn1.personopplysninger().identifikator().value();
        }
        public String barn2Ident() {
            return barn2.personopplysninger().identifikator().value();
        }
    }

    // ---------------------------------------------------------------------------
    // Søker – "Dolly Duck", K, ~50 år — standalone, uten familierelasjoner
    // ---------------------------------------------------------------------------
    public static Person lagSøker() {
        var ident = new FødselsnummerGenerator.Builder()
                .kjonn(no.nav.foreldrepenger.vtp.kontrakter.person.Kjønn.K)
                .fodselsdato(LocalDate.now().minusYears(50))
                .buildAndGenerate();
        var bostedsadresse = new Adresse(Adresse.AdresseType.BOSTEDSADRESSE, "0000001", CountryCode.NO,
                LocalDate.now().minusYears(1), null);
        var adresser = new Adresser(List.of(bostedsadresse), Adressebeskyttelse.UGRADERT);
        var personopplysninger = new Personopplysninger(
                new PersonIdent(ident), UUID.randomUUID(), Rolle.MOR,
                new Navn("Dolly", null, "Duck"),
                LocalDate.now().minusYears(50), null, Språk.NB, Kjønn.K,
                new GeografiskTilknytning(CountryCode.NO, GeografiskTilknytning.GeografiskTilknytningType.LAND),
                List.of(),
                List.of(new Statsborgerskap(CountryCode.NO), new Statsborgerskap(CountryCode.SE)),
                List.of(new Sivilstand(Sivilstand.Type.ENKE_ELLER_ENKEMANN, null, null)),
                List.of(new Personstatus(Personstatus.Type.BOSA, LocalDate.now().minusDays(6), null),
                        new Personstatus(Personstatus.Type.UREG, LocalDate.now().minusYears(50), LocalDate.now().minusDays(7))),
                List.of(), adresser, false);
        return new Person(personopplysninger, lagArbeidsforhold(), lagInntekt(), lagSøkerYtelser(), lagSkatteopplysninger());
    }

    // ---------------------------------------------------------------------------
    // Annen part – "Donald Duck", M, ~50 år — standalone, uten familierelasjoner
    // ---------------------------------------------------------------------------
    public static Person lagAnnenPart() {
        var ident = new FødselsnummerGenerator.Builder()
                .kjonn(no.nav.foreldrepenger.vtp.kontrakter.person.Kjønn.M)
                .fodselsdato(LocalDate.now().minusYears(50))
                .buildAndGenerate();
        var bostedsadresse = new Adresse(Adresse.AdresseType.BOSTEDSADRESSE, "0000001", CountryCode.NO,
                LocalDate.now().minusYears(1), null);
        var adresser = new Adresser(List.of(bostedsadresse), Adressebeskyttelse.UGRADERT);
        var personopplysninger = new Personopplysninger(
                new PersonIdent(ident), UUID.randomUUID(), Rolle.FAR,
                new Navn("Donald", null, "Duck"),
                LocalDate.now().minusYears(50), null, Språk.NB, Kjønn.M,
                new GeografiskTilknytning(CountryCode.NO, GeografiskTilknytning.GeografiskTilknytningType.LAND),
                List.of(),
                List.of(new Statsborgerskap(CountryCode.NO)),
                List.of(new Sivilstand(Sivilstand.Type.GIFT, null, null)),
                List.of(new Personstatus(Personstatus.Type.BOSA, LocalDate.now().minusYears(50), null)),
                List.of(), adresser, false);
        return new Person(personopplysninger, lagArbeidsforhold(), lagInntekt(), List.of(), List.of());
    }

    // ---------------------------------------------------------------------------
    // Personopplysninger søker (brukt fra lagPersoner)
    // ---------------------------------------------------------------------------
    private static Personopplysninger lagSøkerPersonopplysninger(String søkerIdent, String annenPartIdent,
                                                                   String barn1Ident, String barn2Ident) {
        var bostedsadresse = new Adresse(Adresse.AdresseType.BOSTEDSADRESSE, "0000001", CountryCode.NO,
                LocalDate.now().minusYears(1), null);
        var postadresse = new Adresse(Adresse.AdresseType.POSTADRESSE, "0000001", CountryCode.NL,
                LocalDate.now().minusYears(4), LocalDate.now().minusYears(1));
        var adresser = new Adresser(List.of(bostedsadresse, postadresse), Adressebeskyttelse.UGRADERT);

        return new Personopplysninger(new PersonIdent(søkerIdent), UUID.randomUUID(), Rolle.MOR,
                new Navn("Dolly", null, "Duck"),
                LocalDate.now().minusYears(50), null, Språk.NB, Kjønn.K,
                new GeografiskTilknytning(CountryCode.NO, GeografiskTilknytning.GeografiskTilknytningType.LAND),
                List.of(new Familierelasjon(Familierelasjon.Relasjon.BARN, new PersonIdent(barn1Ident)),
                        new Familierelasjon(Familierelasjon.Relasjon.BARN, new PersonIdent(barn2Ident)),
                        new Familierelasjon(Familierelasjon.Relasjon.EKTE, new PersonIdent(annenPartIdent))),
                List.of(new Statsborgerskap(CountryCode.NO), new Statsborgerskap(CountryCode.SE)),
                List.of(new Sivilstand(Sivilstand.Type.ENKE_ELLER_ENKEMANN, null, null)),
                List.of(new Personstatus(Personstatus.Type.BOSA, LocalDate.now().minusDays(6), null),
                        new Personstatus(Personstatus.Type.UREG, LocalDate.now().minusYears(50), LocalDate.now().minusDays(7))),
                List.of(), adresser, false);
    }

    // ---------------------------------------------------------------------------
    // Personopplysninger annen part (brukt fra lagPersoner)
    // ---------------------------------------------------------------------------
    private static Personopplysninger lagAnnenPartPersonopplysninger(String annenPartIdent, String søkerIdent,
                                                                       String barn1Ident, String barn2Ident) {
        var bostedsadresse = new Adresse(Adresse.AdresseType.BOSTEDSADRESSE, "0000001", CountryCode.NO,
                LocalDate.now().minusYears(1), null);
        var adresser = new Adresser(List.of(bostedsadresse), Adressebeskyttelse.UGRADERT);

        return new Personopplysninger(new PersonIdent(annenPartIdent), UUID.randomUUID(), Rolle.FAR,
                new Navn("Donald", null, "Duck"),
                LocalDate.now().minusYears(50), null, Språk.NB, Kjønn.M,
                new GeografiskTilknytning(CountryCode.NO, GeografiskTilknytning.GeografiskTilknytningType.LAND),
                List.of(new Familierelasjon(Familierelasjon.Relasjon.BARN, new PersonIdent(barn1Ident)),
                        new Familierelasjon(Familierelasjon.Relasjon.BARN, new PersonIdent(barn2Ident)),
                        new Familierelasjon(Familierelasjon.Relasjon.EKTE, new PersonIdent(søkerIdent))),
                List.of(new Statsborgerskap(CountryCode.NO)),
                List.of(new Sivilstand(Sivilstand.Type.GIFT, null, null)),
                List.of(new Personstatus(Personstatus.Type.BOSA, LocalDate.now().minusYears(50), null)),
                List.of(), adresser, false);
    }

    // ---------------------------------------------------------------------------
    // Personopplysninger barn (brukt fra lagPersoner)
    // ---------------------------------------------------------------------------
    private static Personopplysninger lagBarnPersonopplysninger(String ident, LocalDate fødselsdato,
                                                                  String morIdent, String farIdent) {
        var bostedsadresse = new Adresse(Adresse.AdresseType.BOSTEDSADRESSE, "0000001", CountryCode.NO,
                fødselsdato, null);
        var adresser = new Adresser(List.of(bostedsadresse), Adressebeskyttelse.UGRADERT);

        return new Personopplysninger(new PersonIdent(ident), UUID.randomUUID(), Rolle.BARN,
                new Navn("Lille", null, "Duck"),
                fødselsdato, null, Språk.NB, Kjønn.K,
                new GeografiskTilknytning(CountryCode.NO, GeografiskTilknytning.GeografiskTilknytningType.LAND),
                List.of(new Familierelasjon(Familierelasjon.Relasjon.MOR, new PersonIdent(morIdent)),
                        new Familierelasjon(Familierelasjon.Relasjon.FAR, new PersonIdent(farIdent))),
                List.of(new Statsborgerskap(CountryCode.NO)), List.of(),
                List.of(new Personstatus(Personstatus.Type.BOSA, fødselsdato, null)),
                List.of(), adresser, false);
    }

    // ---------------------------------------------------------------------------
    // Arbeidsforhold – felles for søker og annen part
    // ---------------------------------------------------------------------------
    private static List<Arbeidsforhold> lagArbeidsforhold() {
        var arb1 = new Arbeidsforhold(new Organisasjon(new Orgnummer(ORGNR_ARBEIDSGIVER), "ARB001-001", null),
                LocalDate.now().minusYears(4), null, Arbeidsforholdstype.ORDINÆRT_ARBEIDSFORHOLD,
                List.of(new Arbeidsavtale(40, 100, 40, null, LocalDate.now().minusYears(4), null)), List.of());
        var arb2 = new Arbeidsforhold(new Organisasjon(new Orgnummer(ORGNR_ARBEIDSGIVER), "ARB001-002", null),
                LocalDate.now().minusYears(4), LocalDate.now().minusYears(2), Arbeidsforholdstype.ORDINÆRT_ARBEIDSFORHOLD,
                List.of(new Arbeidsavtale(40, 100, 40, null, LocalDate.now().minusYears(4), null)), List.of());
        var arb3 = new Arbeidsforhold(new Organisasjon(new Orgnummer(ORGNR_FRILANS), "ARB001-003", null),
                LocalDate.now().minusYears(4), LocalDate.now().minusYears(2), Arbeidsforholdstype.FRILANSER_OPPDRAGSTAKER_MED_MER,
                List.of(new Arbeidsavtale(40, 100, 40, LocalDate.now(), LocalDate.now().minusYears(2), null)), List.of());
        return List.of(arb1, arb2, arb3);
    }

    // ---------------------------------------------------------------------------
    // Inntekt – felles
    // ---------------------------------------------------------------------------
    private static List<Inntektsperiode> lagInntekt() {
        return List.of(
                new Inntektsperiode(new Organisasjon(new Orgnummer(ORGNR_FRILANS), "ARB001-003", null),
                        LocalDate.now().minusYears(2), LocalDate.now(), 45_000,
                        Inntektsperiode.YtelseType.FASTLØNN, Inntektsperiode.FordelType.KONTANTYTELSE));
    }

    // ---------------------------------------------------------------------------
    // Ytelser søker
    // ---------------------------------------------------------------------------
    private static List<Ytelse> lagSøkerYtelser() {
        return List.of(new Ytelse(YtelseType.ARBEIDSAVKLARINGSPENGER,
                LocalDate.now().minusMonths(12), LocalDate.now().plusMonths(2), 1000, 10_000,
                List.of(new Beregningsgrunnlag(Beregningsgrunnlag.Kategori.ARBEIDSTAKER, null))));
    }

    // ---------------------------------------------------------------------------
    // Skatteopplysninger søker
    // ---------------------------------------------------------------------------
    private static List<Skatteopplysning> lagSkatteopplysninger() {
        return List.of(
                new Skatteopplysning(LocalDate.now().getYear() - 1, 550_000),
                new Skatteopplysning(LocalDate.now().getYear() - 2, 500_000),
                new Skatteopplysning(LocalDate.now().getYear() - 3, 480_000));
    }
}

