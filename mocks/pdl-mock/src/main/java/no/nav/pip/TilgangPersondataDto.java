package no.nav.pip;

import java.time.LocalDate;
import java.util.List;

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

    public enum Gradering { STRENGT_FORTROLIG_UTLAND, STRENGT_FORTROLIG, FORTROLIG, UGRADERT }

    public enum IdentGruppe { AKTORID, FOLKEREGISTERIDENT, NPID, @JsonEnumDefaultValue UDEFINERT }

    public enum GtType { KOMMUNE, BYDEL, UTLAND, @JsonEnumDefaultValue UDEFINERT }
}
