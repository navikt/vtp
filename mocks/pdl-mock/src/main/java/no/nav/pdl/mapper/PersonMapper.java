package no.nav.pdl.mapper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.neovisionaries.i18n.CountryCode;

import no.nav.pdl.Adressebeskyttelse;
import no.nav.pdl.AdressebeskyttelseGradering;
import no.nav.pdl.Bostedsadresse;
import no.nav.pdl.DoedfoedtBarn;
import no.nav.pdl.Doedsfall;
import no.nav.pdl.Foedselsdato;
import no.nav.pdl.Folkeregisteridentifikator;
import no.nav.pdl.Folkeregistermetadata;
import no.nav.pdl.Folkeregisterpersonstatus;
import no.nav.pdl.ForelderBarnRelasjon;
import no.nav.pdl.ForelderBarnRelasjonRolle;
import no.nav.pdl.Kjoenn;
import no.nav.pdl.KjoennType;
import no.nav.pdl.Kontaktadresse;
import no.nav.pdl.KontaktadresseType;
import no.nav.pdl.Matrikkeladresse;
import no.nav.pdl.Metadata;
import no.nav.pdl.Navn;
import no.nav.pdl.PostadresseIFrittFormat;
import no.nav.pdl.Sivilstand;
import no.nav.pdl.Sivilstandstype;
import no.nav.pdl.Statsborgerskap;
import no.nav.pdl.UtenlandskAdresse;
import no.nav.pdl.Vegadresse;
import no.nav.vtp.Person;
import no.nav.vtp.ident.PersonIdent;
import no.nav.vtp.personopplysninger.Adresse;
import no.nav.vtp.personopplysninger.Familierelasjon;
import no.nav.vtp.personopplysninger.Personstatus;
import no.nav.vtp.personopplysninger.Rolle;

public class PersonMapper {

    private static final DateTimeFormatter DATO_FORMATTERER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private PersonMapper() {
    }

    public static no.nav.pdl.Person tilPerson(Person person, List<Person> barnaTilPerson, boolean historikk) {
        // TODO: historikk?
        var pdlPerson = new no.nav.pdl.Person();

        pdlPerson.setAdressebeskyttelse(tilAdressebeskyttelse(person));
        pdlPerson.setFoedselsdato(tilFoedselsdato(person));
        pdlPerson.setDoedsfall(tilDoedsfall(person));
        pdlPerson.setFolkeregisteridentifikator(tilFolkeregisteridentifikator(person));
        pdlPerson.setFolkeregisterpersonstatus(tilFolkeregisterpersonstatus(person));
        pdlPerson.setKjoenn(tilKjoenn(person));
        pdlPerson.setSivilstand(tilSivilstand(person));
        pdlPerson.setStatsborgerskap(tilStatsborgerskap(person));
        pdlPerson.setForelderBarnRelasjon(tilForelderBarnRelasjon(person));

        pdlPerson.setNavn(tilNavn(person));
        pdlPerson.setBostedsadresse(tilBostedsadresser(person.personopplysninger().adresser().adresser()));
        pdlPerson.setKontaktadresse(tilKontaktadresse(person.personopplysninger().adresser().adresser()));
        pdlPerson.setDoedfoedtBarn(tilDødfødtBarn(barnaTilPerson));

        // Ikke implementert
        pdlPerson.setOppholdsadresse(List.of());
        pdlPerson.setDeltBosted(List.of());
        pdlPerson.setForeldreansvar(List.of());
        pdlPerson.setIdentitetsgrunnlag(List.of());
        pdlPerson.setKontaktinformasjonForDoedsbo(List.of());
        pdlPerson.setOpphold(List.of());
        pdlPerson.setSikkerhetstiltak(List.of());
        pdlPerson.setTelefonnummer(List.of());
        pdlPerson.setTilrettelagtKommunikasjon(List.of());
        pdlPerson.setUtenlandskIdentifikasjonsnummer(List.of());
        pdlPerson.setInnflyttingTilNorge(List.of());
        pdlPerson.setUtflyttingFraNorge(List.of());
        pdlPerson.setVergemaalEllerFremtidsfullmakt(List.of());

        return pdlPerson;
    }

