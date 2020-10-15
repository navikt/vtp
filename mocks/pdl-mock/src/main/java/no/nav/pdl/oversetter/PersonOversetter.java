package no.nav.pdl.oversetter;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.*;
import no.nav.pdl.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.GeografiskTilknytningModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
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
import static java.util.List.of;
import static java.util.stream.Collectors.toList;


public class PersonOversetter {
    static final DateTimeFormatter DATO_FORMATTERER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static Person oversettPerson(PersonModell personModell, boolean historikk) {
        var person = new Person();

        Foedsel fødsel = new Foedsel();
        fødsel.setFoedselsdato(personModell.getFødselsdato().format(DATO_FORMATTERER));
        person.setFoedsel(of(fødsel));

        Doedsfall doedsfall = new Doedsfall();
        if (personModell.getDødsdato() != null) {
            doedsfall.setDoedsdato(personModell.getDødsdato().format(DATO_FORMATTERER));
        }
        person.setDoedsfall(List.of(doedsfall));



        Navn navn = new Navn();
        navn.setFornavn(personModell.getFornavn().toUpperCase());
        navn.setEtternavn(personModell.getEtternavn().toUpperCase());
        navn.setForkortetNavn(personModell.getEtternavn().toUpperCase() + " " + personModell.getFornavn().toUpperCase());
        person.setNavn(of(navn));

        person.setStatsborgerskap(
                historikk
                        ? personModell.getAlleStatsborgerskap().stream()
                        .map(PersonOversetter::tilStatsborgerskap)
                        .collect(toList())
                        : of(tilStatsborgerskap(personModell.getStatsborgerskap()))
        );

        Kjoenn kjoenn = new Kjoenn();
        kjoenn.setKjoenn(KjoennType.KVINNE);
        kjoenn.setKjoenn(personModell.getKjønn() == BrukerModell.Kjønn.K ? KjoennType.KVINNE : KjoennType.MANN);
        person.setKjoenn(of(kjoenn));

        leggTilPersonstatus(person, personModell);
        person.setFolkeregisterpersonstatus(
                historikk
                        ? personModell.getAllePersonstatus().stream()
                        .map(PersonOversetter::tilFolkeregisterpersonstatus)
                        .collect(toList())
                        : of(tilFolkeregisterpersonstatus(personModell.getPersonstatus()))
        );

        person.setGeografiskTilknytning(tilGeografiskTilknytning(personModell));

        AdresseAdapter.setAdresser(person, personModell);

        Adressebeskyttelse adressebeskyttelse = new Adressebeskyttelse();
        adressebeskyttelse.setGradering(tilAdressebeskyttelseGradering(personModell));
        person.setAdressebeskyttelse(List.of(adressebeskyttelse));

        List<AdresseModell> adresserUtenHistorikk = personModell.getAdresser();
        List<AdresseModell> adresserMedHistorikk = personModell.getAdresser(AdresseType.BOSTEDSADRESSE);

        AdresseAdapter.setAdresser(person, historikk ? adresserMedHistorikk: adresserUtenHistorikk);

        return person;
    }

    private static void leggTilPersonstatus(Person person, PersonModell personModell) {
        List<String> personstatuserPDL = PersonstatusAdapter.hentPersonstatusPDL(personModell.getPersonstatus().getStatus());
        if (personstatuserPDL == null || personstatuserPDL.isEmpty()) {
            return;
        }
        Folkeregisterpersonstatus personstatus = new Folkeregisterpersonstatus();
        personstatus.setForenkletStatus(personstatuserPDL.get(0));
        personstatus.setStatus(personstatuserPDL.get(1));
        person.setFolkeregisterpersonstatus(List.of(personstatus));
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

    private static Folkeregisterpersonstatus tilFolkeregisterpersonstatus(PersonstatusModell status) {
        Folkeregisterpersonstatus folkeregisterpersonstatus = new Folkeregisterpersonstatus();
        folkeregisterpersonstatus.setStatus(status.getStatus());
        return folkeregisterpersonstatus;
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
