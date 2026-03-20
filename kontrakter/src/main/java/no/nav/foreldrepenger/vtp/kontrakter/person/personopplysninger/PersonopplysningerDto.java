package no.nav.foreldrepenger.vtp.kontrakter.person.personopplysninger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.vtp.kontrakter.FødselsnummerGenerator;

public record PersonopplysningerDto(String fnr,
                                    Rolle rolle,
                                    NavnDto navn,
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
                                    AdresserDto adresser,
                                    boolean erSkjermet) {

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(PersonopplysningerDto existing) {
        return new Builder(existing);
    }

    public static class Builder {
        private String fnr;
        private Rolle rolle;
        private NavnDto navn;
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
        private AdresserDto adresser;
        private boolean erSkjermet;

        public Builder() {}

        public Builder(PersonopplysningerDto existing) {
            this.fnr = existing.fnr();
            this.rolle = existing.rolle();
            this.navn = existing.navn();
            this.fødselsdato = existing.fødselsdato();
            this.dødsdato = existing.dødsdato();
            this.språk = existing.språk();
            this.kjønn = existing.kjønn();
            this.geografiskTilknytning = existing.geografiskTilknytning();
            this.familierelasjoner = new ArrayList<>(existing.familierelasjoner());
            this.statsborgerskap = new ArrayList<>(existing.statsborgerskap());
            this.sivilstand = new ArrayList<>(existing.sivilstand());
            this.personstatus = new ArrayList<>(existing.personstatus());
            this.medlemskap = new ArrayList<>(existing.medlemskap());
            this.adresser = existing.adresser();
            this.erSkjermet = existing.erSkjermet();
        }

        public Builder medFnr(String fnr) { this.fnr = fnr; return this; }
        public Builder medRolle(Rolle rolle) { this.rolle = rolle; return this; }
        public Builder medNavn(NavnDto navn) { this.navn = navn; return this; }
        public Builder medNavn(String fornavn, String mellomnavn, String etternavn) { this.navn = new NavnDto(fornavn, mellomnavn, etternavn); return this; }
        public Builder medFødselsdato(LocalDate fødselsdato) { this.fødselsdato = fødselsdato; return this; }
        public Builder medDødsdato(LocalDate dødsdato) { this.dødsdato = dødsdato; return this; }
        public Builder medSpråk(Språk språk) { this.språk = språk; return this; }
        public Builder medKjønn(Kjønn kjønn) { this.kjønn = kjønn; return this; }
        public Builder medGeografiskTilknytning(GeografiskTilknytningDto geografiskTilknytning) { this.geografiskTilknytning = geografiskTilknytning; return this; }
        public Builder medFamilierelasjoner(List<FamilierelasjonDto> familierelasjoner) { this.familierelasjoner = familierelasjoner; return this; }
        public Builder leggTilFamilierelasjon(FamilierelasjonDto familierelasjon) { this.familierelasjoner.add(familierelasjon); return this; }
        public Builder medStatsborgerskap(List<StatsborgerskapDto> statsborgerskap) { this.statsborgerskap = statsborgerskap; return this; }
        public Builder leggTilStatsborgerskap(StatsborgerskapDto statsborgerskap) { this.statsborgerskap.add(statsborgerskap); return this; }
        public Builder medSivilstand(List<SivilstandDto> sivilstand) { this.sivilstand = sivilstand; return this; }
        public Builder leggTilSivilstand(SivilstandDto sivilstand) { this.sivilstand.add(sivilstand); return this; }
        public Builder medPersonstatus(List<PersonstatusDto> personstatus) { this.personstatus = personstatus; return this; }
        public Builder leggTilPersonstatus(PersonstatusDto personstatus) { this.personstatus.add(personstatus); return this; }
        public Builder medMedlemskap(List<MedlemskapDto> medlemskap) { this.medlemskap = medlemskap; return this; }
        public Builder leggTilMedlemskap(MedlemskapDto medlemskap) { this.medlemskap.add(medlemskap); return this; }
        public Builder medAdresser(AdresserDto adresser) { this.adresser = adresser; return this; }
        public Builder medErSkjermet(boolean erSkjermet) { this.erSkjermet = erSkjermet; return this; }

        public PersonopplysningerDto build() {
            if (fnr == null) {
                fnr = new FødselsnummerGenerator.Builder()
                        .kjonn(kjønn)
                        .fodselsdato(fødselsdato)
                        .buildAndGenerate();

            }
            return new PersonopplysningerDto(fnr, rolle, navn, fødselsdato, dødsdato, språk, kjønn,
                    geografiskTilknytning, familierelasjoner, statsborgerskap, sivilstand,
                    personstatus, medlemskap, adresser, erSkjermet);
        }
    }
}
