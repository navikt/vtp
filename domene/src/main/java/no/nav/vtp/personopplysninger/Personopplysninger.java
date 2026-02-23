package no.nav.vtp.personopplysninger;

import java.time.LocalDate;
import java.util.List;

import no.nav.vtp.ident.Identifikator;

public record Personopplysninger(Identifikator identifikator,
                                 LocalDate fødselsdato,
                                 LocalDate dødsdato,
                                 String språk,
                                 Kjønn kjønn,
                                 GeografiskTilknytning geografiskTilknytning,
                                 List<Familierelasjon> familierelasjoner,
                                 List<Statsborgerskap> statsborgerskap,
                                 List<Sivilstand> sivilstand,
                                 List<Personstatus> personstatus,
                                 List<Medlemskap> medlemskap,
                                 Adresser adresser,
                                 boolean erSkjermet) {
}
