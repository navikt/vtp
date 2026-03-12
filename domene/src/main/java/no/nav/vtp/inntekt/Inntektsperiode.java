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
    }

    public Builder tilBuilder() {
        return new Builder(this);
    }

    public static class Builder {
        private Inntektsperiode kopi;

        public Builder(Inntektsperiode inntektsperiode) {
            this.kopi = inntektsperiode;
        }

        public Builder medArbeidsgiver(Arbeidsgiver arbeidsgiver) {
            kopi = new Inntektsperiode(arbeidsgiver, kopi.fom, kopi.tom, kopi.beløp, kopi.ytelseType, kopi.inntektFordel);
            return this;
        }

        public Builder medFom(LocalDate fom) {
            kopi = new Inntektsperiode(kopi.arbeidsgiver, fom, kopi.tom, kopi.beløp, kopi.ytelseType, kopi.inntektFordel);
            return this;
        }

        public Builder medTom(LocalDate tom) {
            kopi = new Inntektsperiode(kopi.arbeidsgiver, kopi.fom, tom, kopi.beløp, kopi.ytelseType, kopi.inntektFordel);
            return this;
        }

        public Builder medBeløp(Integer beløp) {
            kopi = new Inntektsperiode(kopi.arbeidsgiver, kopi.fom, kopi.tom, beløp, kopi.ytelseType, kopi.inntektFordel);
            return this;
        }

        public Builder medYtelseType(YtelseType ytelseType) {
            kopi = new Inntektsperiode(kopi.arbeidsgiver, kopi.fom, kopi.tom, kopi.beløp, ytelseType, kopi.inntektFordel);
            return this;
        }

        public Builder medInntektFordel(FordelType inntektFordel) {
            kopi = new Inntektsperiode(kopi.arbeidsgiver, kopi.fom, kopi.tom, kopi.beløp, kopi.ytelseType, inntektFordel);
            return this;
        }

        public Inntektsperiode build() {
            return kopi;
        }
    }
}
