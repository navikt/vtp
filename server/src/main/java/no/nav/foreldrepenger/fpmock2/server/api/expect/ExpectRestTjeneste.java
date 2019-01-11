package no.nav.foreldrepenger.fpmock2.server.api.expect;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.fpmock2.felles.ExpectRepository;

@Api(tags = { "Expect" })
@Path("/api/expect")
public class ExpectRestTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(ExpectRestTjeneste.class);
    
    @POST
    @Path("/createExpectation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = ("Oppretter en forventning om at en mock skal bli truffet"), response = ExpectTokenDto.class)
    public ExpectTokenDto createExpectation(ExpectRequestDto expect) throws Exception {
        LOG.info("Ny forventning opprettet for mock " + expect.mock.toString() + " - " + expect.webMethod);
        
        if(expect.mock == null) {
            throw new Exception("Må angi felt 'mock'");
        }
        if(expect.webMethod == null || expect.webMethod.isEmpty()) {
            throw new Exception("Må angi felt 'webMethod'");
        }
        
        ExpectTokenDto response = new ExpectTokenDto(expect.mock, expect.webMethod);
        ExpectRepository.registerToken(expect.mock, expect.webMethod, response.getToken());
        return response;
    }
    
    @POST
    @Path("/checkExpectation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = ("Returnerer om en mock har blidt truffet gitt en token"), response = ExpectResultDto.class)
    public ExpectResultDto checkExpectation(ExpectTokenDto token) throws Exception {
        LOG.info("henter forventningsresultat for token " + token.getToken());
        
        if(token.mock == null) {
            throw new Exception("Må angi felt 'mock'");
        }
        if(token.webMethod == null || token.webMethod.isEmpty()) {
            throw new Exception("Må angi felt 'webMethod'");
        }
        if(token.token == null || token.token.isEmpty()) {
            throw new Exception("Må angi felt 'token'");
        }
        
        boolean result = ExpectRepository.isHit(token.getMock(), token.getWebMethod(), token.getToken());
        ExpectResultDto response = new ExpectResultDto(result);
        return response;
    }
}
