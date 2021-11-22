package no.nav.axsys;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;

@Path("axsys-enhetstilgang/api/v1/tilgang/")
@Produces(MediaType.APPLICATION_JSON)
@Api(tags = {"Axsys/enhetstilgang"})
public class AxsysEnhetstilgangMock {

    private static final Logger LOG = LoggerFactory.getLogger(AxsysEnhetstilgangMock.class);

    @Context
    private TestscenarioBuilderRepository scenarioRepository;

    @SuppressWarnings("unused")
    @GET
    @Path("/{ident}")
    @ApiOperation(value = "Henter enhetstilgang for saksbehandlerident")
    public AxsysTilgangDto hentOrganisasjonAdresse(@PathParam("ident") String ident,
                                                   @Context HttpHeaders httpHeaders,
                                                   @Context UriInfo uriInfo) {
        LOG.info("Axsys/enhetstilgang for ident {}", ident);
        // henter foreldrepengeenhet fra norg og mapper til axsysenhet
        var norgEnhet = scenarioRepository.getEnheterIndeks().finnByDiskresjonskode("NORMAL-FOR");
        if (norgEnhet == null) {
            return null;
        }
        var axsysEnhet = new AxsysEnhetDto(norgEnhet.getEnhetId(), norgEnhet.getNavn(), Set.of("FOR"));
        return new AxsysTilgangDto(List.of(axsysEnhet));
    }

    record AxsysEnhetDto(String enhetId,
                         @JsonProperty("navn") String enhetNavn,
                         @JsonProperty("fagomrader") Set<String> fagområder) {}

    record AxsysTilgangDto(List<AxsysEnhetDto> enheter) {}
}

