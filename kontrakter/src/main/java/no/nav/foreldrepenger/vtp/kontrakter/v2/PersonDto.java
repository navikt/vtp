package no.nav.foreldrepenger.vtp.kontrakter.v2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record PersonDto(UUID id,
                        Rolle rolle,
                        LocalDate fødselsdato,
                        LocalDate dødsdato,
                        String språk,
                        Kjønn kjønn,
                        GeografiskTilknytningDto geografiskTilknytning,
                        List<FamilierelasjonModellDto> familierelasjoner,
                        List<StatsborgerskapDto> statsborgerskap,
                        List<SivilstandDto> sivilstand,
                        List<PersonstatusDto> personstatus,
                        List<MedlemskapDto> medlemskap,
                        List<AdresseDto> adresser,
                        InntektYtelseModellDto inntektytelse) {


    private PersonDto(Builder b) {
        this(b.id, b.rolle, b.fødselsdato, b.dødsdato, b.språk, b.kjønn, b.geografiskTilknytning, b.familierelasjoner,
                b.statsborgerskap, b.sivilstand, b.personstatus, b.medlemskap, b.adresser, b.inntektytelse);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private Rolle rolle;
        private LocalDate fødselsdato;
        private LocalDate dødsdato;
        private String språk;
        private Kjønn kjønn;
        private GeografiskTilknytningDto geografiskTilknytning;
        private List<FamilierelasjonModellDto> familierelasjoner = new ArrayList<>();
        private List<StatsborgerskapDto> statsborgerskap = new ArrayList<>();
        private List<SivilstandDto> sivilstand = new ArrayList<>();
        private List<PersonstatusDto> personstatus = new ArrayList<>();
        private List<MedlemskapDto> medlemskap = new ArrayList<>();
        private List<AdresseDto> adresser = new ArrayList<>();
        private InntektYtelseModellDto inntektytelse;


        Builder() {
            this.id = UUID.randomUUID();
        }

        public UUID id() {
            return id;
        }

        public Rolle rolle() {
            return rolle;
        }

        public LocalDate fødselsdato() {
            return fødselsdato;
        }

        public LocalDate dødsdato() {
            return dødsdato;
        }

        public String språk() {
            return språk;
        }

        public Kjønn kjønn() {
            return kjønn;
        }

        public GeografiskTilknytningDto geografiskTilknytning() {
            return geografiskTilknytning;
        }

        public List<FamilierelasjonModellDto> familierelasjoner() {
            return familierelasjoner;
        }

        public List<StatsborgerskapDto> statsborgerskap() {
            return statsborgerskap;
        }

        public List<SivilstandDto> sivilstand() {
            return sivilstand;
        }

        public List<PersonstatusDto> personstatus() {
            return personstatus;
        }

        public List<MedlemskapDto> medlemskap() {
            return medlemskap;
        }

        public List<AdresseDto> adresser() {
            return adresser;
        }

        public InntektYtelseModellDto inntektytelse() {
            return inntektytelse;
        }

        public Builder id(UUID id) {
            this.id = id;
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

        public Builder språk(String språk) {
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

        public Builder familierelasjoner(List<FamilierelasjonModellDto> familierelasjoner) {
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

        public Builder inntektytelse(InntektYtelseModellDto inntektytelse) {
            this.inntektytelse = inntektytelse;
            return this;
        }

        public PersonDto build() {
            return new PersonDto(this);
        }
    }

}


