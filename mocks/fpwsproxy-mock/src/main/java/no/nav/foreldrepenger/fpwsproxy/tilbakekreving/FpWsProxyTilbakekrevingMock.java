package no.nav.foreldrepenger.fpwsproxy.tilbakekreving;


import static no.nav.foreldrepenger.fpwsproxy.tilbakekreving.Kravgrunnlag431DtoMapper.lagGeneriskKravgrunnlag;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.tilbakekreving.iverksett.TilbakekrevingVedtakDTO;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.tilbakekreving.kravgrunnlag.request.AnnullerKravGrunnlagDto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.tilbakekreving.kravgrunnlag.request.HentKravgrunnlagDetaljDto;
import no.nav.foreldrepenger.kontrakter.fpwsproxy.tilbakekreving.kravgrunnlag.respons.Kravgrunnlag431Dto;

@Path("/api/fpwsproxy/tilbakekreving")
public class FpWsProxyTilbakekrevingMock {
    private static final Logger LOG = LoggerFactory.getLogger(FpWsProxyTilbakekrevingMock.class);

    private static final String KRAVGRUNNLAG_PATH = "/kravgrunnlag";
    private static final String KRAVGRUNNLAG_ANNULLER_PATH = "/kravgrunnlag/annuller";
    private static final String TILBAKEKREVINGVEDTAK_PATH = "/tilbakekrevingsvedtak";

    @POST
    @Path(KRAVGRUNNLAG_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = "Hent kravgrunnlag", response = Kravgrunnlag431Dto[].class)
    public Response hentKravgrunnlag(@Valid HentKravgrunnlagDetaljDto hentKravgrunnlagDetaljDto) {
        LOG.info("Henter kravgrunnlag for behandling med kravgrunnlagid {}", hentKravgrunnlagDetaljDto.kravgrunnlagId());
        return Response.ok(lagGeneriskKravgrunnlag(hentKravgrunnlagDetaljDto))
                .build();
    }

    @PUT
    @Path(KRAVGRUNNLAG_ANNULLER_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = "Annuler kravgrunnlag")
    public Response annulerKravgrunnlag(@Valid AnnullerKravGrunnlagDto annullerKravGrunnlagDto) {
        LOG.info("Annuler grunnlag med vedtaksId {}", annullerKravGrunnlagDto.vedtakId());
        return Response.ok().build();
    }

    @POST
    @Path(TILBAKEKREVINGVEDTAK_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = "Iverksett tilbakekrevingsvedtak", response = TilbakekrevingVedtakDTO[].class)
    public Response iverksettTilbakekrevingsvedtak(@Valid @NotNull TilbakekrevingVedtakDTO tilbakekrevingVedtakDTO) {
        LOG.info("Iverksetter tilbakekrevingsvedtak for {}", tilbakekrevingVedtakDTO.vedtakId());
        return Response.ok()
                .build();
    }
}
