package no.nav.foreldrepenger.vtp.kontrakter.person.v2;

import java.time.LocalDate;

/**
 * Kun én type-enum (YtelseType) — dropper v1s InntektTypeDto/InntektYtelseType-dualitet.
 * Brukes for eksplisitt oppgitt inntekt (f.eks. selvstendig næringsdrivende). Inntekt avledet
 * fra ytelser (se YtelseDto) genereres av PersonMapperV2 og oppgis ikke direkte her.
 */
public record InntektsperiodeDto(ArbeidsgiverDto arbeidsgiver,
                                 LocalDate fom,
                                 LocalDate tom,
                                 Integer beløp,
                                 YtelseType ytelseType,
                                 FordelType inntektFordel) {

    /** Typer for eksplisitt lønnsinntekt. NAV-ytelser oppgis gjennom {@link YtelseDto}. */
    public enum YtelseType {
        FASTLØNN,
        FERIEPENGER,
        KOMMUNAL_OMSORGSLØNN_MM
    }

    public enum FordelType {
        KONTANTYTELSE,
        UTGIFTSGODTGJØRELSE,
        NATURALYTELSE
    }
}
