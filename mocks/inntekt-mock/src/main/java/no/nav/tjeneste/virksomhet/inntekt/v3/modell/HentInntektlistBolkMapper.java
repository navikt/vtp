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
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent.InntektType;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent.Inntektsperiode;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.ArbeidsInntektIdent;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.ArbeidsInntektInformasjon;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.ArbeidsInntektMaaned;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.Inntekt;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.Loennsbeskrivelse;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.Loennsinntekt;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.Organisasjon;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.Periode;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.PersonIdent;

public class HentInntektlistBolkMapper {


    public static ArbeidsInntektIdent makeArbeidsInntektIdent(InntektskomponentModell modell, String fnr) {
        //TODO: OL: Gjør refaktorering når implemnterer støtte av Frilansperiode. Generering av måneder mellom A - B bør gjøres i lasting av modell, ikke når mappes til WS-response.
        ArbeidsInntektIdent arbeidsInntektIdent = new ArbeidsInntektIdent();
        arbeidsInntektIdent.setIdent(makePersonIdent(fnr));
        List<Inntekt> inntektsliste = mapInntektFraModell(modell.getInntektsperioder(), fnr);
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


    public static List<Inntekt> mapInntektFraModell(List<Inntektsperiode> modellList, String fnr) {
        List<Inntekt> inntektListe = new ArrayList<>();

        for (Inntektsperiode ip : modellList) {
            if (ip.getType().equals(InntektType.LØNNSINNTEKT)) {
                Loennsinntekt inntekt = lagLoennsinntekt(ip, fnr);
                inntektListe.add(inntekt);
            } else if (ip.getType().equals(InntektType.NÆRINGSINNTEKT)) {
                throw new UnsupportedOperationException("Ikke implementert ennå");
            } else if (ip.getType().equals(InntektType.PENSJON_ELLER_TRYGD)) {
                throw new UnsupportedOperationException("Ikke implementert ennå");
            } else if (ip.getType().equals(InntektType.YTELSE_FRA_OFFENTLIGE)) {
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
        loennsinntekt.setInntektsmottaker(makePersonIdent(fnr));
        loennsinntekt.setVirksomhet(makeOrganisation(ip.getOrgnr()));
        loennsinntekt.setOpplysningspliktig(makeOrganisation(ip.getOrgnr()));
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

    private static Organisasjon makeOrganisation(String orgNummer) {
        Organisasjon organisasjon = new Organisasjon();
        organisasjon.setOrgnummer(orgNummer);
        return organisasjon;
    }

    private static PersonIdent makePersonIdent(String fnr) {
        PersonIdent personIdent = new PersonIdent();
        personIdent.setPersonIdent(fnr);
        return personIdent;
    }

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


    /*

                        <inntektsmottaker xsi:type="ns4:PersonIdent">
                           <personIdent>26049726650</personIdent>
                        </inntektsmottaker>
                        <informasjonsstatus>InngaarAlltid</informasjonsstatus>

     */


}
