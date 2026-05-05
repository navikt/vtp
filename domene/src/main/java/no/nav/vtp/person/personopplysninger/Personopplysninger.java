package no.nav.vtp.person.personopplysninger;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import no.nav.vtp.person.ident.Identifikator;

/*
   Forenklet modell av PDL + medlemskap
 */
public record Personopplysninger(Identifikator identifikator, UUID uuid, //genererert fra test
                                 Rolle rolle, Navn navn, LocalDate fødselsdato, LocalDate dødsdato, Språk språk, Kjønn kjønn,
                                 GeografiskTilknytning geografiskTilknytning, List<Familierelasjon> familierelasjoner,
                                 List<Statsborgerskap> statsborgerskap, List<Sivilstand> sivilstand, List<Personstatus> personstatus,
                                 List<Medlemskap> medlemskap, Adresser adresser, boolean erSkjermet) {

    public Builder tilBuilder() {
        return new Builder(this);
    }

    public static class Builder {
        private Personopplysninger kopi;

        public Builder(Personopplysninger personopplysninger) {
            this.kopi = personopplysninger;
        }

        public Builder medDødsdato(LocalDate dødsdato) {
            kopi = new Personopplysninger(kopi.identifikator, kopi.uuid, kopi.rolle, kopi.navn, kopi.fødselsdato, dødsdato, kopi.språk,
                    kopi.kjønn, kopi.geografiskTilknytning, kopi.familierelasjoner, kopi.statsborgerskap, kopi.sivilstand,
                    kopi.personstatus, kopi.medlemskap, kopi.adresser, kopi.erSkjermet);
            return this;
        }

        public Builder medFamilierelasjoner(List<Familierelasjon> familierelasjoner) {
            kopi = new Personopplysninger(kopi.identifikator, kopi.uuid, kopi.rolle, kopi.navn, kopi.fødselsdato, kopi.dødsdato,
                    kopi.språk, kopi.kjønn, kopi.geografiskTilknytning, familierelasjoner, kopi.statsborgerskap, kopi.sivilstand,
                    kopi.personstatus, kopi.medlemskap, kopi.adresser, kopi.erSkjermet);
            return this;
        }

        public Personopplysninger build() {
            return kopi;
        }
    }
}
