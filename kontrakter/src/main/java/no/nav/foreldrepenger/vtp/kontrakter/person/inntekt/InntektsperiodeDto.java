package no.nav.foreldrepenger.vtp.kontrakter.person.inntekt;


import java.time.LocalDate;

import no.nav.foreldrepenger.vtp.kontrakter.person.arbeidsforhold.ArbeidsgiverDto;

public record InntektsperiodeDto(ArbeidsgiverDto arbeidsgiver,
                                 LocalDate fom,
                                 LocalDate tom,
                                 Integer beløp,
                                 Type inntektType,
                                 YtelseType ytelseType,
                                 FordelType inntektFordel) {

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(InntektsperiodeDto existing) {
        return new Builder(existing);
    }

    public static class Builder {
        private ArbeidsgiverDto arbeidsgiver;
        private LocalDate fom;
        private LocalDate tom;
        private Integer beløp;
        private Type inntektType;
        private YtelseType ytelseType;
        private FordelType inntektFordel;

        public Builder() {}

        public Builder(InntektsperiodeDto existing) {
            this.arbeidsgiver = existing.arbeidsgiver();
            this.fom = existing.fom();
            this.tom = existing.tom();
            this.beløp = existing.beløp();
            this.inntektType = existing.inntektType();
            this.ytelseType = existing.ytelseType();
            this.inntektFordel = existing.inntektFordel();
        }

        public Builder medArbeidsgiver(ArbeidsgiverDto arbeidsgiver) { this.arbeidsgiver = arbeidsgiver; return this; }
        public Builder medFom(LocalDate fom) { this.fom = fom; return this; }
        public Builder medTom(LocalDate tom) { this.tom = tom; return this; }
        public Builder medBeløp(Integer beløp) { this.beløp = beløp; return this; }
        public Builder medInntektType(Type inntektType) { this.inntektType = inntektType; return this; }
        public Builder medYtelseType(YtelseType ytelseType) { this.ytelseType = ytelseType; return this; }
        public Builder medInntektFordel(FordelType inntektFordel) { this.inntektFordel = inntektFordel; return this; }

        public InntektsperiodeDto build() {
            return new InntektsperiodeDto(arbeidsgiver, fom, tom, beløp, inntektType, ytelseType, inntektFordel);
        }
    }


    public enum Type {
        LØNNSINNTEKT,
        NÆRINGSINNTEKT,
        PENSJON_ELLER_TRYGD,
        YTELSE_FRA_OFFENTLIGE
    }

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
        KOMPENSASJON_FOR_TAPT_PERSONINNTEKT
        ;

    }
}