    private static List<DoedfoedtBarn> tilDødfødtBarn(List<Person> barnaTilPerson) {
        return barnaTilPerson.stream()
                .filter(PersonMapper::erDødsfødtBarn)
                .map(PersonMapper::tilDødfødtBarn)
                .toList();
    }

    private static boolean erDødsfødtBarn(Person barnet) {
        var personopplysninger = barnet.personopplysninger();
        return personopplysninger.dødsdato() != null && personopplysninger.dødsdato().equals(personopplysninger.fødselsdato());
    }

    private static DoedfoedtBarn tilDødfødtBarn(Person barnet) {
        var doedfoedtBarn = new DoedfoedtBarn();
        doedfoedtBarn.setDato(barnet.personopplysninger().dødsdato().format(DATO_FORMATTERER));
        return doedfoedtBarn;
    }

    private static List<Navn> tilNavn(Person person) {
        var navn = person.personopplysninger().navn();
        return List.of(no.nav.pdl.Navn.builder()
                .setFornavn(navn.fornavn().toUpperCase())
                .setEtternavn(navn.etternavn().toUpperCase())
                .setForkortetNavn(navn.etternavn().toUpperCase() + " " + navn.fornavn().toUpperCase()) // TODO: Fjern? er deprekert.
                .build());
    }

    private static List<Kontaktadresse> tilKontaktadresse(List<Adresse> adresser) {
        return adresser.stream()
                .filter(adresse -> adresse.adresseType().equals(Adresse.AdresseType.POSTADRESSE))
                .map(PersonMapper::tilKontaktadresse)
                .toList();
    }

    private static List<Bostedsadresse> tilBostedsadresser(List<Adresse> adresser) {
        return adresser.stream()
                .filter(adresse -> adresse.adresseType().equals(Adresse.AdresseType.BOSTEDSADRESSE))
                .map(PersonMapper::tilBostedsadresse)
                .toList();
    }

    private static Bostedsadresse tilBostedsadresse(Adresse adresse) {
        var bostedsadresseBuilder = Bostedsadresse.builder()
            .setAngittFlyttedato(LocalDate.now().format(DATO_FORMATTERER))
            .setGyldigFraOgMed(adresse.fom() == null ?  toDate(LocalDate.of(2000, 1, 1)) : toDate(adresse.fom()))
            .setGyldigTilOgMed(adresse.tom() == null ? toDate(LocalDate.of(2050, 1, 1)) : toDate(adresse.tom()))
            .setCoAdressenavn(null)
            .setVegadresse(Vegadresse.builder()
                    .setAdressenavn("Haugesund ally")
                    .setHusbokstav("B")
                    .setHusnummer("10")
                    .setPostnummer("5511")
                    .setMatrikkelId(adresse.matrikkelId())
                    .build())
            .setMatrikkeladresse(Matrikkeladresse.builder()
                    .setPostnummer("5511")
                    .setMatrikkelId(adresse.matrikkelId())
                    .build())
            .setMetadata(Metadata.builder()
                    .setMaster("PDL")
                    .setHistorisk(false)
                    .build());
        if (!CountryCode.NO.equals(adresse.land())) {
            bostedsadresseBuilder.setUtenlandskAdresse(UtenlandskAdresse.builder()
                    .setAdressenavnNummer("Utenlandsveien 1")
                    .setPostkode("1234")
                    .setLandkode(adresse.land().getAlpha3())
                    .build());
        }
        return bostedsadresseBuilder.build();
    }

