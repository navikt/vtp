package no.nav.okonomi.tilbakekrevingservice;

import java.util.Random;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import no.nav.foreldrepenger.vtp.kontrakter.TilbakekrevingKonsistensDto;

/**
 * Tjenesten tar vare på siste saksnummer og henvisning (behandlingId) fra Autotest,
 * slik at de kan sendes tilbake i genererte kravgrunnlag som da vil være konsistente med siste kjente sak.
 */
@Api(tags = {"Tilbakekreving"})
@Path("/api/tilbakekreving")
public class TilbakekrevingKonsistensTjeneste {

    private static int sisteSaksnummer = genererTilfeldigSaksnummer();
    private static String sisteHenvisning = "1";

    @POST
    @Path("/konsistens")
    @ApiOperation(value = "Sørger for at kravgrunnlag som returneres av mock har riktig saksnummer og henvisning")
    public Response oppdaterKonsistens(TilbakekrevingKonsistensDto request) {
        TilbakekrevingKonsistensTjeneste.sisteSaksnummer = Integer.parseInt(request.saksnummer());
        TilbakekrevingKonsistensTjeneste.sisteHenvisning = request.behandlingId();
        return Response.status(200).build();
    }

    public static int getSisteSaksnummer() {
        return sisteSaksnummer;
    }

    public static String getSisteHenvisning() {
        return sisteHenvisning;
    }

    private static int genererTilfeldigSaksnummer() {
        int m = (int) Math.pow(10, 10 - 1);
        return m + new Random().nextInt(9 * m);
    }
}
