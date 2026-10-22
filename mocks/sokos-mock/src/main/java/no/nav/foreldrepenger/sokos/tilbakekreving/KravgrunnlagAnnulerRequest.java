package no.nav.foreldrepenger.sokos.tilbakekreving;

public record KravgrunnlagAnnulerRequest(String kodeAksjon,
                                         Integer vedtakId,
                                         String enhetAnsvarlig,
                                         String saksbehandlerId) {
}
