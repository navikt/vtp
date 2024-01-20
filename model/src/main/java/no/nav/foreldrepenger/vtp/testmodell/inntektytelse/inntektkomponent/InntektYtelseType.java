package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent;

/*
 * Ytvalg fra kodeverkene Loennsbeskrivelse, Næringsinntektbeskrivelse, PensjonEllerTrygdBeskrivelse, YtelseFraOffentligeBeskrivelse
 */
public enum InntektYtelseType {

    // Loennsbeskrivelse - Lønn
    FASTLØNN(InntektType.LØNNSINNTEKT, "fastloenn"),
    FERIEPENGER(InntektType.LØNNSINNTEKT, "feriepenger"),
    KOMMUNAL_OMSORGSLØNN_MM(InntektType.LØNNSINNTEKT, "kommunalOmsorgsloennOgFosterhjemsgodtgjoerelse"), // Tilkommet for K9

    // YtelseFraOffentligeBeskrivelse - Ytelse utbetalt til person som er arbeidstaker/frilanser/ytelsesmottaker
    AAP(InntektType.YTELSE_FRA_OFFENTLIGE, "arbeidsavklaringspenger"),
    DAGPENGER(InntektType.YTELSE_FRA_OFFENTLIGE, "dagpengerVedArbeidsloeshet"),
    DAGPENGER_FISKER_HYRE(InntektType.YTELSE_FRA_OFFENTLIGE, "dagpengerTilFiskerSomBareHarHyre"),
    FORELDREPENGER(InntektType.YTELSE_FRA_OFFENTLIGE, "foreldrepenger"),
    SVANGERSKAPSPENGER(InntektType.YTELSE_FRA_OFFENTLIGE, "svangerskapspenger"),
    SYKEPENGER(InntektType.YTELSE_FRA_OFFENTLIGE, "sykepenger"),
    SYKEPENGER_FISKER_HYRE(InntektType.YTELSE_FRA_OFFENTLIGE, "sykepengerTilFiskerSomBareHarHyre"),
    OMSORGSPENGER(InntektType.YTELSE_FRA_OFFENTLIGE, "omsorgspenger"),
    OPPLÆRINGSPENGER(InntektType.YTELSE_FRA_OFFENTLIGE, "opplaeringspenger"),
    PLEIEPENGER(InntektType.YTELSE_FRA_OFFENTLIGE, "pleiepenger"),
    OVERGANGSSTØNAD_ENSLIG(InntektType.YTELSE_FRA_OFFENTLIGE, "overgangsstoenadTilEnsligMorEllerFarSomBegynteAaLoepe1April2014EllerSenere"),
    VENTELØNN(InntektType.YTELSE_FRA_OFFENTLIGE, "venteloenn"),

    FERIEPENGER_FORELDREPENGER(InntektType.YTELSE_FRA_OFFENTLIGE, "feriepengerForeldrepenger"),
    FERIEPENGER_SVANGERSKAPSPENGER(InntektType.YTELSE_FRA_OFFENTLIGE, "feriepengerSvangerskapspenger"),
    FERIEPENGER_OMSORGSPENGER(InntektType.YTELSE_FRA_OFFENTLIGE, "feriepengerOmsorgspenger"),
    FERIEPENGER_OPPLÆRINGSPENGER(InntektType.YTELSE_FRA_OFFENTLIGE, "feriepengerOpplaeringspenger"),
    FERIEPENGER_PLEIEPENGER(InntektType.YTELSE_FRA_OFFENTLIGE, "feriepengerPleiepenger"),
    FERIEPENGER_SYKEPENGER(InntektType.YTELSE_FRA_OFFENTLIGE, "feriepengerSykepenger"),
    FERIEPENGER_SYKEPENGER_FISKER_HYRE(InntektType.YTELSE_FRA_OFFENTLIGE, "feriepengerSykepengerTilFiskerSomBareHarHyre"),
    FERIETILLEGG_DAGPENGER(InntektType.YTELSE_FRA_OFFENTLIGE, "ferietilleggDagpengerVedArbeidsloeshet"),
    FERIETILLEGG_DAGPENGER_FISKER_HYRE(InntektType.YTELSE_FRA_OFFENTLIGE, "ferietilleggDagpengerTilFiskerSomBareHarHyre"),

    // PensjonEllerTrygdBeskrivelse - Annen ytelse utbetalt til person
    KVALIFISERINGSSTØNAD(InntektType.PENSJON_ELLER_TRYGD, "kvalifiseringstoenad"),

