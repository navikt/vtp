package no.nav.vtp;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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