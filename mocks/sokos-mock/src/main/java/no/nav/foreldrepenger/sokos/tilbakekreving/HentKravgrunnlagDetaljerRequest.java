package no.nav.foreldrepenger.sokos.tilbakekreving;

public record HentKravgrunnlagDetaljerRequest(String kodeAksjon,
                                              Integer kravgrunnlagId,
                                              String enhetAnsvarlig,
                                              String saksbehandlerId) {
}
