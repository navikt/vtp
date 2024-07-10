package no.nav.pip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.FamilierelasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.GeografiskTilknytningModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;


@Tag(name = "pdl")
@Path("/api/pdl-pip-api")
public class PdlPipMock {
    private static final String IDENT_HEADER_NAME = "ident";

    private final TestscenarioBuilderRepository scenarioRepository;

    public PdlPipMock(@Context TestscenarioBuilderRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }


    @GET
    @Path("/api/v1/person")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Enkelt oppslag pdl pip ")
    public TilgangPersondataDto personData(@HeaderParam(IDENT_HEADER_NAME) String ident) {
        return tilTilgangPersondataDto(ident);
    }

    @POST
    @Path("/api/v1/personBolk")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Enkelt oppslag pdl pip ")
    public Map<String, TilgangPersondataDto> persondataBolk(List<String> identer) {
        var persondataMap = new HashMap<String , TilgangPersondataDto>();
        for (var ident : identer) {
            var tilgangPersondataDto = tilTilgangPersondataDto(ident);
            persondataMap.put(ident, tilgangPersondataDto);
        }
        return persondataMap;
    }

    private TilgangPersondataDto tilTilgangPersondataDto(String ident) {
        var personModell = (PersonModell) scenarioRepository.getPersonIndeks().finnByIdent(ident);
        var personopplysnigner = scenarioRepository.getPersonIndeks().finnPersonopplysningerByIdent(ident);
        if (personModell == null || personopplysnigner == null) {
            return null;
        }
        return new TilgangPersondataDto(
                personModell.getAktørIdent(),
                tilPerson(personModell, personopplysnigner),
                tilIdenter(personModell),
                tilGeografiskTilknytning(personModell)
        );
    }

    private static TilgangPersondataDto.GeografiskTilknytning tilGeografiskTilknytning(PersonModell person) {
        var geografiskTilknytning = person.getGeografiskTilknytning();
        if (geografiskTilknytning == null) {
            return null;
        }

        return new TilgangPersondataDto.GeografiskTilknytning(
                tilGtType(geografiskTilknytning),
                null,
                null,
                geografiskTilknytning.getKode(),
                null
        );
    }

    private static TilgangPersondataDto.GtType tilGtType(GeografiskTilknytningModell geografiskTilknytning) {
        if (!"NOR".equalsIgnoreCase(geografiskTilknytning.getKode())) return TilgangPersondataDto.GtType.UTLAND;
        return switch (geografiskTilknytning.getGeografiskTilknytningType()) {
            case Bydel -> TilgangPersondataDto.GtType.BYDEL;
            case Kommune -> TilgangPersondataDto.GtType.KOMMUNE;
            case Land -> TilgangPersondataDto.GtType.UDEFINERT;
        };
    }

    private static TilgangPersondataDto.Identer tilIdenter(PersonModell person) {
        return new TilgangPersondataDto.Identer(List.of(
                new TilgangPersondataDto.Ident(person.getIdent(), false, TilgangPersondataDto.IdentGruppe.FOLKEREGISTERIDENT),
                new TilgangPersondataDto.Ident(person.getAktørIdent(), false, TilgangPersondataDto.IdentGruppe.AKTORID)
        ));
    }

    private static TilgangPersondataDto.Person tilPerson(PersonModell personModell, Personopplysninger personopplysnigner) {
        return new TilgangPersondataDto.Person(
                List.of(tilAdressebeskyttesle(personModell)),
                List.of(tilFødsel(personModell)),
                tilDødsfall(personModell),
                tilFamilierelasjoner(personopplysnigner)
        );
    }

    private static List<TilgangPersondataDto.Familierelasjoner> tilFamilierelasjoner(Personopplysninger personopplysnigner) {
        return personopplysnigner.getFamilierelasjoner().stream()
                .map(PdlPipMock::tilFamilierelasjon)
                .toList();
    }

    private static TilgangPersondataDto.Familierelasjoner tilFamilierelasjon(FamilierelasjonModell familierelasjonModell) {
        return new TilgangPersondataDto.Familierelasjoner(familierelasjonModell.getTil().getIdent());
    }

    private static List<TilgangPersondataDto.Dødsfall> tilDødsfall(PersonModell personModell) {
        if (personModell.getDødsdato() == null) {
            return List.of();
        }
        return List.of(new TilgangPersondataDto.Dødsfall(personModell.getDødsdato()));
    }

    private static TilgangPersondataDto.Fødsel tilFødsel(PersonModell personModell) {
        return new TilgangPersondataDto.Fødsel(personModell.getFødselsdato());
    }

    private static TilgangPersondataDto.Adressebeskyttelse tilAdressebeskyttesle(PersonModell personModell) {
        return new TilgangPersondataDto.Adressebeskyttelse(tilGradering(personModell));
    }

    private static TilgangPersondataDto.Gradering tilGradering(PersonModell personModell) {
        var diskresjonskodeType = personModell.getDiskresjonskodeType();
        if (diskresjonskodeType == null) {
            return TilgangPersondataDto.Gradering.UDEFINERT;
        }
        return switch (diskresjonskodeType) {
            case SPSF -> TilgangPersondataDto.Gradering.STRENGT_FORTROLIG;
            case SPFO -> TilgangPersondataDto.Gradering.FORTROLIG;
            default ->  TilgangPersondataDto.Gradering.UDEFINERT;
        };
    }
}
