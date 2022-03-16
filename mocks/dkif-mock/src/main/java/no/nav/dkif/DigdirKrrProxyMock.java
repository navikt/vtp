package no.nav.dkif;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

import java.io.IOException;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;

@Api(tags = {"digdir-krr-proxy"})
@Path("/digdir")
public class DigdirKrrProxyMock {

    public static final String HEADER_NAV_PERSONIDENT = "Nav-Personident";

    private TestscenarioBuilderRepository scenarioRepository;

    public DigdirKrrProxyMock() {
        try {
            scenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @GET
    @Path("/v1/person")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = "Henter kontaktinformasjon for person")
    public Response hentKontaktinformasjon(@HeaderParam(HEADER_NAV_PERSONIDENT) @NotNull String fnr,
                                           @HeaderParam(AUTHORIZATION) String authorizationHeader) {
        var spraak = hentUtForetrukketSpråkFraBruker(fnr);
        if (spraak == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        var kontaktinformasjon = new Kontaktinformasjon();
        kontaktinformasjon.setSpraak(spraak);
        return Response.ok(kontaktinformasjon).build();
    }

    public String hentUtForetrukketSpråkFraBruker(String fnr) {
        var personIndeks = scenarioRepository.getPersonIndeks();
        var personopplysninger = personIndeks.finnPersonopplysningerByIdent(fnr);

        if (personopplysninger == null) {
            return null;
        }
        if (personopplysninger.getSøker() != null && personopplysninger.getSøker().getIdent().equals(fnr)) {
            return personopplysninger.getSøker().getSpråk2Bokstaver();
        } else if (personopplysninger.getAnnenPart() != null && personopplysninger.getAnnenPart().getIdent().equals(fnr)) {
            return personopplysninger.getAnnenPart().getSpråk2Bokstaver();
        } else {
            return null;
        }
    }
}
