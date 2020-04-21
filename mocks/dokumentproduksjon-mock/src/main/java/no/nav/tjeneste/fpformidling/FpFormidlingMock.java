package no.nav.tjeneste.fpformidling;

import java.util.*;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.tjeneste.fpformidling.dto.*;

@Api("/fpformidling")
@Path("/fpformidling")
public class FpFormidlingMock {

    private final Map<UUID, List<String>> dokumentProduksjon = new HashMap<>();
    private final Map<UUID, TekstFraSaksbehandlerDto> saksbehandlerTekst = new HashMap<>();

    public FpFormidlingMock() {
    }

    @SuppressWarnings("unused")
    @POST
    @Path("/hent-dokumentmaler")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "HentDokumentmalListe", notes = ("Returnerer tilgjengelige dokumentmaler"))
    public Response hentDokumentmalListe(BehandlingUuidDto request) {
        return Response.ok(new HentBrevmalerDto(Collections.emptyList())).build();
    }

    @SuppressWarnings("unused")
    @POST
    @Path("brev/maler")
    @Produces("application/json")
    public HentBrevmalerDto hentBrevmaler(BehandlingUuidDto uuidDto) {
        return new HentBrevmalerDto(Collections.emptyList());
    }

    @POST
    @Path("brev/dokument-sendt")
    @Produces("application/json")
    public Boolean erDokumentSendt(DokumentProdusertDto request) {
        return dokumentProduksjon.getOrDefault(request.getBehandlingUuid(), List.of()).contains(request.getDokumentMal());
    }

    @POST
    @Path("brev/bestill")
    @Produces("application/json")
    public void bestillDokument(DokumentbestillingDto request) {
        dokumentProduksjon.putIfAbsent(request.getBehandlingUuid(), new ArrayList<>());
        dokumentProduksjon.get(request.getBehandlingUuid()).add(request.getDokumentMal());
    }

    @POST
    @Path("saksbehandlertekst/hent")
    @Produces("application/json")
    public TekstFraSaksbehandlerDto hentSaksbehandlersTekst(BehandlingUuidDto uuidDto) {
        return saksbehandlerTekst.getOrDefault(uuidDto.getBehandlingUuid(), null);
    }

    @POST
    @Path("saksbehandlertekst/lagre")
    @Produces("application/json")
    public void lagreSaksbehandlersTekst(TekstFraSaksbehandlerDto request) {
        saksbehandlerTekst.put(request.getBehandlingUuid(), request);
    }

}
