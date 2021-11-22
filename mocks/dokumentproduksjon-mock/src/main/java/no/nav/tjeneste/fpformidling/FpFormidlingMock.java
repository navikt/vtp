package no.nav.tjeneste.fpformidling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import no.nav.tjeneste.fpformidling.dto.BehandlingUuidDto;
import no.nav.tjeneste.fpformidling.dto.DokumentProdusertDto;
import no.nav.tjeneste.fpformidling.dto.DokumentbestillingDto;
import no.nav.tjeneste.fpformidling.dto.HentBrevmalerDto;
import no.nav.tjeneste.fpformidling.dto.TekstFraSaksbehandlerDto;

@Api("/fpformidling")
@Path("/fpformidling")
public class FpFormidlingMock {

    private final Map<UUID, List<String>> dokumentProduksjon = new ConcurrentHashMap<>();
    private final Map<UUID, TekstFraSaksbehandlerDto> saksbehandlerTekst = new ConcurrentHashMap<>();

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
    @Produces(MediaType.APPLICATION_JSON)
    public HentBrevmalerDto hentBrevmaler(BehandlingUuidDto uuidDto) {
        return new HentBrevmalerDto(Collections.emptyList());
    }

    @POST
    @Path("brev/dokument-sendt")
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean erDokumentSendt(DokumentProdusertDto request) {
        return dokumentProduksjon.getOrDefault(request.getBehandlingUuid(), List.of()).contains(request.getDokumentMal());
    }

    @POST
    @Path("brev/bestill")
    @Produces(MediaType.APPLICATION_JSON)
    public void bestillDokument(DokumentbestillingDto request) {
        dokumentProduksjon.putIfAbsent(request.getBehandlingUuid(), new ArrayList<>());
        dokumentProduksjon.get(request.getBehandlingUuid()).add(request.getDokumentMal());
    }

    @POST
    @Path("saksbehandlertekst/hent")
    @Produces(MediaType.APPLICATION_JSON)
    public TekstFraSaksbehandlerDto hentSaksbehandlersTekst(BehandlingUuidDto uuidDto) {
        return saksbehandlerTekst.getOrDefault(uuidDto.getBehandlingUuid(), null);
    }

    @POST
    @Path("saksbehandlertekst/lagre")
    @Produces(MediaType.APPLICATION_JSON)
    public void lagreSaksbehandlersTekst(TekstFraSaksbehandlerDto request) {
        saksbehandlerTekst.put(request.getBehandlingUuid(), request);
    }

}
