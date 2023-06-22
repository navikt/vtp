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

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;

@Path("axsys-enhetstilgang/api/v1/tilgang/")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Axsys/enhetstilgang")
public class AxsysEnhetstilgangMock {

    private static final Logger LOG = LoggerFactory.getLogger(AxsysEnhetstilgangMock.class);

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
                         @JsonProperty("navn") String enhetNavn,
                         @JsonProperty("fagomrader") Set<String> fagområder) {}

    record AxsysTilgangDto(List<AxsysEnhetDto> enheter) {}
}

