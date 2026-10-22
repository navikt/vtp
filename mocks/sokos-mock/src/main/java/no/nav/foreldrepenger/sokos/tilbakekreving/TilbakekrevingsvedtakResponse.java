package no.nav.foreldrepenger.sokos.tilbakekreving;

import java.time.LocalDate;

public record TilbakekrevingsvedtakResponse(Integer status,
                                            String melding,
                                            Integer vedtakId,
                                            LocalDate datoVedtakFagsystem) {
}
