package no.nav.vtp;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;

@Api(tags = {"Utilities"})
@Path("/dummyfile/")
public class DummyRestTjenesteFile {

    private static final Logger LOG = LoggerFactory.getLogger(DummyRestTjenesteFile.class);

    public DummyRestTjenesteFile() {

    }

    @POST
    @Path("/pdf{var:.*}")
    @ApiOperation(value = "post", notes = ("Returnerer en pdf"))
    public Response postpdf() throws IOException {
        LOG.info("Lager en forespørsel på DummyRestTjeneste for pdf");
        InputStream is = getClass().getResourceAsStream("/filer/dummy.pdf");

        return Response.status(Response.Status.OK)
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .entity(is.readAllBytes())
                .build();
    }

    @GET
    @Path("/pdf{var:.*}")
    @ApiOperation(value = "post", notes = ("Returnerer en pdf"))
    public Response getpdf() throws IOException {
        LOG.info("Lager en forespørsel på DummyRestTjeneste for pdf");
        InputStream is = getClass().getResourceAsStream("/filer/dummy.pdf");

        return Response.status(Response.Status.OK)
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .entity(is.readAllBytes())
                .build();
    }
}
