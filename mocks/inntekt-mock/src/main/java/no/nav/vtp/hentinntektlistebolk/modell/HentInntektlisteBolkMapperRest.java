package no.nav.vtp.hentinntektlistebolk.modell;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.FrilansArbeidsforholdsperiode;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektYtelseType;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.Inntektsperiode;
import no.nav.tjenester.aordningen.inntektsinformasjon.Aktoer;
import no.nav.tjenester.aordningen.inntektsinformasjon.ArbeidsInntektIdent;
import no.nav.tjenester.aordningen.inntektsinformasjon.ArbeidsInntektInformasjon;
import no.nav.tjenester.aordningen.inntektsinformasjon.ArbeidsInntektMaaned;
import no.nav.tjenester.aordningen.inntektsinformasjon.ArbeidsforholdFrilanser;
import no.nav.tjenester.aordningen.inntektsinformasjon.inntekt.Inntekt;
import no.nav.tjenester.aordningen.inntektsinformasjon.inntekt.InntektType;

public class HentInntektlisteBolkMapperRest {

    public static ArbeidsInntektIdent makeArbeidsInntektIdent(InntektskomponentModell modell, Aktoer aktoer, YearMonth fom, YearMonth tom,
                                                              boolean filter82830) {
        ArbeidsInntektIdent arbeidsInntektIdent = new ArbeidsInntektIdent();
        arbeidsInntektIdent.setIdent(aktoer);
        arbeidsInntektIdent.setArbeidsInntektMaaned(new ArrayList<>());

        YearMonth runningMonth = fom;
        while (runningMonth.isBefore(tom)) {
            ArbeidsInntektInformasjon arbeidsInntektInformasjon = makeArbeidsInntektInformasjonForMåned(modell, runningMonth,
                    filter82830);
            ArbeidsInntektMaaned arbeidsInntektMaaned = new ArbeidsInntektMaaned();
            arbeidsInntektMaaned.setArbeidsInntektInformasjon(arbeidsInntektInformasjon);
            arbeidsInntektMaaned.setAarMaaned(runningMonth);
            arbeidsInntektIdent.getArbeidsInntektMaaned().add(arbeidsInntektMaaned);
            runningMonth = runningMonth.plusMonths(1);
        }

        return arbeidsInntektIdent;
    }

    private static ArbeidsInntektInformasjon makeArbeidsInntektInformasjonForMåned(InntektskomponentModell modell, YearMonth måned,
                                                                                   boolean filter82830) {
        ArbeidsInntektInformasjon arbeidsInntektInformasjon = new ArbeidsInntektInformasjon();
        arbeidsInntektInformasjon
            .setArbeidsforholdListe(arbeidsforholdFrilanserListeFraModellListeForMåned(modell.getFrilansarbeidsforholdperioderSplittMånedlig(), måned));
        arbeidsInntektInformasjon.setInntektListe(inntektListeFraModell(modell.getInntektsperioderSplittMånedlig(), måned, filter82830));
        return arbeidsInntektInformasjon;
    }

    private static List<ArbeidsforholdFrilanser> arbeidsforholdFrilanserListeFraModellListeForMåned(List<FrilansArbeidsforholdsperiode> modellPeriode,
                                                                                                    YearMonth måned) {
        List<FrilansArbeidsforholdsperiode> frilansArbeidsforholdsperiodeList = modellPeriode.stream()
            .filter(t -> localDateTimeInYearMonth(t.frilansFom(), måned)).collect(Collectors.toList());
        return frilansArbeidsforholdsperiodeList.stream().map(temp -> {
            ArbeidsforholdFrilanser res = new ArbeidsforholdFrilanser();
            res.setFrilansPeriodeFom(temp.frilansFom());
            res.setFrilansPeriodeTom(temp.frilansTom());
            res.setArbeidsforholdstype(temp.getArbeidsforholdstype());
            res.setStillingsprosent((double) temp.stillingsprosent());
            Aktoer arbeidsgiver = temp.orgnr() != null && !temp.orgnr().equals("") ? Aktoer.newOrganisasjon(temp.orgnr())
                    : Aktoer.newAktoerId(temp.arbeidsgiver().getAktørIdent());
            res.setArbeidsgiver(arbeidsgiver);
            return res;
        }).collect(Collectors.toList());
    }

