package no.nav.pip;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public record TilgangPersondataDto(String aktoerId, Person person, Identer identer, GeografiskTilknytning geografiskTilknytning) {

    public record Person(List<Adressebeskyttelse> adressebeskyttelse, List<Fødsel> foedsel,
                         List<Dødsfall> doedsfall, List<Familierelasjoner> familierelasjoner) {
    }

    public record Adressebeskyttelse(Gradering gradering) { }

    public record Fødsel(LocalDate foedselsdato) { }

    public record Dødsfall(LocalDate doedsdato) { }

    public record Familierelasjoner(String relatertPersonsIdent) { } // FNR

    public record Identer(List<Ident> identer) { }

    public record Ident(String ident, Boolean historisk, IdentGruppe gruppe) { }

    public record GeografiskTilknytning(GtType gtType, String gtKommune, String gtBydel, String gtLand, String regel) { }

    public enum Gradering { STRENGT_FORTROLIG_UTLAND, STRENGT_FORTROLIG, FORTROLIG, @JsonEnumDefaultValue UDEFINERT }

    public enum IdentGruppe { AKTORID, FOLKEREGISTERIDENT, NPID, @JsonEnumDefaultValue UDEFINERT }

    public enum GtType { KOMMUNE, BYDEL, UTLAND, @JsonEnumDefaultValue UDEFINERT }

    public boolean harStrengAdresseBeskyttelse() {
        return Optional.ofNullable(person()).map(Person::adressebeskyttelse).orElse(List.of()).stream()
            .map(Adressebeskyttelse::gradering)
            .anyMatch(g -> Gradering.STRENGT_FORTROLIG.equals(g) || Gradering.STRENGT_FORTROLIG_UTLAND.equals(g));
    }

    public boolean harAdresseBeskyttelse() {
        return Optional.ofNullable(person()).map(Person::adressebeskyttelse).orElse(List.of()).stream()
            .map(Adressebeskyttelse::gradering)
            .anyMatch(g -> g != null && !Gradering.UDEFINERT.equals(g));
    }

    public boolean erIkkeMyndig() {
        return Optional.ofNullable(person()).map(Person::foedsel).orElse(List.of()).stream()
            .map(Fødsel::foedselsdato)
            .anyMatch(f -> f == null || f.plusYears(18).isAfter(LocalDate.now()));
    }

    public String personIdent() {
        return Optional.ofNullable(identer()).map(Identer::identer).orElse(List.of()).stream()
            .filter(i -> IdentGruppe.FOLKEREGISTERIDENT.equals(i.gruppe()))
            .filter(i -> !i.historisk())
            .map(Ident::ident)
            .findFirst().orElse(null);
    }

    public List<String> personIdenter() {
        return Optional.ofNullable(identer()).map(Identer::identer).orElse(List.of()).stream()
            .filter(i -> IdentGruppe.FOLKEREGISTERIDENT.equals(i.gruppe()))
            .map(Ident::ident)
            .toList();
    }

    public String aktørId() {
        return Optional.ofNullable(identer()).map(Identer::identer).orElse(List.of()).stream()
            .filter(i -> IdentGruppe.AKTORID.equals(i.gruppe()))
            .filter(i -> !i.historisk())
            .map(Ident::ident)
            .findFirst().orElse(null);
    }

    public List<String> aktørIdMedHistoriske() {
        return Optional.ofNullable(identer()).map(Identer::identer).orElse(List.of()).stream()
            .filter(i -> IdentGruppe.AKTORID.equals(i.gruppe()))
            .map(Ident::ident)
            .toList();
    }

    public boolean harNasjonalTilknytning() {
        return Optional.ofNullable(geografiskTilknytning()).map(GeografiskTilknytning::gtType)
            .filter(gtt -> GtType.KOMMUNE.equals(gtt) || GtType.BYDEL.equals(gtt))
            .isPresent();
    }

    public boolean harIkkeNasjonalTilknytning() {
        return Optional.ofNullable(geografiskTilknytning()).map(GeografiskTilknytning::gtType)
            .filter(gtt -> GtType.KOMMUNE.equals(gtt) || GtType.BYDEL.equals(gtt))
            .isEmpty();
    }

}
