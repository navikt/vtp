package no.nav.tjeneste.virksomhet.inntekt.v3.modell;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.nav.foreldrepenger.fpmock2.felles.ConversionUtils;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent.FrilansArbeidsforholdsperiode;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent.InntektType;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent.Inntektsperiode;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.AapenPeriode;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.ArbeidsInntektIdent;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.ArbeidsInntektInformasjon;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.ArbeidsInntektMaaned;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.ArbeidsforholdFrilanser;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.Inntekt;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.Loennsbeskrivelse;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.Loennsinntekt;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.Organisasjon;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.Periode;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.PersonIdent;

public class HentInntektlistBolkMapper {


    public static ArbeidsInntektIdent makeArbeidsInntektIdent(InntektskomponentModell modell, String fnr) {
        ArbeidsInntektIdent arbeidsInntektIdent = new ArbeidsInntektIdent();
        arbeidsInntektIdent.setIdent(lagPersonIdent(fnr));
        List<Inntekt> inntektsliste = mapInntektFraModell(modell.getInntektsperioder(), fnr);
        List<ArbeidsforholdFrilanser> arbeidsforholdFrilansereListe = mapArbeidsforholdFrilansFraModell(modell.getFrilansarbeidsforholdperioder(), fnr);
        Map<String, List<Inntekt>> inntektsMåneder = new HashMap<>();

        for (Inntekt inntekt : inntektsliste) {
            LocalDate localDate = ConversionUtils.convertToLocalDate(inntekt.getOpptjeningsperiode().getStartDato());
            String key = "" + localDate.getYear() + "-" + localDate.getMonth();
            if (!inntektsMåneder.containsKey(key)) {
                List<Inntekt> inntektMåned = new ArrayList<>();
                inntektMåned.add(inntekt);
                inntektsMåneder.put(key, inntektMåned);
            } else {
                inntektsMåneder.get(key).add(inntekt);
            }
        }

        for (LocalDateTime monthYearDate : getMonthYearsWithData(modell)) {
            ArbeidsInntektMaaned arbeidsInntektMaaned = new ArbeidsInntektMaaned();
            ArbeidsInntektInformasjon arbeidsInntektInformasjon = new ArbeidsInntektInformasjon();
            arbeidsInntektMaaned.setArbeidsInntektInformasjon(arbeidsInntektInformasjon);
            arbeidsInntektMaaned.setAarMaaned(ConversionUtils.convertToXMLGregorianCalendar(monthYearDate));
            String key = "" + monthYearDate.getYear() + "-" + monthYearDate.getMonth();
            boolean leggTil = false;
            if (inntektsMåneder.containsKey(key)) {
                arbeidsInntektInformasjon.getInntektListe().addAll(inntektsMåneder.get(key));
                leggTil = true;
            }
            if (leggTil) {
                arbeidsInntektIdent.getArbeidsInntektMaaned().add(arbeidsInntektMaaned);
            }
        }
        return arbeidsInntektIdent;
    }

    private static List<ArbeidsforholdFrilanser> mapArbeidsforholdFrilansFraModell(List<FrilansArbeidsforholdsperiode> modellPeriodeListe, String fnr) {
        List<ArbeidsforholdFrilanser> arbeidsforholdliste = new ArrayList<>();

        for(FrilansArbeidsforholdsperiode modellPeriode: modellPeriodeListe){
            ArbeidsforholdFrilanser arbeidsforhold = new ArbeidsforholdFrilanser();

            arbeidsforhold.setFrilansPeriode(lagÅpenPeriode(modellPeriode.getFrilansFom(),modellPeriode.getFrilansTom()));
            arbeidsforhold.setArbeidsgiver(lagOrganisation(modellPeriode.getOrgnr()));
            if(modellPeriode.getStillingsprosent() != null){
                arbeidsforhold.setStillingsprosent(new BigDecimal(modellPeriode.getStillingsprosent()));

            }
        }

        return arbeidsforholdliste;
    }


