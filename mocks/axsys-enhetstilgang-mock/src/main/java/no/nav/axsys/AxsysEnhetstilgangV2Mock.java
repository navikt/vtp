package no.nav.axsys;

import java.util.List;
import java.util.Set;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;

@Path("axsys-enhetstilgang/api/v2/tilgang/")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Axsys/enhetstilgang")
public class AxsysEnhetstilgangV2Mock {

    private static final Logger LOG = LoggerFactory.getLogger(AxsysEnhetstilgangV2Mock.class);

    @Context
    private TestscenarioBuilderRepository scenarioRepository;

    @SuppressWarnings("unused")
    @GET
    @Path("/{ident}")
    @Operation(description = "Henter enhetstilgang for saksbehandlerident")
    public AxsysTilgangDto hentOrganisasjonAdresse(@PathParam("ident") String ident,
                                                   @Context HttpHeaders httpHeaders,
                                                   @Context UriInfo uriInfo) {
        LOG.info("Axsys/enhetstilgang for ident {}", ident);
        // henter foreldrepengeenhet fra norg og mapper til axsysenhet
        var norgEnhet = scenarioRepository.getEnheterIndeks().finnByDiskresjonskode("NORMAL-FOR");
        if (norgEnhet == null) {
            return null;
        }
        var axsysEnhet = new AxsysEnhetDto(norgEnhet.enhetId(), norgEnhet.navn(), Set.of("FOR"));
        return new AxsysTilgangDto(List.of(axsysEnhet));
    }

    record AxsysEnhetDto(String enhetId,
                         String navn,
                         Set<String> temaer) {}

    record AxsysTilgangDto(List<AxsysEnhetDto> enheter) {}
}

