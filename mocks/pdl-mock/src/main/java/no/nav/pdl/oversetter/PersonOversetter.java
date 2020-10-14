package no.nav.pdl.oversetter;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.GeografiskTilknytningModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.StatsborgerskapModell;
import no.nav.pdl.*;

import java.time.format.DateTimeFormatter;

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

        Folkeregisterpersonstatus personstatus = new Folkeregisterpersonstatus();
        personstatus.setStatus(personModell.getPersonstatus().getStatus());
        person.setFolkeregisterpersonstatus(of(personstatus));

        person.setGeografiskTilknytning(tilGeografiskTilknytning(personModell));

        AdresseAdapter.setAdresser(person, personModell);

        return person;
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
