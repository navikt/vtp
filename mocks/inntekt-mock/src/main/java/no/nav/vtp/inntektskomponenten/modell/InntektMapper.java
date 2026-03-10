package no.nav.vtp.inntektskomponenten.modell;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import no.nav.vtp.inntekt.Inntektsperiode;
import no.nav.vtp.inntektskomponenten.Inntektsinformasjon;

public class InntektMapper {

    private InntektMapper() {
        // Bare statiske metoder
    }

    public static List<Inntektsinformasjon> makeInntektsinformasjon(List<Inntektsperiode> inntektsperioder,
                                                                    YearMonth fom, YearMonth tom,
                                                                    String filter, String brukerIdent) {
        List<Inntektsinformasjon> respons = new ArrayList<>();
        YearMonth runningMonth = fom;
        while (runningMonth.isBefore(tom) || runningMonth.equals(tom)) {
            respons.addAll(inntektListeForMåned(inntektsperioder, runningMonth, filter, brukerIdent));
            runningMonth = runningMonth.plusMonths(1);
        }
        return respons;
    }

    private static List<Inntektsinformasjon> inntektListeForMåned(List<Inntektsperiode> perioder, YearMonth måned,
                                                                   String filter, String brukerIdent) {
        return perioder.stream()
                .flatMap(p -> splittInntektsperiodeTilMånedligeIntervall(p).stream())
                .filter(p -> erIMåned(p, måned))
                .filter(p -> taMedFraInntektsfilter(p, filter))
                .map(p -> tilInntektsinformasjon(p, måned, brukerIdent))
                .toList();
    }

    private static List<Inntektsperiode> splittInntektsperiodeTilMånedligeIntervall(Inntektsperiode inntektsperiode) {
        List<Inntektsperiode> månedlige = new ArrayList<>();
        var start = YearMonth.from(inntektsperiode.fom());
        var slutt = YearMonth.from(inntektsperiode.tom());

        var current = start;
        while (!current.isAfter(slutt)) {
            var maanedFom = current.equals(start)
                    ? inntektsperiode.fom()
                    : current.atDay(1);
            var maanedTom = current.equals(slutt)
                    ? inntektsperiode.tom()
                    : current.atEndOfMonth();

            månedlige.add(new Inntektsperiode(
                    inntektsperiode.arbeidsgiver(),
                    maanedFom,
                    maanedTom,
                    inntektsperiode.beløp(),
                    inntektsperiode.ytelseType(),
                    inntektsperiode.inntektFordel()
            ));
            current = current.plusMonths(1);
        }
        return månedlige;
    }

    private static boolean erIMåned(Inntektsperiode periode, YearMonth måned) {
        return YearMonth.of(periode.tom().getYear(), periode.tom().getMonth()).equals(måned);
    }

    private static Inntektsinformasjon tilInntektsinformasjon(Inntektsperiode periode, YearMonth måned, String brukerIdent) {
        var underenhet = periode.arbeidsgiver().identifikator();

        var inntekt = new Inntektsinformasjon.Inntekt(
                fraInntektType(tilInntektstype(periode.ytelseType())),
                new BigDecimal(periode.beløp()),
                "fastloenn",
                null,
                periode.fom(),
                periode.tom(),
                null);

        return new Inntektsinformasjon(måned, null, underenhet, brukerIdent, List.of(inntekt));
    }

    private static boolean taMedFraInntektsfilter(Inntektsperiode p, String filter) {
        // Opptjeningsfilter har med alt. Lønn skal alltid med
        if (!filter.startsWith("8") || erLønnsinntekt(p)) {
            return true;
        }
        // Beregningsfilter har ikke med ytelser
        if (filter.startsWith("8-28") || erPensjonEllerTrygd(p) || erNæringsinntekt(p)) {
            return false;
        }
        // Sammenligningsfilter har med enkelte ytelser (ikke DAG, AAP)
        return Set.of(
                Inntektsperiode.YtelseType.SYKEPENGER,
                Inntektsperiode.YtelseType.SYKEPENGER_FISKER_HYRE,
                Inntektsperiode.YtelseType.FORELDREPENGER,
                Inntektsperiode.YtelseType.SVANGERSKAPSPENGER,
                Inntektsperiode.YtelseType.PLEIEPENGER,
                Inntektsperiode.YtelseType.OMSORGSPENGER,
                Inntektsperiode.YtelseType.OPPLÆRINGSPENGER
        ).contains(p.ytelseType());
    }

