package no.nav.foreldrepenger.sokos.tilbakekreving;

public record KravgrunnlagAnnullerResponse(Integer status,
                                           String melding,
                                           Integer vedtakId,
                                           String saksbehandlerId) {
}
