package no.nav.digdir;

import static jakarta.ws.rs.core.HttpHeaders.AUTHORIZATION;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;

@Tag(name = "digdir-krr-proxy")
@Path("/digdir")
public class DigdirKrrProxyMock {

    public static final String HEADER_NAV_PERSONIDENT = "Nav-Personident";

    private final TestscenarioBuilderRepository scenarioRepository;

    public DigdirKrrProxyMock() {
        scenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
    }

    @GET
    @Path("/rest/v1/person")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Henter kontaktinformasjon for person")
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
