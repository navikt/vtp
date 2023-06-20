package no.nav.foreldrepenger.vtp.server.selftest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/isReady")
public class IsReadyImpl {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String buildPermitResponse() {
        return "OK";
    }
}
