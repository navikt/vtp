package no.nav.tjeneste.virksomhet.organisasjon.rs;

import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;

@Path("ereg/api/v1/organisasjon")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(tags = {"Enhetsregister Rest"})
public class OrganisasjonRSV1Mock {

    private static final Logger LOG = LoggerFactory.getLogger(OrganisasjonRSV1Mock.class);


    @Context
    private TestscenarioBuilderRepository scenarioRepository;

    @SuppressWarnings("unused")
    @GET
    @Path("/{orgnummer}")
    @ApiOperation(value = "Henter informasjon for et organisasjonsnummer")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "inkluderHierarki", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "inkluderHistorikk", dataType = "string", paramType = "query")
    })
    public OrganisasjonJson hentOrganisasjon(@PathParam("orgnummer") String orgnummer,
                                     @Context HttpHeaders httpHeaders,
                                     @Context UriInfo uriInfo) {
        if (orgnummer != null) {
            LOG.info("EREG REST {}", orgnummer);
            Optional<OrganisasjonModell> organisasjonModell = scenarioRepository.getOrganisasjon(orgnummer);
            if (organisasjonModell.isPresent()) {
                OrganisasjonModell modell = organisasjonModell.get();
                OrganisasjonJson organisasjon = new OrganisasjonJson(modell);
                return organisasjon;
            } else {
                return null;
            }
        } else {
            throw new IllegalArgumentException("Orgnummer ikke angitt");
        }
    }

    @SuppressWarnings("unused")
    @GET
    @Path("/{orgnummer}")
    @ApiOperation(value = "Henter adresse informasjon for et organisasjonsnummer")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "inkluderHierarki", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "inkluderHistorikk", dataType = "string", paramType = "query")
    })
    public OrganisasjonAdresse hentOrganisasjonAdresse(@PathParam("orgnummer") String orgnummer,
                                     @Context HttpHeaders httpHeaders,
                                     @Context UriInfo uriInfo) {
        if (orgnummer != null) {
            LOG.info("EREG REST {}", orgnummer);
            Optional<OrganisasjonModell> organisasjonModell = scenarioRepository.getOrganisasjon(orgnummer);
            if (organisasjonModell.isPresent()) {
                OrganisasjonModell modell = organisasjonModell.get();
                OrganisasjonAdresse adresse = new OrganisasjonAdresse(modell);
                return adresse;
            } else {
                return null;
            }
        } else {
            throw new IllegalArgumentException("Orgnummer ikke angitt");
        }
    }
}

