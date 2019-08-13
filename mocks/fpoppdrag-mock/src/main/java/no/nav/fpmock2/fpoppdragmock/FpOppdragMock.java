package no.nav.fpmock2.fpoppdragmock;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Api(tags = {"FpOppdrag"})
@Path("/rest/fpoppdrag/simulering/")
public class FpOppdragMock {

    private static final Logger logg = LoggerFactory.getLogger(FpOppdragMock.class);

    @POST
    @Path("resultat")
    @ApiOperation(value = "Hent resultat av simulering mot økonomi", notes = ("Returnerer simuleringsresultat."))
    public Void hentSimuleringResultat(String behandlingIdDto) {
        logg.info("invoke: hentSimuleringResultat kalles her");
        return null;
    }

    @POST
    @Path("resultat-uten-inntrekk")
    @ApiOperation(value = "Hent detaljert resultat av simulering mot økonomi med og uten inntrekk", notes = ("Returnerer simuleringsresultat."))
    public Void hentSimuleringResultatMedOgUtenInntrekk(String behandlingIdDto) {
        logg.info("invoke: hentSimuleringResultatMedOgUtenInntrekk kalles her");
        return null;
    }

    @POST
    @Path("start")
    @ApiOperation(value = "Start simulering for behandling med oppdrag", notes = "Returnerer status på om oppdrag er gyldig")
    public Response startSimulering(String simulerOppdragDto) {
        logg.info("invoke: startSimulering kalles her");
        return Response.ok().build();
    }

    @POST
    @Path("kanseller")
    @ApiOperation(value = "Kanseller simulering for behandling", notes = "Deaktiverer simuleringgrunnlag for behandling")
    public Response kansellerSimulering(String behandlingIdDto) {
        logg.info("invoke: kansellerSimulering kalles her");
        return Response.ok().build();
    }

    @POST
    @Path("feilutbetalte-perioder")
    @ApiOperation(value = "Hent sum feilutbetaling og simulerte perioder som er feilutbetalte og kan kreves tilbake fra brukeren.", notes = ("Returnerer perioder som er feilutbetalt."))
    public Void hentFeilutbetaltePerioderForTilbakekreving(String behandlingIdDto) {
        logg.info("invoke: hentFeilutbetalePerioderForTilbakekreving kalles her");
        return null;
    }
}