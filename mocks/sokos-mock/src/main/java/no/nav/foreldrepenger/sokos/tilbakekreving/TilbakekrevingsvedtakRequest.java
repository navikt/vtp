package no.nav.foreldrepenger.sokos.tilbakekreving;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record TilbakekrevingsvedtakRequest(String kodeAksjon,
                                           Integer vedtakId,
                                           LocalDate vedtaksDato,
                                           String kodeHjemmel,
                                           String enhetAnsvarlig,
                                           String kontrollfelt,
                                           String saksbehandlerId,
                                           LocalDate datoTilleggsfrist,
                                           List<Periode> perioder) {

    public record Periode(LocalDate periodeFom,
                          LocalDate periodeTom,
                          BigDecimal belopRenter,
                          List<Postering> posteringer) {
    }

    public record Postering(String kodeKlasse,
                            BigDecimal belopOpprinneligUtbetalt,
                            BigDecimal belopNy,
                            BigDecimal belopTilbakekreves,
                            BigDecimal belopUinnkrevd,
                            BigDecimal belopSkatt,
                            String kodeResultat,
                            String kodeAarsak,
                            String kodeSkyld) {
    }
}
