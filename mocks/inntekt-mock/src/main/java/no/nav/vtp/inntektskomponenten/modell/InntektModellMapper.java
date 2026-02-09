package no.nav.vtp.inntektskomponenten.modell;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektType;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektYtelseType;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.Inntektsperiode;
import no.nav.vtp.inntektskomponenten.Inntektsinformasjon;

public class InntektModellMapper {

    public static List<Inntektsinformasjon> makeInntektsinformasjon(InntektskomponentModell modell, YearMonth fom, YearMonth tom,
                                                                    String filter, String brukerIdent) {

        List<Inntektsinformasjon> respons = new ArrayList<>();

        YearMonth runningMonth = fom;
        while (runningMonth.isBefore(tom) || runningMonth.equals(tom)) {
            respons.addAll(inntektListeFraModell(modell.getInntektsperioderSplittMånedlig(), runningMonth, filter, brukerIdent));
            runningMonth = runningMonth.plusMonths(1);
        }

        return respons;
    }

    private static List<Inntektsinformasjon> inntektListeFraModell(List<Inntektsperiode> modellPeriode, YearMonth måned,
                                                                   String filter, String brukerIdent) {
        return modellPeriode.stream()
                .filter(t -> localDateTimeInYearMonth(t.tom(), måned))
                .filter(t -> taMedFraInntektsfilter(t, filter))
                .map(temp -> inntektFraModell(temp, måned, brukerIdent))
                .toList();
    }

    private static Inntektsinformasjon inntektFraModell(Inntektsperiode ip, YearMonth måned, String brukerIdent) {
        var underenhet = ip.orgnr() != null  && !ip.orgnr().isEmpty() ? ip.orgnr() : ip.arbeidsgiver().getAktørIdent();
        var inntekt =  new Inntektsinformasjon.Inntekt(fraModellInntektstype(ip.inntektType()), new BigDecimal(ip.beløp()),
                ip.beskrivelse(), ip.skatteOgAvgiftsregel(), ip.fom(), ip.tom(), null);
        return new Inntektsinformasjon(måned, null, underenhet, brukerIdent, List.of(inntekt));
    }

    private static boolean taMedFraInntektsfilter(Inntektsperiode p, String filter) {
        // Opptjeningsfilter har med alt. Lønn skal alltid med
        if (!filter.startsWith("8") || p.inntektYtelseType() == null || InntektYtelseType.InntektType.LØNNSINNTEKT.equals(p.inntektYtelseType().getInntektType())) {
            return true;
        }
        // Beregningsfilter har ikke med ytelser
        if (filter.startsWith("8-28") ||
                InntektYtelseType.InntektType.PENSJON_ELLER_TRYGD.equals(p.inntektYtelseType().getInntektType()) ||
                InntektYtelseType.InntektType.NÆRINGSINNTEKT.equals(p.inntektYtelseType().getInntektType())) {
            return false;
        }
        // Sammenligningsfilter har med enkelte ytelser (ikke DAG, AAP)
        return Set.of(InntektYtelseType.SYKEPENGER, InntektYtelseType.SYKEPENGER_FISKER_HYRE, InntektYtelseType.FORELDREPENGER,
                InntektYtelseType.SVANGERSKAPSPENGER, InntektYtelseType.PLEIEPENGER, InntektYtelseType.OMSORGSPENGER,
                InntektYtelseType.OPPLÆRINGSPENGER).contains(p.inntektYtelseType());
    }

    private static boolean localDateTimeInYearMonth(LocalDate ldt, YearMonth yearMonth) {
        return YearMonth.of(ldt.getYear(), ldt.getMonth()).compareTo(yearMonth) == 0;
    }

    private static String fraModellInntektstype(InntektType modellType) {
        return switch (modellType) {
            case LØNNSINNTEKT -> "Loennsinntekt";
            case NÆRINGSINNTEKT -> "Naeringsinntekt";
            case PENSJON_ELLER_TRYGD -> "PensjonEllerTrygd";
            case YTELSE_FRA_OFFENTLIGE -> "YtelseFraOffentlige";
        };
    }

}
