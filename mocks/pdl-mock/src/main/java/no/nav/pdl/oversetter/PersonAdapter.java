package no.nav.pdl.oversetter;

import static java.util.stream.Collectors.toList;

import java.time.format.DateTimeFormatter;
import java.util.List;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.AdresseModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.AdresseType;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.GeografiskTilknytningModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonstatusModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.StatsborgerskapModell;
import no.nav.pdl.Adressebeskyttelse;
import no.nav.pdl.AdressebeskyttelseGradering;
import no.nav.pdl.Doedsfall;
import no.nav.pdl.Foedsel;
import no.nav.pdl.Folkeregisterpersonstatus;
import no.nav.pdl.GeografiskTilknytning;
import no.nav.pdl.GtType;
import no.nav.pdl.Kjoenn;
import no.nav.pdl.KjoennType;
import no.nav.pdl.Navn;
import no.nav.pdl.Person;
import no.nav.pdl.Statsborgerskap;


public class PersonAdapter {
    static final DateTimeFormatter DATO_FORMATTERER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static Person oversettPerson(PersonModell personModell, boolean historikk) {
        var person = new Person();

        Foedsel fødsel = tilFoedsel(personModell);
        person.setFoedsel(List.of(fødsel));

        Doedsfall doedsfall = tilDoedsfall(personModell);
        person.setDoedsfall(List.of(doedsfall));

        Navn navn = tilNavn(personModell);
        person.setNavn(List.of(navn));

        person.setStatsborgerskap(tilStatsborgerskaps(personModell, historikk));

        Kjoenn kjoenn = tilKjoenn(personModell);
        person.setKjoenn(List.of(kjoenn));

        person.setFolkeregisterpersonstatus(tilFolkeregisterpersonstatuse(personModell, historikk));

        person.setGeografiskTilknytning(tilGeografiskTilknytning(personModell));

        Adressebeskyttelse adressebeskyttelse = tilAdressebeskyttelse(personModell);
        person.setAdressebeskyttelse(List.of(adressebeskyttelse));

        AdresseAdapter.setAdresser(person, tilAdresseModeller(personModell, historikk));

        return person;
    }

    private static List<AdresseModell> tilAdresseModeller(PersonModell personModell, boolean historikk) {
        return historikk ? personModell.getAdresser(AdresseType.BOSTEDSADRESSE) : personModell.getAdresser();
    }

    private static Adressebeskyttelse tilAdressebeskyttelse(PersonModell personModell) {
        Adressebeskyttelse adressebeskyttelse = new Adressebeskyttelse();
        adressebeskyttelse.setGradering(tilAdressebeskyttelseGradering(personModell));
        return adressebeskyttelse;
    }


    private static Kjoenn tilKjoenn(PersonModell personModell) {
        Kjoenn kjoenn = new Kjoenn();
        kjoenn.setKjoenn(KjoennType.KVINNE);
        kjoenn.setKjoenn(personModell.getKjønn() == BrukerModell.Kjønn.K ? KjoennType.KVINNE : KjoennType.MANN);
        return kjoenn;
    }


    private static Navn tilNavn(PersonModell personModell) {
        Navn navn = new Navn();
        navn.setFornavn(personModell.getFornavn().toUpperCase());
        navn.setEtternavn(personModell.getEtternavn().toUpperCase());
        navn.setForkortetNavn(personModell.getEtternavn().toUpperCase() + " " + personModell.getFornavn().toUpperCase());
        return navn;
    }

    private static Foedsel tilFoedsel(PersonModell personModell) {
        Foedsel fødsel = new Foedsel();
        fødsel.setFoedselsdato(personModell.getFødselsdato().format(DATO_FORMATTERER));
        return fødsel;
    }

    private static Doedsfall tilDoedsfall(PersonModell personModell) {
        Doedsfall doedsfall = new Doedsfall();
        if (personModell.getDødsdato() != null) {
            doedsfall.setDoedsdato(personModell.getDødsdato().format(DATO_FORMATTERER));
        }
        return doedsfall;
    }

    private static List<Folkeregisterpersonstatus> tilFolkeregisterpersonstatuse(PersonModell personModell, boolean historikk) {
        return historikk
                ? personModell.getAllePersonstatus().stream()
                .map(PersonAdapter::tilFolkeregisterpersonstatus)
                .collect(toList())
                : List.of(tilFolkeregisterpersonstatus(personModell.getPersonstatus()));
    }

    private static Folkeregisterpersonstatus tilFolkeregisterpersonstatus(PersonstatusModell personstatusModell) {
        List<String> personstatuserPDL = PersonstatusKoder.hentPersonstatusPDL(personstatusModell.getStatus());
        Folkeregisterpersonstatus folkeregisterpersonstatus = new Folkeregisterpersonstatus();
        if (personstatuserPDL == null || personstatuserPDL.isEmpty()) {
            return folkeregisterpersonstatus;
        }
        folkeregisterpersonstatus.setForenkletStatus(personstatuserPDL.get(0));
        folkeregisterpersonstatus.setStatus(personstatuserPDL.get(1));
        return folkeregisterpersonstatus;
    }

    private static AdressebeskyttelseGradering tilAdressebeskyttelseGradering(PersonModell bruker) {
        PersonModell.Diskresjonskoder diskresjonskodeType = bruker.getDiskresjonskodeType();
        if (diskresjonskodeType == null) {
            return AdressebeskyttelseGradering.UGRADERT;
        }
        switch (diskresjonskodeType) {
            case SPSF:
                return AdressebeskyttelseGradering.STRENGT_FORTROLIG;
            case SPFO:
                return AdressebeskyttelseGradering.FORTROLIG;
            default:
                return AdressebeskyttelseGradering.UGRADERT;
        }
    }

    private static List<Statsborgerskap> tilStatsborgerskaps(PersonModell personModell, boolean historikk) {
        return historikk
                ? personModell.getAlleStatsborgerskap().stream()
                .map(PersonAdapter::tilStatsborgerskap)
                .collect(toList())
                : List.of(tilStatsborgerskap(personModell.getStatsborgerskap()));
    }

    private static Statsborgerskap tilStatsborgerskap(StatsborgerskapModell sm) {
        Statsborgerskap statsborgerskap = new Statsborgerskap();
        statsborgerskap.setLand(sm.getLandkode());
        return statsborgerskap;
    }

    private static GeografiskTilknytning tilGeografiskTilknytning(PersonModell bruker) {
        GeografiskTilknytningModell tilknytning = bruker.getGeografiskTilknytning();
        if (tilknytning == null) {
            return null;
        } else {
            GeografiskTilknytning geo = new GeografiskTilknytning();
            switch (tilknytning.getGeografiskTilknytningType()) {
                case Land:
                    geo.setGtType(GtType.UTLAND);
                    geo.setGtLand(tilknytning.getKode());
                    break;
                case Kommune:
                    geo.setGtType(GtType.KOMMUNE);
                    geo.setGtKommune(tilknytning.getKode());
                    break;
                case Bydel:
                    geo.setGtType(GtType.BYDEL);
                    geo.setGtBydel(tilknytning.getKode());
                default:
                    geo.setGtType(GtType.UDEFINERT);
            }
            return geo;
        }
    }

}
