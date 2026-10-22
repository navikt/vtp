package no.nav.foreldrepenger.sokos.tilbakekreving;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import no.nav.vtp.tilbake.TilbakekrevingSaksnummer;

@Path("/sokos/api/v1/tilbakekreving")
public class SokosTilbakekrevingMock {
    private static final Logger LOG = LoggerFactory.getLogger(SokosTilbakekrevingMock.class);

    private static final String KRAVGRUNNLAG_PATH = "/kravgrunnlag/detaljer";
    private static final String KRAVGRUNNLAG_ANNULLER_PATH = "/kravgrunnlag/annuller";
    private static final String TILBAKEKREVINGVEDTAK_PATH = "/vedtak";

    @POST
    @Path(KRAVGRUNNLAG_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response hentKravgrunnlag(@Valid HentKravgrunnlagDetaljerRequest hentKravgrunnlagDetaljerRequest) {
        LOG.info("Henter kravgrunnlag for behandling med kravgrunnlagid {}", hentKravgrunnlagDetaljerRequest.kravgrunnlagId());
        var respons = new HentKravgrunnlagDetaljerResponse(0, "", lagGeneriskKravgrunnlag(hentKravgrunnlagDetaljerRequest));
        return Response.ok(respons).build();
    }

    @POST
    @Path(KRAVGRUNNLAG_ANNULLER_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response annulerKravgrunnlag(@Valid KravgrunnlagAnnulerRequest kravgrunnlagAnnulerRequest) {
        LOG.info("Annuler grunnlag med vedtaksId {}", kravgrunnlagAnnulerRequest.vedtakId());
        var respons = new KravgrunnlagAnnulerResponse(0, "", kravgrunnlagAnnulerRequest.vedtakId(),
                kravgrunnlagAnnulerRequest.saksbehandlerId());
        return Response.ok(respons).build();
    }

    @POST
    @Path(TILBAKEKREVINGVEDTAK_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response iverksettTilbakekrevingsvedtak(@Valid @NotNull TilbakekrevingsvedtakRequest tilbakekrevingsvedtakRequest) {
        LOG.info("Iverksetter tilbakekrevingsvedtak for {}", tilbakekrevingsvedtakRequest.vedtakId());
        var respons = new TilbakekrevingsvedtakResponse(0, "", tilbakekrevingsvedtakRequest.vedtakId(),
                tilbakekrevingsvedtakRequest.vedtaksDato());
        return Response.ok(respons).build();
    }

    public static HentKravgrunnlagDetaljerResponse.KravgrunnlagDetaljer lagGeneriskKravgrunnlag(HentKravgrunnlagDetaljerRequest request) {
        return new HentKravgrunnlagDetaljerResponse.KravgrunnlagDetaljer(Long.valueOf(request.kravgrunnlagId()),
                207406L,
                KravStatusKode.NY,
                FagOmrådeKode.FP,
                TilbakekrevingSaksnummer.getSisteSaksnummer() + "100",
                LocalDate.of(2020, 5, 1),
                null,
                "910909088",
                GjelderType.ORGANISASJON,
                "910909088",
                GjelderType.ORGANISASJON,
                null,
                false,
                request.enhetAnsvarlig(),
                request.enhetAnsvarlig(),
                request.enhetAnsvarlig(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH.mm.ss.SSSSSS")),
                request.saksbehandlerId(),
                TilbakekrevingSaksnummer.getSisteHenvisning(),
                null,
                hentPerioder());
    }

    private static List<HentKravgrunnlagDetaljerResponse.DetaljerPeriode> hentPerioder() {
        var kravgrunnlagPeriode1 = new HentKravgrunnlagDetaljerResponse.DetaljerPeriode(
                LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31),
                BigDecimal.valueOf(2700), List.of(
                        lagPostering(BigDecimal.valueOf(6000.00), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, KlasseType.FEIL, 30),
                        lagPostering(BigDecimal.ZERO, BigDecimal.valueOf(6000.00), BigDecimal.valueOf(6000.00), BigDecimal.ZERO, KlasseType.YTEL, 30)
                )
        );
        var kravgrunnlagPeriode2 = new HentKravgrunnlagDetaljerResponse.DetaljerPeriode(
                LocalDate.of(2020, 4, 1), LocalDate.of(2020, 4, 30),
                BigDecimal.valueOf(6300),
                List.of(
                        lagPostering(BigDecimal.valueOf(21000.00), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, KlasseType.FEIL, 30),
                        lagPostering(BigDecimal.ZERO, BigDecimal.valueOf(21000.00), BigDecimal.valueOf(21000.00), BigDecimal.ZERO, KlasseType.YTEL, 30)
                ));
        return List.of(kravgrunnlagPeriode1, kravgrunnlagPeriode2);
    }

    private static HentKravgrunnlagDetaljerResponse.DetaljerPostering lagPostering(
            BigDecimal nyBeløp, BigDecimal tilbakekrevesBeløp,
            BigDecimal opprUtbetBeløp, BigDecimal uInnkrevdBeløp,
            KlasseType klassetype, int skatteprosent) {
        return new HentKravgrunnlagDetaljerResponse.DetaljerPostering(
                "FPATAL",
                klassetype,
                opprUtbetBeløp,
                nyBeløp,
                tilbakekrevesBeløp,
                uInnkrevdBeløp,
                BigDecimal.valueOf(skatteprosent),
                null,
                null,
                null
        );
    }
}
