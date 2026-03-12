package no.nav.vtp;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import no.nav.vtp.arbeidsforhold.Organisasjon;
import no.nav.vtp.ident.Orgnummer;

public class PersonRepository {

    private static Map<String, Person> personer = new ConcurrentHashMap<>();

    public void leggTilPerson(Person person) {
        personer.put(person.personopplysninger().identifikator().value(), person);
    }

    public void leggTilPersoner(List<Person> personer) {
        personer.forEach(this::leggTilPerson);
    }

    public Person hentPerson(String ident) {
        if (ident.length() == 13) { // Aktørid
            var fnr = ident.replaceFirst("99", "");
            return personer.get(fnr);
        }
        return personer.get(ident);
    }

    public List<Person> allePersoner() {
        return List.copyOf(personer.values());
    }

    public Optional<Organisasjon> hentInformasjonOmArbeidsforhold(Orgnummer orgnummer) {
        return alleRegistrerteOrganisasjoner().stream()
                .filter(o -> o.orgnummer().equals(orgnummer))
                .findFirst();
    }

    // TODO: Flytt ut?
    public Set<Organisasjon> alleRegistrerteOrganisasjoner() {
        var alleOrgnummereFraRegistrerteArbeidsforhold = personer.values().stream()
                .flatMap(p -> p.arbeidsforhold().stream())
                .filter(a -> a.arbeidsgiver() instanceof Organisasjon)
                .map(a -> ((Organisasjon) a.arbeidsgiver()))
                .collect(Collectors.toSet());
        var alleOrgnummerFraInntektopplysninger = personer.values().stream()
                .flatMap(p -> p.inntekt().stream())
                .filter(a -> a.arbeidsgiver() instanceof Organisasjon)
                .map(a -> ((Organisasjon) a.arbeidsgiver()))
                .collect(Collectors.toSet());
        return Stream.concat(alleOrgnummereFraRegistrerteArbeidsforhold.stream(), alleOrgnummerFraInntektopplysninger.stream())
                .collect(Collectors.toSet());
    }

    public void endrePerson(Person person) {
        personer.replace(person.personopplysninger().identifikator().value(), person);
    }

}