    // Næringsinntektbeskrivelse, Ytelse utbetalt til person som er næringsdrivende, fisker/lott, dagmamma eller jord/skogbruker
    FORELDREPENGER_NÆRING(InntektType.NÆRINGSINNTEKT, "foreldrepenger"),
    FORELDREPENGER_NÆRING_DAGMAMMA(InntektType.NÆRINGSINNTEKT, "foreldrepengerTilDagmamma"),
    FORELDREPENGER_NÆRING_FISKER(InntektType.NÆRINGSINNTEKT, "foreldrepengerTilFisker"),
    FORELDREPENGER_NÆRING_JORDBRUK(InntektType.NÆRINGSINNTEKT, "foreldrepengerTilJordOgSkogbrukere"),
    SVANGERSKAPSPENGER_NÆRING(InntektType.NÆRINGSINNTEKT, "svangerskapspenger"),
    SYKEPENGER_NÆRING(InntektType.NÆRINGSINNTEKT, "sykepenger"),
    SYKEPENGER_NÆRING_DAGMAMMA(InntektType.NÆRINGSINNTEKT, "sykepengerTilDagmamma"),
    SYKEPENGER_NÆRING_FISKER(InntektType.NÆRINGSINNTEKT, "sykepengerTilFisker"),
    SYKEPENGER_NÆRING_JORDBRUK(InntektType.NÆRINGSINNTEKT, "sykepengerTilJordOgSkogbrukere"),
    OMSORGSPENGER_NÆRING(InntektType.NÆRINGSINNTEKT, "omsorgspenger"),
    OMSORGSPENGER_NÆRING_DAGMAMMA(InntektType.NÆRINGSINNTEKT, "omsorgspengerTilDagmamma"),
    OMSORGSPENGER_NÆRING_FISKER(InntektType.NÆRINGSINNTEKT, "omsorgspengerTilFisker"),
    OMSORGSPENGER_NÆRING_JORDBRUK(InntektType.NÆRINGSINNTEKT, "omsorgspengerTilJordOgSkogbrukere"),
    OPPLÆRINGSPENGER_NÆRING(InntektType.NÆRINGSINNTEKT, "opplaeringspenger"),
    PLEIEPENGER_NÆRING(InntektType.NÆRINGSINNTEKT, "pleiepenger"),
    PLEIEPENGER_NÆRING_DAGMAMMA(InntektType.NÆRINGSINNTEKT, "pleiepengerTilDagmamma"),
    PLEIEPENGER_NÆRING_FISKER(InntektType.NÆRINGSINNTEKT, "pleiepengerTilFisker"),
    PLEIEPENGER_NÆRING_JORDBRUK(InntektType.NÆRINGSINNTEKT, "pleiepengerTilJordOgSkogbrukere"),
    DAGPENGER_NÆRING(InntektType.NÆRINGSINNTEKT, "dagpengerVedArbeidsloeshet"),
    DAGPENGER_NÆRING_FISKER(InntektType.NÆRINGSINNTEKT, "dagpengerTilFisker"),

    // Annen ytelse utbetalt til person som er næringsdrivende
    ANNET(InntektType.NÆRINGSINNTEKT, "annet"),
    VEDERLAG(InntektType.NÆRINGSINNTEKT, "vederlag"),
    VEDERLAG_DAGMAMMA(InntektType.NÆRINGSINNTEKT, "vederlagDagmammaIEgetHjem"),
    LOTT_KUN_TRYGDEAVGIFT(InntektType.NÆRINGSINNTEKT, "lottKunTrygdeavgift"),
    KOMPENSASJON_FOR_TAPT_PERSONINNTEKT(InntektType.NÆRINGSINNTEKT, "kompensasjonForTaptPersoninntekt")
    ;

    private final InntektType inntektType;
    private final String beskrivelse; // Finnes i feltet beskrivelse i Inntektskomponentens felt Inntekt . beskrivelse

    InntektYtelseType(InntektType inntektType, String offisiellKode) {
        this.inntektType = inntektType;
        this.beskrivelse = offisiellKode;
    }

    public InntektType getInntektType() {
        return inntektType;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public enum InntektType {
        LØNNSINNTEKT,
        NÆRINGSINNTEKT,
        PENSJON_ELLER_TRYGD,
        YTELSE_FRA_OFFENTLIGE
    }
}
