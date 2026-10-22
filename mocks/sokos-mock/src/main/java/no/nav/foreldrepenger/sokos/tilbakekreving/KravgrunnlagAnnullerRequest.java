package no.nav.foreldrepenger.sokos.tilbakekreving;

public record KravgrunnlagAnnullerRequest(String kodeAksjon,
                                          Integer vedtakId,
                                          String enhetAnsvarlig,
                                          String saksbehandlerId) {
}
