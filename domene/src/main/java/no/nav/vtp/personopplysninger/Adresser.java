package no.nav.vtp.personopplysninger;

import java.util.List;

public record Adresser(List<Adresse> adresser, Adressebeskyttelse adressebeskyttelse) {
}