    private static Kontaktadresse tilKontaktadresse(Adresse adresse) {
        if (CountryCode.NO.equals(adresse.land())) {
            return Kontaktadresse.builder()
                .setType(KontaktadresseType.Innland)
                .setPostadresseIFrittFormat(PostadresseIFrittFormat.builder()
                    .setAdresselinje1("President Avenue")
                    .setAdresselinje2("Central district")
                    .setAdresselinje3("Utlandet")
                    .build())
                .build();
        } else {
            return Kontaktadresse.builder()
                .setType(KontaktadresseType.Utland)
                .setUtenlandskAdresse(UtenlandskAdresse.builder()
                    .setAdressenavnNummer("Utenlandsveien 1")
                    .setPostkode("1234")
                    .setLandkode(adresse.land().getAlpha3())
                    .build())
                .build();
        }
    }

    private static List<Adressebeskyttelse> tilAdressebeskyttelse(Person person) {
        var adresser = person.personopplysninger().adresser();
        var gradering = adresser == null || adresser.adressebeskyttelse() == null
                ? AdressebeskyttelseGradering.UGRADERT
                : switch (adresser.adressebeskyttelse()) {
                    case STRENGT_FORTROLIG -> AdressebeskyttelseGradering.STRENGT_FORTROLIG;
                    case FORTROLIG -> AdressebeskyttelseGradering.FORTROLIG;
                    case UGRADERT -> AdressebeskyttelseGradering.UGRADERT;
                };
        var adressebeskyttelse = new Adressebeskyttelse();
        adressebeskyttelse.setGradering(gradering);
        return List.of(adressebeskyttelse);
    }

    private static List<Foedselsdato> tilFoedselsdato(Person person) {
        var fødselsdato = person.personopplysninger().fødselsdato();
        if (fødselsdato == null) return List.of();
        var foedselsdato = new Foedselsdato();
        foedselsdato.setFoedselsdato(fødselsdato.format(DATO_FORMATTERER));
        return List.of(foedselsdato);
    }

    private static List<Doedsfall> tilDoedsfall(Person person) {
        var dødsdato = person.personopplysninger().dødsdato();
        if (dødsdato == null) return List.of();
        var doedsfall = new Doedsfall();
        doedsfall.setDoedsdato(dødsdato.format(DATO_FORMATTERER));
        return List.of(doedsfall);
    }

    private static List<Folkeregisteridentifikator> tilFolkeregisteridentifikator(Person person) {
        var identifikator = person.personopplysninger().identifikator();
        if (!(identifikator instanceof PersonIdent personIdent)) return List.of();
        var ident = new Folkeregisteridentifikator();
        ident.setIdentifikasjonsnummer(personIdent.fnr());
        ident.setStatus("I_BRUK");
        ident.setType("FNR");
        return List.of(ident);
    }

    private static List<Folkeregisterpersonstatus> tilFolkeregisterpersonstatus(Person person) {
        return person.personopplysninger().personstatus().stream()
                .map(PersonMapper::tilPersonstatus)
                .toList();
    }

    private static Folkeregisterpersonstatus tilPersonstatus(Personstatus ps) {
        var pdlStatus = no.nav.pdl.oversetter.Personstatus.valueOf(ps.personstatus().name());
        return Folkeregisterpersonstatus.builder()
                .setStatus(pdlStatus.getStatus())
                .setForenkletStatus(pdlStatus.getForenkletStatus())
                .setFolkeregistermetadata(Folkeregistermetadata.builder()
                        .setAjourholdstidspunkt(ps.fom() != null ? toDate(ps.fom()) : null)
                        .setGyldighetstidspunkt(ps.fom() != null ? toDate(ps.fom()) : null)
                        .setOpphoerstidspunkt(ps.tom() != null ? toDate(ps.tom()) : null)
                        .build())
                .build();
    }

    private static List<Kjoenn> tilKjoenn(Person person) {
        var kjønn = person.personopplysninger().kjønn();
        var kjoenn = new Kjoenn();
        var kjoenntype = switch (kjønn) {
            case K -> KjoennType.KVINNE;
            case M -> KjoennType.MANN;
            case null -> KjoennType.UKJENT;
        };
        kjoenn.setKjoenn(kjoenntype);
        return List.of(kjoenn);
    }

