package no.nav.vtp;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PersonRepository {

    private static Map<String, Person> personer = new ConcurrentHashMap<>();

    public void leggTilPerson(Person person) {
        personer.put(person.personopplysninger().identifikator().ident(), person);
    }

    public void leggTilPersoner(List<Person> personer) {
        personer.forEach(this::leggTilPerson);
    }

    public Person hentPerson(String ident) {
        return personer.get(ident);
    }

    public void endrePerson(Person person) {
        leggTilPerson(person);
    }

}
