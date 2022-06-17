package no.nav.pdl.oversetter;

import static no.nav.pdl.oversetter.AdresseAdapter.setAdresser;
import static no.nav.pdl.oversetter.DoedfoedtBarnAdapter.tilDoedfoedtBarn;
import static no.nav.pdl.oversetter.ForelderBarnRelasjonBygger.byggForelderBarnRelasjoner;
import static no.nav.pdl.oversetter.SivilstandBygger.leggTilSivilstand;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.AdresseModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.AdresseType;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonstatusModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.StatsborgerskapModell;
import no.nav.pdl.Adressebeskyttelse;
import no.nav.pdl.AdressebeskyttelseGradering;
import no.nav.pdl.Doedsfall;
import no.nav.pdl.Foedsel;
import no.nav.pdl.Folkeregisteridentifikator;
import no.nav.pdl.Folkeregistermetadata;
import no.nav.pdl.Folkeregisterpersonstatus;
import no.nav.pdl.Kjoenn;
import no.nav.pdl.KjoennType;
import no.nav.pdl.Navn;
import no.nav.pdl.Person;
import no.nav.pdl.Statsborgerskap;


public class PersonAdapter {
    static final DateTimeFormatter DATO_FORMATTERER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

    private PersonAdapter() {
        // Ikke instansieres!
    }

    public static Person tilPerson(PersonModell personModell, Personopplysninger personopplysninger, boolean historikk) {
        var person = new Person();
        person.setAdressebeskyttelse(tilAdressebeskyttelse(personModell));
        // bostedsadresse, oppholdsadresse, kontaktadresse, deltBosted settes metode under.
        setAdresser(person, hentAdresseModellTPS(personModell, historikk));
        person.setDoedsfall(tilDoedsfall(personModell));
        person.setDoedfoedtBarn(tilDoedfoedtBarn(personModell.getAktørIdent(), personopplysninger));
        byggForelderBarnRelasjoner(personModell.getAktørIdent(), personopplysninger, person);
        person.setFoedsel(tilFoedsel(personModell));
        person.setFolkeregisteridentifikator(tilFolkeregisteridentifkkator(personModell));
        person.setFolkeregisterpersonstatus(tilFolkeregisterpersonstatuse(personModell, historikk));
        person.setForeldreansvar(ikkeImplementert());
        person.setFullmakt(ikkeImplementert());
        person.setIdentitetsgrunnlag(ikkeImplementert());
        person.setKjoenn(tilKjoenn(personModell));
        person.setKontaktinformasjonForDoedsbo(ikkeImplementert());
        person.setNavn(tilNavn(personModell));
        person.setOpphold(ikkeImplementert());
        person.setSikkerhetstiltak(ikkeImplementert());
        leggTilSivilstand(person, personModell, personopplysninger);
        person.setStatsborgerskap(tilStatsborgerskaps(personModell, historikk));
        person.setTelefonnummer(ikkeImplementert());
        person.setTilrettelagtKommunikasjon(ikkeImplementert());
        person.setUtenlandskIdentifikasjonsnummer(ikkeImplementert());
        person.setInnflyttingTilNorge(ikkeImplementert());
        person.setUtflyttingFraNorge(ikkeImplementert());
        person.setVergemaalEllerFremtidsfullmakt(ikkeImplementert());
        return person;
    }

    private static List<AdresseModell> hentAdresseModellTPS(PersonModell personModell, boolean historikk) {
        return historikk ? personModell.getAdresser(AdresseType.BOSTEDSADRESSE) : personModell.getAdresser();
    }

    private static List<Adressebeskyttelse> tilAdressebeskyttelse(PersonModell personModell) {
        var adressebeskyttelse = new Adressebeskyttelse();
        adressebeskyttelse.setGradering(tilAdressebeskyttelseGradering(personModell));
        return List.of(adressebeskyttelse);
    }


    private static List<Kjoenn> tilKjoenn(PersonModell personModell) {
        var kjoenn = new Kjoenn();
        if (personModell.getKjønn() == null) {
            kjoenn.setKjoenn(KjoennType.UKJENT);
            return List.of(kjoenn);
        }
        kjoenn.setKjoenn(personModell.getKjønn() == BrukerModell.Kjønn.K ? KjoennType.KVINNE : KjoennType.MANN);
        return List.of(kjoenn);
    }


