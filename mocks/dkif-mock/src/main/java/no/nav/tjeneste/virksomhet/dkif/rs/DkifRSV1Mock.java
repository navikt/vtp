package no.nav.tjeneste.virksomhet.dkif.rs;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;

@Path("dkif/api/v1/personer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(tags = {"DKIF Rest"})
public class DkifRSV1Mock {

    private static final Logger LOG = LoggerFactory.getLogger(DkifRSV1Mock.class);
    private static final String HEADER_NAV_PERSONIDENT = "Nav-Personidenter";
    private static final String QPRM_SDP = "inkluderSikkerDigitalPost";

    @Context
    private TestscenarioBuilderRepository scenarioRepository;


    @SuppressWarnings("unused")
    @GET
    @Path("/kontaktinformasjon")
    @ApiOperation(value = "Henter digital kontaktinfo for en (liste av) person")
    @ApiImplicitParams({
            @ApiImplicitParam(name = HEADER_NAV_PERSONIDENT, required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = QPRM_SDP, dataType = "string", paramType = "query")
    })
    public DigitalKontaktinfo hentArbeidsforholdFor(@Context HttpHeaders httpHeaders, @Context UriInfo uriInfo) {
        String ident = httpHeaders.getHeaderString(HEADER_NAV_PERSONIDENT); // Det kan komme en liste her - vurder split

        LOG.info("DKIF REST ident {}", ident);
        return new DigitalKontaktinfo(ident);
    }

}

