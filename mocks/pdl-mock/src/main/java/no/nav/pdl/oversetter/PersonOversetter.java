package no.nav.pdl.oversetter;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.*;
import no.nav.pdl.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.List.of;
import static java.util.stream.Collectors.toList;


public class PersonOversetter {
    static final DateTimeFormatter DATO_FORMATTERER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static Person oversettPerson(PersonModell personModell, boolean historikk) {
        var person = new Person();

        Foedsel fødsel = new Foedsel();
        fødsel.setFoedselsdato(personModell.getFødselsdato().format(DATO_FORMATTERER));
        person.setFoedsel(of(fødsel));

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

        person.setFolkeregisterpersonstatus(
                historikk
                        ? personModell.getAllePersonstatus().stream()
                        .map(PersonOversetter::tilFolkeregisterpersonstatus)
                        .collect(toList())
                        : of(tilFolkeregisterpersonstatus(personModell.getPersonstatus()))
        );

        person.setGeografiskTilknytning(tilGeografiskTilknytning(personModell));

        List<AdresseModell> adresserUtenHistorikk = personModell.getAdresser();
        List<AdresseModell> adresserMedHistorikk = personModell.getAdresser(AdresseType.BOSTEDSADRESSE);

        AdresseAdapter.setAdresser(person, historikk ? adresserMedHistorikk: adresserUtenHistorikk);

        return person;
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
