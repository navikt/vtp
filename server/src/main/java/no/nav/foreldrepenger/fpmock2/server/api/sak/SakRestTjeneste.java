package no.nav.foreldrepenger.fpmock2.server.api.sak;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.fpmock2.testmodell.personopplysning.SøkerModell;
import no.nav.tjeneste.virksomhet.sak.v1.GsakRepo;
import no.nav.tjeneste.virksomhet.sak.v1.informasjon.Sak;

@Api(tags = "Gsak repository")
@Path("/api/sak")
public class SakRestTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(SakRestTjeneste.class);


    @Context
    private GsakRepo gsakRepo;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = ("Lager nytt saksnummer fra sekvens"), response = OpprettSakResponseDTO.class)
    public Response foreldrepengesoknadErketype(OpprettSakRequestDTO requestDTO){

        if(requestDTO.getLokalIdent() == null || requestDTO.getLokalIdent().size() < 1 || requestDTO.getFagområde() == null ||
                requestDTO.getFagsystem() == null || requestDTO.getSakstype() == null){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("Request mangler påkrevde verdier")
                    .build();
        }

        List<PersonModell> brukere = requestDTO.getLokalIdent().stream().map(p ->
                new SøkerModell(p, "place holder", null, null)).collect(Collectors.toList());

        Sak sak = gsakRepo.leggTilSak(brukere, requestDTO.getFagområde(), requestDTO.getFagsystem(), requestDTO.getSakstype());

        LOG.info("Opprettet sak med saksnummer: {}", sak.getSakId());

        return Response
                .status(Response.Status.OK)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new OpprettSakResponseDTO(sak.getSakId()))
                .build();

    }

}
