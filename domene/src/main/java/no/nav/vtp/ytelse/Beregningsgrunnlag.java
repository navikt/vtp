package no.nav.vtp.ytelse;

import java.math.BigDecimal;

public record Beregningsgrunnlag(Kategori kategori, BigDecimal beløp) {

    public enum Kategori {
        ARBEIDSTAKER,
        FRILANSER,
        DAGPENGER,
        ARBEIDSAVKLARINGSPENGER,
        SJØMANN,
        SELVSTENDIG_NÆRINGSDRIVENDE,
        DAGMAMMA,
        JORDBRUKER,
        FISKER,
        ARBEIDSTAKER_UTEN_FERIEPENGER,
    }
}
