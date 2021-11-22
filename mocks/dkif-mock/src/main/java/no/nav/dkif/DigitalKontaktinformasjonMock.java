package no.nav.dkif;

import static jakarta.ws.rs.core.HttpHeaders.AUTHORIZATION;

import java.io.IOException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonIndeks;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;

@Api(tags = {"dkif"})
@Path("/api/v1/personer")
public class DigitalKontaktinformasjonMock {

    public static final String HEADER_NAV_PERSONIDENT = "Nav-Personidenter";
    private static final String NAV_CALLID = "Nav-Callid";
    private static final String NAV_CONSUMER_ID = "Nav-Consumer-Id";


    private TestscenarioBuilderRepository scenarioRepository;

    public DigitalKontaktinformasjonMock() {
        try {
            scenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @GET
    @Path("/kontaktinformasjon")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = "Henter kontaktinformasjon for bruker")
    public DigitalKontaktinfoResponsDto hentKontaktinformasjon(@HeaderParam(HEADER_NAV_PERSONIDENT) String fnr,
                                           @HeaderParam(AUTHORIZATION) String authorizationHeader,
                                           @HeaderParam(NAV_CALLID) String navCallid,
                                           @HeaderParam(NAV_CONSUMER_ID) String navConsumerId) {
        DigitalKontaktinfoResponsDto digitalKontaktinfoResponsDto = new DigitalKontaktinfoResponsDto();
        String spraak = hentUtForetrukketSpråkFraBruker(fnr);
        digitalKontaktinfoResponsDto.leggTilSpråk(fnr, spraak);
        return digitalKontaktinfoResponsDto;
    }

    private String hentUtForetrukketSpråkFraBruker(String fnr) {
        PersonIndeks personIndeks = scenarioRepository.getPersonIndeks();
        Personopplysninger personopplysninger = personIndeks.finnPersonopplysningerByIdent(fnr);
        if ((personopplysninger.getSøker() != null && personopplysninger.getSøker().getIdent().equals(fnr))) {
            return personopplysninger.getSøker().getSpråk();
        } else if ((personopplysninger.getAnnenPart() != null && personopplysninger.getAnnenPart().getIdent().equals(fnr))) {
            return personopplysninger.getAnnenPart().getSpråk();
        } else {
            return null;
        }
    }
}
