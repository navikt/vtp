package no.nav.foreldrepenger.vtp.kontrakter.person.v2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import no.nav.foreldrepenger.vtp.kontrakter.person.Adressebeskyttelse;
import no.nav.foreldrepenger.vtp.kontrakter.person.Kjønn;
import no.nav.foreldrepenger.vtp.kontrakter.person.Rolle;
import no.nav.foreldrepenger.vtp.kontrakter.person.Språk;

/** Identifikator og navn genereres server-side (se PersonMapperV2) og er derfor ikke del av kontrakten. */
public record PersonopplysningerDto(UUID uuid,
                                    Rolle rolle,
                                    LocalDate fødselsdato,
                                    LocalDate dødsdato,
                                    Språk språk,
                                    Kjønn kjønn,
                                    GeografiskTilknytningDto geografiskTilknytning,
                                    List<FamilierelasjonDto> familierelasjoner,
                                    List<StatsborgerskapDto> statsborgerskap,
                                    List<SivilstandDto> sivilstand,
                                    List<PersonstatusDto> personstatus,
                                    List<MedlemskapDto> medlemskap,
                                    List<AdresseDto> adresser,
                                    Adressebeskyttelse adressebeskyttelse,
                                    boolean erSkjermet) {

    private PersonopplysningerDto(Builder b) {
        this(b.uuid, b.rolle, b.fødselsdato, b.dødsdato, b.språk, b.kjønn, b.geografiskTilknytning, b.familierelasjoner,
                b.statsborgerskap, b.sivilstand, b.personstatus, b.medlemskap, b.adresser, b.adressebeskyttelse, b.erSkjermet);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID uuid;
        private Rolle rolle;
        private LocalDate fødselsdato;
        private LocalDate dødsdato;
        private Språk språk;
        private Kjønn kjønn;
        private GeografiskTilknytningDto geografiskTilknytning;
        private List<FamilierelasjonDto> familierelasjoner = new ArrayList<>();
        private List<StatsborgerskapDto> statsborgerskap = new ArrayList<>();
        private List<SivilstandDto> sivilstand = new ArrayList<>();
        private List<PersonstatusDto> personstatus = new ArrayList<>();
        private List<MedlemskapDto> medlemskap = new ArrayList<>();
        private List<AdresseDto> adresser = new ArrayList<>();
        private Adressebeskyttelse adressebeskyttelse;
        private boolean erSkjermet;

        Builder() {
            this.uuid = UUID.randomUUID();
        }

        public Builder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder rolle(Rolle rolle) {
            this.rolle = rolle;
            return this;
        }

        public Builder fødselsdato(LocalDate fødselsdato) {
            this.fødselsdato = fødselsdato;
            return this;
        }

        public Builder dødsdato(LocalDate dødsdato) {
            this.dødsdato = dødsdato;
            return this;
        }

        public Builder språk(Språk språk) {
            this.språk = språk;
            return this;
        }

        public Builder kjønn(Kjønn kjønn) {
            this.kjønn = kjønn;
            return this;
        }

        public Builder geografiskTilknytning(GeografiskTilknytningDto geografiskTilknytning) {
            this.geografiskTilknytning = geografiskTilknytning;
            return this;
        }

        public Builder familierelasjoner(List<FamilierelasjonDto> familierelasjoner) {
            this.familierelasjoner = familierelasjoner;
            return this;
        }

        public Builder statsborgerskap(List<StatsborgerskapDto> statsborgerskap) {
            this.statsborgerskap = statsborgerskap;
            return this;
        }

        public Builder sivilstand(List<SivilstandDto> sivilstand) {
            this.sivilstand = sivilstand;
            return this;
        }

        public Builder personstatus(List<PersonstatusDto> personstatus) {
            this.personstatus = personstatus;
            return this;
        }

        public Builder medlemskap(List<MedlemskapDto> medlemskap) {
            this.medlemskap = medlemskap;
            return this;
        }

        public Builder adresser(List<AdresseDto> adresser) {
            this.adresser = adresser;
            return this;
        }

        public Builder adressebeskyttelse(Adressebeskyttelse adressebeskyttelse) {
            this.adressebeskyttelse = adressebeskyttelse;
            return this;
        }

        public Builder erSkjermet(boolean erSkjermet) {
            this.erSkjermet = erSkjermet;
            return this;
        }

        public PersonopplysningerDto build() {
            return new PersonopplysningerDto(this);
        }
    }
}
