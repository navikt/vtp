package no.nav.foreldrepenger.fpwsproxy.oppdrag;

import static no.nav.foreldrepenger.fpwsproxy.oppdrag.OppdragskontrollTilBeregingMapper.tilBeregningDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.simulering.request.OppdragskontrollDto;

@Path("/api/fpwsproxy/simulering")
public class FpWsProxySimuleringOppdragMock {
    private static final Logger LOG = LoggerFactory.getLogger(FpWsProxySimuleringOppdragMock.class);

    @POST
    @Path("/start")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response simulerBereging(@Valid OppdragskontrollDto oppdragskontrollDto,
                                    @QueryParam("uten_inntrekk") @DefaultValue("false") boolean utenInntrekk,
                                    @QueryParam("ytelse_type") YtelseType ytelseType) {
        var tekstMedUtenInntrekk = utenInntrekk ? "uten inntrekk for" + ytelseType : "med inntrekk";
        LOG.info("Utfører simulering {} for følgende oppdrag {}", tekstMedUtenInntrekk, oppdragskontrollDto.behandlingId());
        var simuleringsresultat = tilBeregningDto(oppdragskontrollDto, utenInntrekk, ytelseType);
        LOG.info("Simulering utført for {}", oppdragskontrollDto.behandlingId());
        return Response.ok(simuleringsresultat).build();
    }

}
