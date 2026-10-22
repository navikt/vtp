package no.nav.foreldrepenger.sokos.tilbakekreving;

public record KravgrunnlagAnnulerResponse(Integer status,
                                          String melding,
                                          Integer vedtakId,
                                          String saksbehandlerId) {
}
