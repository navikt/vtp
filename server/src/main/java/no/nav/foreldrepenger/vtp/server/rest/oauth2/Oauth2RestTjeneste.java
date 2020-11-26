package no.nav.foreldrepenger.vtp.server.rest.oauth2;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.security.mock.oauth2.MockOAuth2Server;

@Api(tags = { "oauth2" })
@Path("/api/oauth2")
public class Oauth2RestTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(Oauth2RestTjeneste.class);
    private static final String ISSUER_ID = "vtp";
    private static final String FNR = "fnr";
    private static MockOAuth2Server server;

    public Oauth2RestTjeneste() {
        server = Oauth2Singleton.getInstance();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "issueToken", notes = ("Issuer token for angitt fnr"))
    public String henterTokenForFnr(@QueryParam(FNR) String fnr) {
        LOG.info("Henter gyldig token for søker med fnr: [{}]", fnr);
        return server.issueToken(ISSUER_ID, fnr).serialize();
    }
}
