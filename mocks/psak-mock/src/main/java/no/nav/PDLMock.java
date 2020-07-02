package no.nav;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * VTP-teamet jobber med å lage mock for PDL, og dette er midlertidig løsning frem til vi merger inn i vtp-en
 */
@Api(tags = {"PDL"})
@Path("/graphql")
public class PDLMock {
    private static final Logger LOG = LoggerFactory.getLogger(PDLMock.class);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
        public Map<String, List<String>> hentFullPerson(String data){
        LOG.info("Nytt kall mot graphQL med data: " + data);
        return Map.of("errors", Collections.singletonList("Ikke implementert"));
    }
}
