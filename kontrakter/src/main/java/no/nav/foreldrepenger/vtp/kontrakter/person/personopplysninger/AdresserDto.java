package no.nav.foreldrepenger.vtp.kontrakter.person.personopplysninger;

import java.util.List;

public record AdresserDto(List<AdresseDto> adresser, Adressebeskyttelse adressebeskyttelse) {
}

