package no.nav.okonomi.tilbakekrevingservice;

import java.util.Random;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.foreldrepenger.vtp.kontrakter.TilbakekrevingKonsistensDto;

/**
 * Tjenesten tar vare på siste saksnummer og henvisning (behandlingId) fra Autotest,
 * slik at de kan sendes tilbake i genererte kravgrunnlag som da vil være konsistente med siste kjente sak.
 */
// TODO: Flytt over til fpwsproxy-mock/tilbakekreving etter sannering
@Tag(name = "Tilbakekreving")
@Path("/api/tilbakekreving")
public class TilbakekrevingKonsistensTjeneste {

    private static final Random RANDOM =  new Random();

    private static int sisteSaksnummer = genererTilfeldigSaksnummer();
    private static String sisteHenvisning = "1";

    @POST
    @Path("/konsistens")
    @Operation(description = "Sørger for at kravgrunnlag som returneres av mock har riktig saksnummer og henvisning")
    public static Response oppdaterKonsistens(TilbakekrevingKonsistensDto request) {
        sisteSaksnummer = Integer.parseInt(request.saksnummer());
        sisteHenvisning = request.behandlingId();
        return Response.status(200).build();
    }

    public static int getSisteSaksnummer() {
        return sisteSaksnummer;
    }

    public static String getSisteHenvisning() {
        return sisteHenvisning;
    }

    private static int genererTilfeldigSaksnummer() {
        var m = (int) Math.pow(10, 9);
        return m + RANDOM.nextInt(9 * m);
    }
}
