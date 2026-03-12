package no.nav.vtp.personopplysninger;

import java.time.LocalDate;
import java.util.List;

import no.nav.vtp.ident.Identifikator;

/*
   Forenklet modell av PDL + medlemskap
 */
public record Personopplysninger(Identifikator identifikator,
                                 Rolle rolle,
                                 Navn navn,
                                 LocalDate fødselsdato,
                                 LocalDate dødsdato,
                                 Språk språk,
                                 Kjønn kjønn,
                                 GeografiskTilknytning geografiskTilknytning,
                                 List<Familierelasjon> familierelasjoner,
                                 List<Statsborgerskap> statsborgerskap,
                                 List<Sivilstand> sivilstand,
                                 List<Personstatus> personstatus,
                                 List<Medlemskap> medlemskap,
                                 Adresser adresser,
                                 boolean erSkjermet) {

    public Builder tilBuilder() {
        return new Builder(this);
    }

    public static class Builder {
        private Personopplysninger kopi;

        public Builder(Personopplysninger personopplysninger) {
            this.kopi = personopplysninger;
        }

        public Builder medIdentifikator(Identifikator identifikator) {
            kopi = new Personopplysninger(identifikator, kopi.rolle, kopi.navn, kopi.fødselsdato, kopi.dødsdato, kopi.språk, kopi.kjønn, kopi.geografiskTilknytning, kopi.familierelasjoner, kopi.statsborgerskap, kopi.sivilstand, kopi.personstatus, kopi.medlemskap, kopi.adresser, kopi.erSkjermet);
            return this;
        }

        public Builder medRolle(Rolle rolle) {
            kopi = new Personopplysninger(kopi.identifikator, rolle, kopi.navn, kopi.fødselsdato, kopi.dødsdato, kopi.språk, kopi.kjønn, kopi.geografiskTilknytning, kopi.familierelasjoner, kopi.statsborgerskap, kopi.sivilstand, kopi.personstatus, kopi.medlemskap, kopi.adresser, kopi.erSkjermet);
            return this;
        }

        public Builder medNavn(Navn navn) {
            kopi = new Personopplysninger(kopi.identifikator, kopi.rolle, navn, kopi.fødselsdato, kopi.dødsdato, kopi.språk, kopi.kjønn, kopi.geografiskTilknytning, kopi.familierelasjoner, kopi.statsborgerskap, kopi.sivilstand, kopi.personstatus, kopi.medlemskap, kopi.adresser, kopi.erSkjermet);
            return this;
        }

        public Builder medFødselsdato(LocalDate fødselsdato) {
            kopi = new Personopplysninger(kopi.identifikator, kopi.rolle, kopi.navn, fødselsdato, kopi.dødsdato, kopi.språk, kopi.kjønn, kopi.geografiskTilknytning, kopi.familierelasjoner, kopi.statsborgerskap, kopi.sivilstand, kopi.personstatus, kopi.medlemskap, kopi.adresser, kopi.erSkjermet);
            return this;
        }

        public Builder medDødsdato(LocalDate dødsdato) {
            kopi = new Personopplysninger(kopi.identifikator, kopi.rolle, kopi.navn, kopi.fødselsdato, dødsdato, kopi.språk, kopi.kjønn, kopi.geografiskTilknytning, kopi.familierelasjoner, kopi.statsborgerskap, kopi.sivilstand, kopi.personstatus, kopi.medlemskap, kopi.adresser, kopi.erSkjermet);
            return this;
        }

        public Builder medSpråk(Språk språk) {
            kopi = new Personopplysninger(kopi.identifikator, kopi.rolle, kopi.navn, kopi.fødselsdato, kopi.dødsdato, språk, kopi.kjønn, kopi.geografiskTilknytning, kopi.familierelasjoner, kopi.statsborgerskap, kopi.sivilstand, kopi.personstatus, kopi.medlemskap, kopi.adresser, kopi.erSkjermet);
            return this;
        }

        public Builder medKjønn(Kjønn kjønn) {
            kopi = new Personopplysninger(kopi.identifikator, kopi.rolle, kopi.navn, kopi.fødselsdato, kopi.dødsdato, kopi.språk, kjønn, kopi.geografiskTilknytning, kopi.familierelasjoner, kopi.statsborgerskap, kopi.sivilstand, kopi.personstatus, kopi.medlemskap, kopi.adresser, kopi.erSkjermet);
            return this;
        }

        public Builder medGeografiskTilknytning(GeografiskTilknytning geografiskTilknytning) {
            kopi = new Personopplysninger(kopi.identifikator, kopi.rolle, kopi.navn, kopi.fødselsdato, kopi.dødsdato, kopi.språk, kopi.kjønn, geografiskTilknytning, kopi.familierelasjoner, kopi.statsborgerskap, kopi.sivilstand, kopi.personstatus, kopi.medlemskap, kopi.adresser, kopi.erSkjermet);
            return this;
        }

        public Builder medFamilierelasjoner(List<Familierelasjon> familierelasjoner) {
            kopi = new Personopplysninger(kopi.identifikator, kopi.rolle, kopi.navn, kopi.fødselsdato, kopi.dødsdato, kopi.språk, kopi.kjønn, kopi.geografiskTilknytning, familierelasjoner, kopi.statsborgerskap, kopi.sivilstand, kopi.personstatus, kopi.medlemskap, kopi.adresser, kopi.erSkjermet);
            return this;
        }

        public Builder medStatsborgerskap(List<Statsborgerskap> statsborgerskap) {
            kopi = new Personopplysninger(kopi.identifikator, kopi.rolle, kopi.navn, kopi.fødselsdato, kopi.dødsdato, kopi.språk, kopi.kjønn, kopi.geografiskTilknytning, kopi.familierelasjoner, statsborgerskap, kopi.sivilstand, kopi.personstatus, kopi.medlemskap, kopi.adresser, kopi.erSkjermet);
            return this;
        }

        public Builder medSivilstand(List<Sivilstand> sivilstand) {
            kopi = new Personopplysninger(kopi.identifikator, kopi.rolle, kopi.navn, kopi.fødselsdato, kopi.dødsdato, kopi.språk, kopi.kjønn, kopi.geografiskTilknytning, kopi.familierelasjoner, kopi.statsborgerskap, sivilstand, kopi.personstatus, kopi.medlemskap, kopi.adresser, kopi.erSkjermet);
            return this;
        }

        public Builder medPersonstatus(List<Personstatus> personstatus) {
            kopi = new Personopplysninger(kopi.identifikator, kopi.rolle, kopi.navn, kopi.fødselsdato, kopi.dødsdato, kopi.språk, kopi.kjønn, kopi.geografiskTilknytning, kopi.familierelasjoner, kopi.statsborgerskap, kopi.sivilstand, personstatus, kopi.medlemskap, kopi.adresser, kopi.erSkjermet);
            return this;
        }

        public Builder medMedlemskap(List<Medlemskap> medlemskap) {
            kopi = new Personopplysninger(kopi.identifikator, kopi.rolle, kopi.navn, kopi.fødselsdato, kopi.dødsdato, kopi.språk, kopi.kjønn, kopi.geografiskTilknytning, kopi.familierelasjoner, kopi.statsborgerskap, kopi.sivilstand, kopi.personstatus, medlemskap, kopi.adresser, kopi.erSkjermet);
            return this;
        }

        public Builder medAdresser(Adresser adresser) {
            kopi = new Personopplysninger(kopi.identifikator, kopi.rolle, kopi.navn, kopi.fødselsdato, kopi.dødsdato, kopi.språk, kopi.kjønn, kopi.geografiskTilknytning, kopi.familierelasjoner, kopi.statsborgerskap, kopi.sivilstand, kopi.personstatus, kopi.medlemskap, adresser, kopi.erSkjermet);
            return this;
        }

        public Builder medErSkjermet(boolean erSkjermet) {
            kopi = new Personopplysninger(kopi.identifikator, kopi.rolle, kopi.navn, kopi.fødselsdato, kopi.dødsdato, kopi.språk, kopi.kjønn, kopi.geografiskTilknytning, kopi.familierelasjoner, kopi.statsborgerskap, kopi.sivilstand, kopi.personstatus, kopi.medlemskap, kopi.adresser, erSkjermet);
            return this;
        }

        public Personopplysninger build() {
            return kopi;
        }
    }
}
