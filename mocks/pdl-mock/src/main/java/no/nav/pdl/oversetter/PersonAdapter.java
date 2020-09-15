package no.nav.pdl.oversetter;

import java.time.format.DateTimeFormatter;
import java.util.List;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.GeografiskTilknytningModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.pdl.Foedsel;
import no.nav.pdl.Folkeregisterpersonstatus;
import no.nav.pdl.GeografiskTilknytning;
import no.nav.pdl.Kjoenn;
import no.nav.pdl.KjoennType;
import no.nav.pdl.Navn;
import no.nav.pdl.Person;
import no.nav.pdl.Statsborgerskap;


public class PersonAdapter {
    static final DateTimeFormatter DATO_FORMATTERER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static Person oversettPerson(PersonModell personModell) {
        var person = new Person();

        Foedsel fødsel = new Foedsel();
        fødsel.setFoedselsdato(personModell.getFødselsdato().format(DATO_FORMATTERER));
        person.setFoedsel(List.of(fødsel));

        Navn navn = new Navn();
        navn.setFornavn(personModell.getFornavn().toUpperCase());
        navn.setEtternavn(personModell.getEtternavn().toUpperCase());
        navn.setForkortetNavn(personModell.getEtternavn().toUpperCase() + " " + personModell.getFornavn().toUpperCase());
        person.setNavn(List.of(navn));

        Statsborgerskap statsborgerskap = new Statsborgerskap();
        statsborgerskap.setLand(personModell.getStatsborgerskap().getLandkode());
        person.setStatsborgerskap(List.of(statsborgerskap));

        Kjoenn kjoenn = new Kjoenn();
        kjoenn.setKjoenn(KjoennType.KVINNE);
        kjoenn.setKjoenn(personModell.getKjønn() == BrukerModell.Kjønn.K ? KjoennType.KVINNE : KjoennType.MANN);
        person.setKjoenn(List.of(kjoenn));

        Folkeregisterpersonstatus personstatus = new Folkeregisterpersonstatus();
        personstatus.setStatus(personModell.getPersonstatus().getStatus());
        person.setFolkeregisterpersonstatus(List.of(personstatus));

        person.setGeografiskTilknytning(tilGeografiskTilknytning(personModell));

        person = AdresseAdapter.setAdresser(person, personModell);

        return person;
    }

    private static GeografiskTilknytning tilGeografiskTilknytning(PersonModell bruker) {
        GeografiskTilknytningModell tilknytning = bruker.getGeografiskTilknytning();
        if (tilknytning == null) {
            return null;
        } else {
            GeografiskTilknytning geo = new GeografiskTilknytning();
            // TODO: Legg til støtte for alle i switch
            switch (tilknytning.getGeografiskTilknytningType()) {
                case Land:
                    geo.setGtLand(tilknytning.getKode());
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
            return geo;
        }
    }

}
