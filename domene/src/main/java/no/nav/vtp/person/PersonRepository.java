package no.nav.vtp.person;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import no.nav.vtp.person.arbeidsforhold.Organisasjon;
import no.nav.vtp.person.ident.Orgnummer;

public class PersonRepository {

    private static final Map<String, Person> PERSONER = new ConcurrentHashMap<>();

    public static Set<String> alleIdenter() {
        return PERSONER.keySet();
    }

    public static void leggTilPerson(Person person) {
        PERSONER.put(person.personopplysninger().identifikator().value(), person);
    }

    public static void leggTilPersoner(List<Person> personer) {
        personer.forEach(PersonRepository::leggTilPerson);
    }

    public static Person hentPerson(String ident) {
        if (ident.length() == 13) { // Aktørid
            var fnr = ident.replaceFirst("99", "");
            return PERSONER.get(fnr);
        }
        return PERSONER.get(ident);
    }

    public static Optional<Person> hentPerson(UUID uuid) {
        return PERSONER.values().stream().filter(p -> p.personopplysninger().uuid().equals(uuid)).findFirst();
    }

    public static Optional<Organisasjon> hentInformasjonOmArbeidsforhold(Orgnummer orgnummer) {
        return alleRegistrerteOrganisasjoner().stream()
                .filter(o -> o.orgnummer().equals(orgnummer))
                .findFirst();
    }

    // TODO: Flytt ut?
    public static Set<Organisasjon> alleRegistrerteOrganisasjoner() {
        var alleOrgnummereFraRegistrerteArbeidsforhold = PERSONER.values().stream()
                .flatMap(p -> p.arbeidsforhold().stream())
                .filter(a -> a.arbeidsgiver() instanceof Organisasjon)
                .map(a -> ((Organisasjon) a.arbeidsgiver()))
                .collect(Collectors.toSet());
        var alleOrgnummerFraInntektopplysninger = PERSONER.values().stream()
                .flatMap(p -> p.inntekt().stream())
                .filter(a -> a.arbeidsgiver() instanceof Organisasjon)
                .map(a -> ((Organisasjon) a.arbeidsgiver()))
                .collect(Collectors.toSet());
        return Stream.concat(alleOrgnummereFraRegistrerteArbeidsforhold.stream(), alleOrgnummerFraInntektopplysninger.stream())
                .collect(Collectors.toSet());
    }
}
