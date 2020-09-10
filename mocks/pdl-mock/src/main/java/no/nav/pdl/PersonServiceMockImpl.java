package no.nav.pdl;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.pdl.graphql.FinnPerson;

public class PersonServiceMockImpl {

    private static final Logger LOG = LoggerFactory.getLogger(PersonServiceMockImpl.class);
    private static final DateTimeFormatter DATO_FORMATTERER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    private TestscenarioBuilderRepository repo;

    public PersonServiceMockImpl(TestscenarioBuilderRepository repo) {
        this.repo = repo;
    }

    public Person fra(PersonModell person) {
        var pers = new Person();

        Foedsel fødsel = new Foedsel();
        fødsel.setFoedselsdato(person.getFødselsdato().format(DATO_FORMATTERER));
        pers.setFoedsel(List.of(fødsel));

        Navn navn = new Navn();
        navn.setFornavn(person.getFornavn());
        navn.setEtternavn(person.getEtternavn());
        navn.setForkortetNavn(person.getFornavn() + " " + person.getEtternavn());
        //pers.setNavn(List.of(navn));

        Statsborgerskap statsborgerskap = new Statsborgerskap();
        statsborgerskap.setLand(person.getStatsborgerskap().getLandkode());
        //pers.setStatsborgerskap(List.of(statsborgerskap));

        return pers;
    }

    public Map<String, Object> hentPerson(String ident) {

        LOG.info("hentPerson. Ident: {}", ident);

        PersonModell bruker = finnPerson(ident);

        // TODO: Mappe relasjoner
        var pers = fra(bruker);

        Map<String, Person> data = Map.of("hentPerson", pers);
        Map<String, Object> response =Map.of("data", data);

        return response;
    }

    public PersonModell finnPerson(String ident) {
        return new FinnPerson(repo).finnPerson(ident);
    }
}


