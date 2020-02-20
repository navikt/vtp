package no.nav.vtp.hentinntektlistebolk.modell;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.FrilansArbeidsforholdsperiode;
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

    public static ArbeidsInntektIdent makeArbeidsInntektIdent(InntektskomponentModell modell, Aktoer aktoer, YearMonth fom, YearMonth tom) {
        ArbeidsInntektIdent arbeidsInntektIdent = new ArbeidsInntektIdent();
        arbeidsInntektIdent.setIdent(aktoer);
        arbeidsInntektIdent.setArbeidsInntektMaaned(new ArrayList<>());

        YearMonth runningMonth = fom;
        while (runningMonth.isBefore(tom)) {
            ArbeidsInntektInformasjon arbeidsInntektInformasjon = makeArbeidsInntektInformasjonForMåned(modell, runningMonth);
            ArbeidsInntektMaaned arbeidsInntektMaaned = new ArbeidsInntektMaaned();
            arbeidsInntektMaaned.setArbeidsInntektInformasjon(arbeidsInntektInformasjon);
            arbeidsInntektMaaned.setAarMaaned(runningMonth);
            arbeidsInntektIdent.getArbeidsInntektMaaned().add(arbeidsInntektMaaned);
            runningMonth = runningMonth.plusMonths(1);
        }

        return arbeidsInntektIdent;
    }

    private static ArbeidsInntektInformasjon makeArbeidsInntektInformasjonForMåned(InntektskomponentModell modell, YearMonth måned) {
        ArbeidsInntektInformasjon arbeidsInntektInformasjon = new ArbeidsInntektInformasjon();
        arbeidsInntektInformasjon
            .setArbeidsforholdListe(arbeidsforholdFrilanserListeFraModellListeForMåned(modell.getFrilansarbeidsforholdperioderSplittMånedlig(), måned));
        arbeidsInntektInformasjon.setInntektListe(inntektListeFraModell(modell.getInntektsperioderSplittMånedlig(), måned));
        return arbeidsInntektInformasjon;
    }

    private static List<ArbeidsforholdFrilanser> arbeidsforholdFrilanserListeFraModellListeForMåned(List<FrilansArbeidsforholdsperiode> modellPeriode,
                                                                                                    YearMonth måned) {
        List<FrilansArbeidsforholdsperiode> frilansArbeidsforholdsperiodeList = modellPeriode.stream()
            .filter(t -> localDateTimeInYearMonth(t.getFrilansFom(), måned)).collect(Collectors.toList());
        return frilansArbeidsforholdsperiodeList.stream().map(temp -> {
            ArbeidsforholdFrilanser res = new ArbeidsforholdFrilanser();
            res.setFrilansPeriodeFom(temp.getFrilansFom());
            res.setFrilansPeriodeTom(temp.getFrilansTom());
            res.setArbeidsforholdstype(temp.getArbeidsforholdstype());
            res.setStillingsprosent((double) temp.getStillingsprosent());
            Aktoer arbeidsgiver = temp.getOrgnr() != null && !temp.getOrgnr().equals("") ? Aktoer.newOrganisasjon(temp.getOrgnr())
                    : Aktoer.newAktoerId(temp.getPersonligArbeidsgiver().getAktørIdent());
            res.setArbeidsgiver(arbeidsgiver);
            return res;
        }).collect(Collectors.toList());
    }

    private static List<Inntekt> inntektListeFraModell(List<Inntektsperiode> modellPeriode, YearMonth måned) {
        List<Inntektsperiode> inntektsperiodeList = modellPeriode.stream().filter(t -> localDateTimeInYearMonth(t.getTom(), måned))
            .collect(Collectors.toList());
        return inntektsperiodeList.stream().map(temp -> {
            Inntekt inntekt = new Inntekt();
            inntekt.setInntektType(fraModellInntektstype(temp.getType()));
            inntekt.setBeloep(new BigDecimal(temp.getBeløp()));
            inntekt.setBeskrivelse(temp.getBeskrivelse());
            inntekt.setFordel(temp.getFordel().getKode());
            Aktoer arbeidsgiver = temp.getOrgnr() != null && !temp.getOrgnr().equals("") ? Aktoer.newOrganisasjon(temp.getOrgnr())
                : Aktoer.newAktoerId(temp.getPersonligArbeidsgiver().getAktørIdent());
            inntekt.setVirksomhet(arbeidsgiver);
            inntekt.setOpptjeningsperiodeFom(temp.getFom());
            inntekt.setOpptjeningsperiodeTom(temp.getTom());
            inntekt.setUtbetaltIMaaned(måned);
            inntekt.setSkatteOgAvgiftsregel(temp.getSkatteOgAvgiftsregel());
            inntekt.setInngaarIGrunnlagForTrekk(temp.getInngaarIGrunnlagForTrekk());
            inntekt.setUtloeserArbeidsgiveravgift(temp.getUtloeserArbeidsgiveravgift());
            return inntekt;
        }).collect(Collectors.toList());
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