    private static List<Inntekt> mapInntektFraModell(List<Inntektsperiode> modellList, String fnr) {
        List<Inntekt> inntektListe = new ArrayList<>();

        for (Inntektsperiode modellPeriode : modellList) {
            if (modellPeriode.getType().equals(InntektType.LØNNSINNTEKT)) {
                Loennsinntekt inntekt = lagLoennsinntekt(modellPeriode, fnr);
                inntektListe.add(inntekt);
            } else if (modellPeriode.getType().equals(InntektType.NÆRINGSINNTEKT)) {
                throw new UnsupportedOperationException("Ikke implementert ennå");
            } else if (modellPeriode.getType().equals(InntektType.PENSJON_ELLER_TRYGD)) {
                throw new UnsupportedOperationException("Ikke implementert ennå");
            } else if (modellPeriode.getType().equals(InntektType.YTELSE_FRA_OFFENTLIGE)) {
                throw new UnsupportedOperationException("Ikke implementert ennå");
            }
        }
        return inntektListe;
    }


    private static Loennsinntekt lagLoennsinntekt(Inntektsperiode ip, String fnr) {
        Loennsinntekt loennsinntekt = new Loennsinntekt();
        loennsinntekt.setBeloep(new BigDecimal(ip.getBeløp()));
        Loennsbeskrivelse loennsbeskrivelse = new Loennsbeskrivelse();
        loennsbeskrivelse.setValue(ip.getBeskrivelse());
        loennsinntekt.setBeskrivelse(loennsbeskrivelse);
        loennsinntekt.setUtloeserArbeidsgiveravgift(true);
        loennsinntekt.setInngaarIGrunnlagForTrekk(true);
        loennsinntekt.setInntektsmottaker(lagPersonIdent(fnr));
        loennsinntekt.setVirksomhet(lagOrganisation(ip.getOrgnr()));
        loennsinntekt.setOpplysningspliktig(lagOrganisation(ip.getOrgnr()));
        loennsinntekt.setUtbetaltIPeriode(ConversionUtils.convertToXMLGregorianCalendar(ip.getFom()));
        //loennsinntekt.setLevereringstidspunkt();
        //loennsinntekt.setInntektsstatus();
        //loennsinntekt.setInntektsperiodetype();
        //loennsinntekt.setInntektskilde();
        //loennsinntekt.setFordel();
        Periode periode = new Periode();
        periode.setStartDato(ConversionUtils.convertToXMLGregorianCalendar(ip.getFom()));
        periode.setSluttDato(ConversionUtils.convertToXMLGregorianCalendar(ip.getTom()));
        loennsinntekt.setOpptjeningsperiode(periode);

        return loennsinntekt;
    }

    private static Organisasjon lagOrganisation(String orgNummer) {
        Organisasjon organisasjon = new Organisasjon();
        organisasjon.setOrgnummer(orgNummer);
        return organisasjon;
    }

    private static PersonIdent lagPersonIdent(String fnr) {
        PersonIdent personIdent = new PersonIdent();
        personIdent.setPersonIdent(fnr);
        return personIdent;
    }

    private static AapenPeriode lagÅpenPeriode(LocalDateTime fom, LocalDateTime tom){
        AapenPeriode åpenPeriode = new AapenPeriode();
        if(fom != null){
            åpenPeriode.setFom(ConversionUtils.convertToXMLGregorianCalendar(fom));
        }
        if(tom != null) {
            åpenPeriode.setTom(ConversionUtils.convertToXMLGregorianCalendar(tom));
        }
        return åpenPeriode;
    }


    //TODO: Skriv om til å både inntekt og arbeidsforhold
    private static List<LocalDateTime> getMonthYearsWithData(InntektskomponentModell modell) {
        if (modell.getInntektsperioder().size() == 0) {
            return new ArrayList<>();
        }
        LocalDateTime minDate = getMinimumMonth(modell);
        LocalDateTime maxDate = getMaximumMonth(modell);
        List<LocalDateTime> monthList = new ArrayList<>();

        for (LocalDateTime date = minDate; date.isBefore(maxDate); date = date.plusMonths(1)) {
            monthList.add(date);
        }
        return monthList;
    }

    private static LocalDateTime getMinimumMonth(InntektskomponentModell modell) {
        return modell.getInntektsperioder().stream().min(Comparator.comparing(t -> t.getFom())).get().getFom();
    }

    private static LocalDateTime getMaximumMonth(InntektskomponentModell modell) {
        return modell.getInntektsperioder().stream().max(Comparator.comparing(t -> t.getTom())).get().getTom();
    }


}
