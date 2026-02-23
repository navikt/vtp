package no.nav.vtp.person.personopplysninger;

import java.util.List;

public record Adresser(List<Adresse> adresser, Adressebeskyttelse adressebeskyttelse) {
}
