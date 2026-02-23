package no.nav.vtp.personopplysninger;

import java.time.LocalDate;
import java.util.List;

public record Personopplysninger(String identifikator,
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
    public String aktørId() {
        return "99" + identifikator;
    }
}
