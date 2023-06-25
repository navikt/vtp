package no.nav.foreldrepenger.vtp.server.selftest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/isAlive")
public class IsAliveImpl {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String buildPermitResponse() {
        return "OK";
    }
}
