package no.nav.pip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neovisionaries.i18n.CountryCode;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import no.nav.vtp.person.Person;
import no.nav.vtp.person.PersonRepository;
import no.nav.vtp.person.ident.PersonIdent;
import no.nav.vtp.person.personopplysninger.Adresser;
import no.nav.vtp.person.personopplysninger.Familierelasjon;
import no.nav.vtp.person.personopplysninger.GeografiskTilknytning;


@Path("/api/pdl-pip-api")
public class PdlPipMock {
    private static final String IDENT_HEADER_NAME = "ident";

    @GET
    @Path("/api/v1/person")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public TilgangPersondataDto personData(@HeaderParam(IDENT_HEADER_NAME) String ident) {
        return tilTilgangPersondataDto(ident);
    }

    @POST
    @Path("/api/v1/personBolk")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String, TilgangPersondataDto> persondataBolk(List<String> identer) {
        var persondataMap = new HashMap<String , TilgangPersondataDto>();
        for (var ident : identer) {
            var tilgangPersondataDto = tilTilgangPersondataDto(ident);
            persondataMap.put(ident, tilgangPersondataDto);
        }
        return persondataMap;
    }

    private TilgangPersondataDto tilTilgangPersondataDto(String ident) {
        var person = PersonRepository.hentPerson(ident);
        if (person == null) {
            return null;
        }
        return new TilgangPersondataDto(
                ((PersonIdent) person.personopplysninger().identifikator()).aktørId(),
                tilPerson(person),
                tilIdenter(person),
                tilGeografiskTilknytning(person)
        );
    }

    private static TilgangPersondataDto.GeografiskTilknytning tilGeografiskTilknytning(Person person) {
        var geografiskTilknytning = person.personopplysninger().geografiskTilknytning();
        if (geografiskTilknytning == null) {
            return null;
        }

        return new TilgangPersondataDto.GeografiskTilknytning(
                tilGtType(geografiskTilknytning),
                null,
                null,
                geografiskTilknytning.land().getAlpha3(),
                null
        );
    }

    private static TilgangPersondataDto.GtType tilGtType(GeografiskTilknytning geografiskTilknytning) {


        if (!CountryCode.NO.equals(geografiskTilknytning.land())) {
            return TilgangPersondataDto.GtType.UTLAND;
        }
        return switch (geografiskTilknytning.type()) {
            case BYDEL -> TilgangPersondataDto.GtType.BYDEL;
            case KOMMUNE -> TilgangPersondataDto.GtType.KOMMUNE;
            case LAND -> TilgangPersondataDto.GtType.UDEFINERT;
        };
    }

    private static TilgangPersondataDto.Identer tilIdenter(Person person) {
        var identifikator = person.personopplysninger().identifikator();
        return new TilgangPersondataDto.Identer(List.of(
                new TilgangPersondataDto.Ident(identifikator.value(), false, TilgangPersondataDto.IdentGruppe.FOLKEREGISTERIDENT),
                new TilgangPersondataDto.Ident(((PersonIdent) identifikator).aktørId(), false, TilgangPersondataDto.IdentGruppe.AKTORID)
        ));
    }

    private static TilgangPersondataDto.Person tilPerson(Person person) {
        return new TilgangPersondataDto.Person(
                List.of(tilAdressebeskyttesle(person.personopplysninger().adresser())),
                List.of(tilFødsel(person)),
                tilDødsfall(person),
                tilFamilierelasjoner(person)
        );
    }

    private static List<TilgangPersondataDto.Familierelasjoner> tilFamilierelasjoner(Person person) {
        return person.personopplysninger().familierelasjoner().stream()
                .map(PdlPipMock::tilFamilierelasjon)
                .toList();
    }

    private static TilgangPersondataDto.Familierelasjoner tilFamilierelasjon(Familierelasjon familierelasjon) {
        return new TilgangPersondataDto.Familierelasjoner(familierelasjon.relatertTilId().fnr());
    }

    private static List<TilgangPersondataDto.Dødsfall> tilDødsfall(Person person) {
        if (person.personopplysninger().dødsdato() == null) {
            return List.of();
        }
        return List.of(new TilgangPersondataDto.Dødsfall(person.personopplysninger().dødsdato()));
    }

    private static TilgangPersondataDto.Fødsel tilFødsel(Person person) {
        return new TilgangPersondataDto.Fødsel(person.personopplysninger().fødselsdato());
    }

    private static TilgangPersondataDto.Adressebeskyttelse tilAdressebeskyttesle(Adresser adresser) {
        return new TilgangPersondataDto.Adressebeskyttelse(tilGradering(adresser));
    }

    private static TilgangPersondataDto.Gradering tilGradering(Adresser adresser) {
        if (adresser == null || adresser.adressebeskyttelse() == null) {
            return TilgangPersondataDto.Gradering.UGRADERT;
        }
        return switch (adresser.adressebeskyttelse()) {
            case FORTROLIG -> TilgangPersondataDto.Gradering.FORTROLIG;
            case STRENGT_FORTROLIG -> TilgangPersondataDto.Gradering.STRENGT_FORTROLIG;
            case UGRADERT -> TilgangPersondataDto.Gradering.UGRADERT;
        };
    }
}
