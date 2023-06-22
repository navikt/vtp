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
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;

@Path("ereg/api/v1/organisasjon")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Enhetsregister Rest")
public class OrganisasjonRSV1Mock {

    private static final Logger LOG = LoggerFactory.getLogger(OrganisasjonRSV1Mock.class);


    @Context
    private TestscenarioBuilderRepository scenarioRepository;


    @SuppressWarnings("unused")
    @GET
    @Path("/{orgnummer}")
    @Operation(description = "Henter adresse informasjon for et organisasjonsnummer")
    @Parameters({
            @Parameter(name = "inkluderHierarki",  in = ParameterIn.QUERY),
            @Parameter(name = "inkluderHistorikk",  in = ParameterIn.QUERY)
    })
    public OrganisasjonResponse hentOrganisasjonAdresse(@PathParam("orgnummer") String orgnummer,
                                                        @Context HttpHeaders httpHeaders,
                                                        @Context UriInfo uriInfo) {
        if (orgnummer != null) {
            LOG.info("EREG REST {}", orgnummer);
            Optional<OrganisasjonModell> organisasjonModell = scenarioRepository.getOrganisasjon(orgnummer);
            if (organisasjonModell.isPresent()) {
                OrganisasjonModell modell = organisasjonModell.get();
                return new OrganisasjonResponse(modell);
            } else {
                return null;
            }
        } else {
            throw new IllegalArgumentException("Orgnummer ikke angitt");
        }
    }

    @SuppressWarnings("unused")
    @GET
    @Path("/{orgnummer}/noekkelinfo")
    @Operation(description = "Henter noekkelinformasjon for et organisasjonsnummer")
    @Parameters({
            @Parameter(name = "inkluderHierarki",  in = ParameterIn.QUERY),
            @Parameter(name = "inkluderHistorikk",  in = ParameterIn.QUERY)
    })
    public OrganisasjonNoekkelinfo hentOrganisasjonNoekkelinfo(@PathParam("orgnummer") String orgnummer,
                                                               @Context HttpHeaders httpHeaders,
                                                               @Context UriInfo uriInfo) {
        if (orgnummer != null) {
            LOG.info("EREG REST noekkelinfo {}", orgnummer);
            Optional<OrganisasjonModell> organisasjonModell = scenarioRepository.getOrganisasjon(orgnummer);
            if (organisasjonModell.isPresent()) {
                OrganisasjonModell modell = organisasjonModell.get();
                return new OrganisasjonNoekkelinfo(modell);
            } else {
                return null;
            }
        } else {
            throw new IllegalArgumentException("Orgnummer ikke angitt");
        }
    }
}

