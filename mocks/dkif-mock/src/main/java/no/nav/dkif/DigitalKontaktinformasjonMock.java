package no.nav.dkif;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonIndeks;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;

@Deprecated(forRemoval = true)
@Tag(name = "dkif")
@Path("/api/v1/personer")
public class DigitalKontaktinformasjonMock {
    private static final Logger LOG = LoggerFactory.getLogger(DigitalKontaktinformasjonMock.class);
    public static final String HEADER_NAV_PERSONIDENT = "Nav-Personidenter";
    private static final String NAV_CALLID = "Nav-Callid";
    private static final String NAV_CONSUMER_ID = "Nav-Consumer-Id";

    private final TestscenarioBuilderRepository scenarioRepository;

    public DigitalKontaktinformasjonMock() {
        scenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
    }

    @GET
    @Path("/kontaktinformasjon")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Henter kontaktinformasjon for bruker")
    public DigitalKontaktinfoResponsDto hentKontaktinformasjon(@HeaderParam(HEADER_NAV_PERSONIDENT) String fnr,
                                           @HeaderParam(AUTHORIZATION) String authorizationHeader,
                                           @HeaderParam(NAV_CALLID) String navCallid,
                                           @HeaderParam(NAV_CONSUMER_ID) String navConsumerId) {
        LOG.warn("Kall på deprekert DKIF endepunkt. Bytt til digdir-krr-proxy");
        DigitalKontaktinfoResponsDto digitalKontaktinfoResponsDto = new DigitalKontaktinfoResponsDto();
        String spraak = hentUtForetrukketSpråkFraBruker(fnr);
        digitalKontaktinfoResponsDto.leggTilSpråk(fnr, spraak);
        return digitalKontaktinfoResponsDto;
    }

    private String hentUtForetrukketSpråkFraBruker(String fnr) {
        PersonIndeks personIndeks = scenarioRepository.getPersonIndeks();
        Personopplysninger personopplysninger = personIndeks.finnPersonopplysningerByIdent(fnr);
        if ((personopplysninger.getSøker() != null && personopplysninger.getSøker().getIdent().equals(fnr))) {
            return personopplysninger.getSøker().getSpråk2Bokstaver();
        } else if ((personopplysninger.getAnnenPart() != null && personopplysninger.getAnnenPart().getIdent().equals(fnr))) {
            return personopplysninger.getAnnenPart().getSpråk2Bokstaver();
        } else {
            return null;
        }
    }
}
