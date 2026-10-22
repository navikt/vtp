package no.nav.foreldrepenger.vtp.server.api.hendelser;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import no.nav.foreldrepenger.vtp.kontrakter.tilbakekreving.TilbakekrevingKonsistensDto;
import no.nav.vtp.tilbake.TilbakekrevingSaksnummer;

/**
 * Tjenesten tar vare på siste saksnummer og henvisning (behandlingId) fra Autotest,
 * slik at de kan sendes tilbake i genererte kravgrunnlag som da vil være konsistente med siste kjente sak.
 */
@Path("/api/tilbakekreving")
public class TilbakekrevingKonsistensTjeneste {


    @POST
    @Path("/konsistens")
    // Sørger for at kravgrunnlag som returneres av mock har riktig saksnummer og henvisning
    public static Response oppdaterKonsistens(TilbakekrevingKonsistensDto request) {
        TilbakekrevingSaksnummer.oppdaterSisteSaksnummer(request);
        return Response.status(200).build();
    }

}