    private static List<Navn> tilNavn(PersonModell personModell) {
        Navn navn = new Navn();
        navn.setFornavn(personModell.getFornavn().toUpperCase());
        navn.setEtternavn(personModell.getEtternavn().toUpperCase());
        navn.setForkortetNavn(personModell.getEtternavn().toUpperCase() + " " + personModell.getFornavn().toUpperCase());
        return List.of(navn);
    }

    private static List<Folkeregisteridentifikator> tilFolkeregisteridentifkkator(PersonModell personmodell) {
        Folkeregisteridentifikator ident = new Folkeregisteridentifikator();
        ident.setIdentifikasjonsnummer(personmodell.getIdent());
        ident.setStatus("I_BRUK");
        ident.setType("FNR");
        return List.of(ident);
    }

    private static List<Foedsel> tilFoedsel(PersonModell personModell) {
        var fødsel = new Foedsel();
        fødsel.setFoedselsdato(personModell.getFødselsdato().format(DATO_FORMATTERER));
        return List.of(fødsel);
    }

    private static List<Doedsfall> tilDoedsfall(PersonModell personModell) {
        if (personModell.getDødsdato() == null) {
            return List.of();
        }
        var doedsfall = new Doedsfall();
        doedsfall.setDoedsdato(personModell.getDødsdato().format(DATO_FORMATTERER));
        return List.of(doedsfall);
    }

    private static List<Folkeregisterpersonstatus> tilFolkeregisterpersonstatuse(PersonModell personModell, boolean historikk) {
        return historikk
                ? personModell.getAllePersonstatus().stream()
                .map(PersonAdapter::tilFolkeregisterpersonstatus)
                .toList()
                : List.of(tilFolkeregisterpersonstatus(personModell.getPersonstatus()));
    }

    private static Folkeregisterpersonstatus tilFolkeregisterpersonstatus(PersonstatusModell personstatusModell) {
        var folkeregisterpersonstatus = new Folkeregisterpersonstatus();
        if (personstatusModell.getStatus() == null) {
            return folkeregisterpersonstatus;
        }
        var personstatuserPDL = Personstatus.valueOf(personstatusModell.getStatus());
        folkeregisterpersonstatus.setForenkletStatus(personstatuserPDL.getForenkletStatus());
        folkeregisterpersonstatus.setStatus(personstatuserPDL.getStatus());

        var builder = Folkeregistermetadata.builder();
        if (personstatusModell.getFom() != null) {
            builder.setAjourholdstidspunkt(toDate(personstatusModell.getFom()))
                    .setGyldighetstidspunkt(toDate(personstatusModell.getFom()));
        }
        if (personstatusModell.getTom() != null) {
            builder.setOpphoerstidspunkt(toDate(personstatusModell.getTom()));
        }
        folkeregisterpersonstatus.setFolkeregistermetadata(builder.build());
        return folkeregisterpersonstatus;
    }

    private static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(DEFAULT_ZONE_ID).toInstant());
    }

    private static AdressebeskyttelseGradering tilAdressebeskyttelseGradering(PersonModell bruker) {
        var diskresjonskodeType = bruker.getDiskresjonskodeType();
        if (diskresjonskodeType == null) {
            return AdressebeskyttelseGradering.UGRADERT;
        }
        return switch (diskresjonskodeType) {
            case SPSF -> AdressebeskyttelseGradering.STRENGT_FORTROLIG;
            case SPFO -> AdressebeskyttelseGradering.FORTROLIG;
            default -> AdressebeskyttelseGradering.UGRADERT;
        };
    }

    private static List<Statsborgerskap> tilStatsborgerskaps(PersonModell personModell, boolean historikk) {
        return historikk
                ? personModell.getAlleStatsborgerskap().stream()
                .map(PersonAdapter::tilStatsborgerskap)
                .toList()
                : List.of(tilStatsborgerskap(personModell.getStatsborgerskap()));
    }

    private static Statsborgerskap tilStatsborgerskap(StatsborgerskapModell sm) {
        var statsborgerskap = new Statsborgerskap();
        statsborgerskap.setLand(sm.getLandkode());
        return statsborgerskap;
    }

    private static <T> List<T> ikkeImplementert() {
        return List.of();
    }

}