    private static List<Sivilstand> tilSivilstand(Person person) {
        return person.personopplysninger().sivilstand().stream()
                .map(s -> {
                    var sivilstand = new Sivilstand();
                    //sivilstand.setGyldigFraOgMed(s.fom() != null ? s.fom().format(DATO_FORMATTERER) : null); // AKtiver
                    sivilstand.setType(Sivilstandstype.valueOf(s.sivilstand().name()));
                    var annenpartOpt = person.personopplysninger().familierelasjoner().stream()
                            .filter(f -> Set.of(Familierelasjon.Relasjon.EKTE, Familierelasjon.Relasjon.SAMBOER).contains(f.relasjon()))
                            .findFirst();
                    if (annenpartOpt.isPresent() && Set.of(no.nav.vtp.personopplysninger.Sivilstand.Type.GIFT,
                            no.nav.vtp.personopplysninger.Sivilstand.Type.REGISTRERT_PARTNER,
                            no.nav.vtp.personopplysninger.Sivilstand.Type.SEPARERT_PARTNER,
                            no.nav.vtp.personopplysninger.Sivilstand.Type.SKILT_PARTNER,
                            no.nav.vtp.personopplysninger.Sivilstand.Type.GJENLEVENDE_PARTNER)
                            .contains(s.sivilstand())) {
                        sivilstand.setRelatertVedSivilstand(annenpartOpt.get().relatertTilId().value());
                    }
                    return sivilstand;
                })
                .toList();
    }



    private static List<Statsborgerskap> tilStatsborgerskap(Person person) {
        return person.personopplysninger().statsborgerskap().stream()
                .map(s -> {
                    var statsborgerskap = new Statsborgerskap();
                    statsborgerskap.setLand(s.land().getAlpha3());
                    return statsborgerskap;
                })
                .toList();
    }

    private static List<ForelderBarnRelasjon> tilForelderBarnRelasjon(Person person) {
        return person.personopplysninger().familierelasjoner().stream()
                .filter(f -> erForelderBarnRelasjon(f.relasjon()))
                .map(f -> {
                    var relasjon = new ForelderBarnRelasjon();
                    relasjon.setRelatertPersonsIdent(f.relatertTilId().value());
                    relasjon.setRelatertPersonsRolle(tilForelderBarnRolle(f.relasjon()));
                    relasjon.setMinRolleForPerson(tilForelderBarnRolle(person.personopplysninger().rolle()));
                    return relasjon;
                })
                .toList();
    }

    private static boolean erForelderBarnRelasjon(Familierelasjon.Relasjon relasjon) {
        return switch (relasjon) {
            case BARN, FAR, MOR, MEDMOR -> true;
            default -> false;
        };
    }

    private static ForelderBarnRelasjonRolle tilForelderBarnRolle(Familierelasjon.Relasjon relasjon) {
        return switch (relasjon) {
            case MOR -> ForelderBarnRelasjonRolle.MOR;
            case FAR -> ForelderBarnRelasjonRolle.FAR;
            case MEDMOR -> ForelderBarnRelasjonRolle.MEDMOR;
            case BARN ->  ForelderBarnRelasjonRolle.BARN;
            default -> throw new IllegalArgumentException("Ikke en forelder-barn rolle: " + relasjon);
        };
    }

    // TODO: Bruk samme enum som over?
    private static ForelderBarnRelasjonRolle tilForelderBarnRolle(Rolle rolle) {
        return switch (rolle) {
            case MOR -> ForelderBarnRelasjonRolle.MOR;
            case FAR -> ForelderBarnRelasjonRolle.FAR;
            case MEDMOR -> ForelderBarnRelasjonRolle.MEDMOR;
            case BARN ->  ForelderBarnRelasjonRolle.BARN;
            default -> throw new IllegalArgumentException("Ikke en forelder-barn rolle: " + rolle);
        };
    }

    private static Date toDate(LocalDate dateTime) {
        return Date.from(dateTime.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }


}


