package no.nav.vtp.inntekt;


import java.time.LocalDate;

import no.nav.vtp.arbeidsforhold.Arbeidsgiver;

public record Inntektsperiode(Arbeidsgiver arbeidsgiver,
                              LocalDate fom,
                              LocalDate tom,
                              Integer beløp,
                              YtelseType ytelseType,
                              FordelType inntektFordel) {

    public enum FordelType {
        KONTANTYTELSE,
        UTGIFTSGODTGJØRELSE,
        NATURALYTELSE
    }

    public enum YtelseType {

        // Loennsbeskrivelse - Lønn
        FASTLØNN,
        FERIEPENGER,
        KOMMUNAL_OMSORGSLØNN_MM, // Tilkommet for K9

        // YtelseFraOffentligeBeskrivelse - Ytelse utbetalt til person som er arbeidstaker/frilanser/ytelsesmottaker
        AAP,
        DAGPENGER,
        DAGPENGER_FISKER_HYRE,
        FORELDREPENGER,
        SVANGERSKAPSPENGER,
        SYKEPENGER,
        SYKEPENGER_FISKER_HYRE,
        OMSORGSPENGER,
        OPPLÆRINGSPENGER,
        PLEIEPENGER,
        OVERGANGSSTØNAD_ENSLIG,
        VENTELØNN,
        FERIEPENGER_FORELDREPENGER,
        FERIEPENGER_SVANGERSKAPSPENGER,
        FERIEPENGER_OMSORGSPENGER,
        FERIEPENGER_OPPLÆRINGSPENGER,
        FERIEPENGER_PLEIEPENGER,
        FERIEPENGER_SYKEPENGER,
        FERIEPENGER_SYKEPENGER_FISKER_HYRE,
        FERIETILLEGG_DAGPENGER,
        FERIETILLEGG_DAGPENGER_FISKER_HYRE,

        // PensjonEllerTrygdBeskrivelse - Annen ytelse utbetalt til person
        KVALIFISERINGSSTØNAD,

        // Næringsinntektbeskrivelse, Ytelse utbetalt til person som er næringsdrivende, fisker/lott, dagmamma eller jord/skogbruker
        FORELDREPENGER_NÆRING,
        FORELDREPENGER_NÆRING_DAGMAMMA,
        FORELDREPENGER_NÆRING_FISKER,
        FORELDREPENGER_NÆRING_JORDBRUK,
        SVANGERSKAPSPENGER_NÆRING,
        SYKEPENGER_NÆRING,
        SYKEPENGER_NÆRING_DAGMAMMA,
        SYKEPENGER_NÆRING_FISKER,
        SYKEPENGER_NÆRING_JORDBRUK,
        OMSORGSPENGER_NÆRING,
        OMSORGSPENGER_NÆRING_DAGMAMMA,
        OMSORGSPENGER_NÆRING_FISKER,
        OMSORGSPENGER_NÆRING_JORDBRUK,
        OPPLÆRINGSPENGER_NÆRING,
        PLEIEPENGER_NÆRING,
        PLEIEPENGER_NÆRING_DAGMAMMA,
        PLEIEPENGER_NÆRING_FISKER,
        PLEIEPENGER_NÆRING_JORDBRUK,
        DAGPENGER_NÆRING,
        DAGPENGER_NÆRING_FISKER,

        // Annen ytelse utbetalt til person som er næringsdrivende
        ANNET,
        VEDERLAG, VEDERLAG_DAGMAMMA,
        LOTT_KUN_TRYGDEAVGIFT,
        KOMPENSASJON_FOR_TAPT_PERSONINNTEKT;

        public Type inntektType() {
            return switch (this) {
                case FASTLØNN, FERIEPENGER, KOMMUNAL_OMSORGSLØNN_MM -> Type.LØNNSINNTEKT;
                case KVALIFISERINGSSTØNAD -> Type.PENSJON_ELLER_TRYGD;
                case AAP, DAGPENGER, DAGPENGER_FISKER_HYRE, FORELDREPENGER, SVANGERSKAPSPENGER, SYKEPENGER,
                     SYKEPENGER_FISKER_HYRE, OMSORGSPENGER, OPPLÆRINGSPENGER, PLEIEPENGER, OVERGANGSSTØNAD_ENSLIG,
                     VENTELØNN, FERIEPENGER_FORELDREPENGER, FERIEPENGER_SVANGERSKAPSPENGER, FERIEPENGER_OMSORGSPENGER,
                     FERIEPENGER_OPPLÆRINGSPENGER, FERIEPENGER_PLEIEPENGER, FERIEPENGER_SYKEPENGER,
                     FERIEPENGER_SYKEPENGER_FISKER_HYRE, FERIETILLEGG_DAGPENGER, FERIETILLEGG_DAGPENGER_FISKER_HYRE -> Type.YTELSE_FRA_OFFENTLIGE;
                case FORELDREPENGER_NÆRING, FORELDREPENGER_NÆRING_DAGMAMMA, FORELDREPENGER_NÆRING_FISKER,
                     FORELDREPENGER_NÆRING_JORDBRUK, SVANGERSKAPSPENGER_NÆRING, SYKEPENGER_NÆRING,
                     SYKEPENGER_NÆRING_DAGMAMMA, SYKEPENGER_NÆRING_FISKER, SYKEPENGER_NÆRING_JORDBRUK,
                     OMSORGSPENGER_NÆRING, OMSORGSPENGER_NÆRING_DAGMAMMA, OMSORGSPENGER_NÆRING_FISKER,
                     OMSORGSPENGER_NÆRING_JORDBRUK, OPPLÆRINGSPENGER_NÆRING, PLEIEPENGER_NÆRING,
                     PLEIEPENGER_NÆRING_DAGMAMMA, PLEIEPENGER_NÆRING_FISKER, PLEIEPENGER_NÆRING_JORDBRUK,
                     DAGPENGER_NÆRING, DAGPENGER_NÆRING_FISKER, ANNET, VEDERLAG, VEDERLAG_DAGMAMMA,
                     LOTT_KUN_TRYGDEAVGIFT, KOMPENSASJON_FOR_TAPT_PERSONINNTEKT -> Type.NÆRINGSINNTEKT;
            };
        }
        public enum Type {
            LØNNSINNTEKT,
            NÆRINGSINNTEKT,
            PENSJON_ELLER_TRYGD,
            YTELSE_FRA_OFFENTLIGE
        }
    }
}