    private static List<Inntekt> inntektListeFraModell(List<Inntektsperiode> modellPeriode, YearMonth måned, boolean filter82830) {
        List<Inntektsperiode> inntektsperiodeList = modellPeriode.stream()
                .filter(t -> localDateTimeInYearMonth(t.tom(), måned))
                .filter(t -> taMedFraInntektsfilter(t, filter82830))
            .collect(Collectors.toList());
        return inntektsperiodeList.stream().map(temp -> {
            Inntekt inntekt = new Inntekt();
            inntekt.setInntektType(fraModellInntektstype(temp.inntektType()));
            inntekt.setBeloep(new BigDecimal(temp.beløp()));
            inntekt.setBeskrivelse(temp.beskrivelse());
            inntekt.setFordel(temp.inntektFordel().getKode());
            Aktoer arbeidsgiver = temp.orgnr() != null && !temp.orgnr().equals("") ? Aktoer.newOrganisasjon(temp.orgnr())
                : Aktoer.newAktoerId(temp.arbeidsgiver().getAktørIdent());
            inntekt.setVirksomhet(arbeidsgiver);
            inntekt.setOpptjeningsperiodeFom(temp.fom());
            inntekt.setOpptjeningsperiodeTom(temp.tom());
            inntekt.setUtbetaltIMaaned(måned);
            inntekt.setSkatteOgAvgiftsregel(temp.skatteOgAvgiftsregel());
            inntekt.setInngaarIGrunnlagForTrekk(temp.inngaarIGrunnlagForTrekk());
            inntekt.setUtloeserArbeidsgiveravgift(temp.utloeserArbeidsgiveravgift());
            return inntekt;
        }).collect(Collectors.toList());
    }

    private static boolean taMedFraInntektsfilter(Inntektsperiode p, boolean filter82830) {
        if (!filter82830 || p.inntektYtelseType() == null) {
            return true;
        }
        if (InntektYtelseType.InntektType.PENSJON_ELLER_TRYGD.equals(p.inntektYtelseType().getInntektType()) ||
                InntektYtelseType.InntektType.NÆRINGSINNTEKT.equals(p.inntektYtelseType().getInntektType())) {
            return false;
        }
        return Set.of(InntektYtelseType.SYKEPENGER, InntektYtelseType.SYKEPENGER_FISKER_HYRE, InntektYtelseType.FORELDREPENGER,
                InntektYtelseType.SVANGERSKAPSPENGER, InntektYtelseType.PLEIEPENGER, InntektYtelseType.OMSORGSPENGER,
                InntektYtelseType.OPPLÆRINGSPENGER).contains(p.inntektYtelseType());
    }

    private static boolean localDateTimeInYearMonth(LocalDate ldt, YearMonth yearMonth) {
        return YearMonth.of(ldt.getYear(), ldt.getMonth()).compareTo(yearMonth) == 0;
    }

    private static InntektType fraModellInntektstype(no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektType modellType) {
        if (modellType.equals(no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektType.LØNNSINNTEKT)) {
            return InntektType.LOENNSINNTEKT;
        }
        if (modellType.equals(no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektType.NÆRINGSINNTEKT)) {
            return InntektType.NAERINGSINNTEKT;
        }
        if (modellType.equals(no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektType.PENSJON_ELLER_TRYGD)) {
            return InntektType.PENSJON_ELLER_TRYGD;
        }
        if (modellType.equals(no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektType.YTELSE_FRA_OFFENTLIGE)) {
            return InntektType.YTELSE_FRA_OFFENTLIGE;
        }
        throw new IllegalStateException("Inntektstype kunne ikke konverteres: " + modellType.getKode());
    }

}
