package no.nav.tjeneste.virksomhet.inntekt.v3.modell;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;

import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell;
import no.nav.tjenester.aordningen.inntektsinformasjon.Aktoer;
import no.nav.tjenester.aordningen.inntektsinformasjon.ArbeidsInntektIdent;
import no.nav.tjenester.aordningen.inntektsinformasjon.ArbeidsInntektInformasjon;
import no.nav.tjenester.aordningen.inntektsinformasjon.ArbeidsInntektMaaned;


public class HentInntektlisteBolkMapperRest {


    public static ArbeidsInntektIdent makeArbeidsInntektIdent(InntektskomponentModell modell, Aktoer aktoer, YearMonth fom, YearMonth tom) {
        ArbeidsInntektIdent arbeidsInntektIdent = new ArbeidsInntektIdent();
        arbeidsInntektIdent.setIdent(aktoer);
        //List<Inntekt> inntektsliste = mapInntektFraModell(modell.getInntektsperioder(), aktoer);
        //List<ArbeidsforholdFrilanser> arbeidsforholdFrilansereListe = mapArbeidsforholdFrilansFraModell(modell.getFrilansarbeidsforholdperioder(), aktoer);

        YearMonth runningMonth = fom;
        while (runningMonth.isBefore(tom)) {
            ArbeidsInntektInformasjon arbeidsInntektInformasjon = makeArbeidsInntektInformasjon();
            ArbeidsInntektMaaned arbeidsInntektMaaned = new ArbeidsInntektMaaned();
            arbeidsInntektMaaned.setArbeidsInntektInformasjon(arbeidsInntektInformasjon);
            arbeidsInntektIdent.setArbeidsInntektMaaned(new ArrayList(Arrays.asList(arbeidsInntektMaaned)));

            System.out.println(runningMonth.toString());
            runningMonth = runningMonth.plusMonths(1);
        }

        return arbeidsInntektIdent;

    }

    private static ArbeidsInntektInformasjon makeArbeidsInntektInformasjon(){
        return new ArbeidsInntektInformasjon();
    }


}
