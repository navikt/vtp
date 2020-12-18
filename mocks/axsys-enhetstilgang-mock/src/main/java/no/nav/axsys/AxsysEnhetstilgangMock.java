package no.nav.axsys;

import java.util.List;
import java.util.Set;

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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    public Enhetstilgang hentOrganisasjonAdresse(@PathParam("ident") String ident,
                                                 @Context HttpHeaders httpHeaders,
                                                 @Context UriInfo uriInfo) {
        LOG.info("Axsys/enhetstilgang for ident {}", ident);

        // Vi henter en foreldrepengerenhet som mappes til axsysrespons.
        // Brukes pt kun til visning i fplos.
        var norgEnhet = scenarioRepository.getEnheterIndeks().finnByDiskresjonskode("NORMAL-FOR");
        if (norgEnhet == null) {
            return null;
        }
        var axsysEnhet = new OrganisasjonsEnhet(norgEnhet.getEnhetId(), norgEnhet.getNavn(), Set.of("FOR"));
        return new Enhetstilgang(List.of(axsysEnhet));
    }
}