    private static boolean erLønnsinntekt(Inntektsperiode p) {
        var ytelsestyperSomRegnesSomLønn = Set.of(
                Inntektsperiode.YtelseType.FASTLØNN,
                Inntektsperiode.YtelseType.FERIEPENGER,
                Inntektsperiode.YtelseType.KOMMUNAL_OMSORGSLØNN_MM
        );
        return Inntektstype.LØNNSINNTEKT.equals(tilInntektstype(p.ytelseType())) &&
                ytelsestyperSomRegnesSomLønn.contains(p.ytelseType());
    }

    private static boolean erPensjonEllerTrygd(Inntektsperiode p) {
        return Inntektstype.PENSJON_ELLER_TRYGD.equals(tilInntektstype(p.ytelseType()));
    }

    private static boolean erNæringsinntekt(Inntektsperiode p) {
        return Inntektstype.NÆRINGSINNTEKT.equals(tilInntektstype(p.ytelseType()));
    }

    private static String fraInntektType(Inntektstype type) {
        return switch (type) {
            case LØNNSINNTEKT -> "Loennsinntekt";
            case NÆRINGSINNTEKT -> "Naeringsinntekt";
            case PENSJON_ELLER_TRYGD -> "PensjonEllerTrygd";
            case YTELSE_FRA_OFFENTLIGE -> "YtelseFraOffentlige";
        };
    }

    private static Inntektstype tilInntektstype(Inntektsperiode.YtelseType ytelseType) {
        return switch (ytelseType) {
            case FASTLØNN, FERIEPENGER, KOMMUNAL_OMSORGSLØNN_MM -> Inntektstype.LØNNSINNTEKT;
            case KVALIFISERINGSSTØNAD -> Inntektstype.PENSJON_ELLER_TRYGD;
            case AAP, DAGPENGER, DAGPENGER_FISKER_HYRE, FORELDREPENGER, SVANGERSKAPSPENGER, SYKEPENGER,
                 SYKEPENGER_FISKER_HYRE, OMSORGSPENGER, OPPLÆRINGSPENGER, PLEIEPENGER, OVERGANGSSTØNAD_ENSLIG,
                 VENTELØNN, FERIEPENGER_FORELDREPENGER, FERIEPENGER_SVANGERSKAPSPENGER, FERIEPENGER_OMSORGSPENGER,
                 FERIEPENGER_OPPLÆRINGSPENGER, FERIEPENGER_PLEIEPENGER, FERIEPENGER_SYKEPENGER,
                 FERIEPENGER_SYKEPENGER_FISKER_HYRE, FERIETILLEGG_DAGPENGER, FERIETILLEGG_DAGPENGER_FISKER_HYRE -> Inntektstype.YTELSE_FRA_OFFENTLIGE;
            case FORELDREPENGER_NÆRING, FORELDREPENGER_NÆRING_DAGMAMMA, FORELDREPENGER_NÆRING_FISKER,
                 FORELDREPENGER_NÆRING_JORDBRUK, SVANGERSKAPSPENGER_NÆRING, SYKEPENGER_NÆRING,
                 SYKEPENGER_NÆRING_DAGMAMMA, SYKEPENGER_NÆRING_FISKER, SYKEPENGER_NÆRING_JORDBRUK,
                 OMSORGSPENGER_NÆRING, OMSORGSPENGER_NÆRING_DAGMAMMA, OMSORGSPENGER_NÆRING_FISKER,
                 OMSORGSPENGER_NÆRING_JORDBRUK, OPPLÆRINGSPENGER_NÆRING, PLEIEPENGER_NÆRING,
                 PLEIEPENGER_NÆRING_DAGMAMMA, PLEIEPENGER_NÆRING_FISKER, PLEIEPENGER_NÆRING_JORDBRUK,
                 DAGPENGER_NÆRING, DAGPENGER_NÆRING_FISKER, ANNET, VEDERLAG, VEDERLAG_DAGMAMMA,
                 LOTT_KUN_TRYGDEAVGIFT, KOMPENSASJON_FOR_TAPT_PERSONINNTEKT -> Inntektstype.NÆRINGSINNTEKT;
        };
    }

    private enum Inntektstype {
        LØNNSINNTEKT,
        NÆRINGSINNTEKT,
        PENSJON_ELLER_TRYGD,
        YTELSE_FRA_OFFENTLIGE
    }
}
