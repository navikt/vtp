package no.nav.tjeneste.virksomhet.inntekt.v3;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import no.nav.foreldrepenger.mock.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.Aktoer;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.ArbeidsInntektIdent;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.ArbeidsInntektInformasjon;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.ArbeidsInntektMaaned;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.Inntekt;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.ObjectFactory;

/**
 * Genererer tilfeldige inntekter til testformål. Bruker random generator til
 * å lage en månedsinntekt mellom 0 og 100 tusen for hver måned mellom fom og tom.
 */
public class InntektGenerator {

    private ObjectFactory objectFactory = new ObjectFactory();
    private final int MIN_INNTEKT = 0;
    private final int MAX_INNTEKT = 100000;

    public List<ArbeidsInntektIdent> hentInntekter(Aktoer aktoer, LocalDate fom, LocalDate tom) {
        List<ArbeidsInntektIdent> returliste = new ArrayList<>();
        LocalDate curMonth = fom;
        while (curMonth.isBefore(tom)) {
            returliste.add(lagArbeidsinntektIdent(aktoer, curMonth));
            curMonth = curMonth.plusMonths(1);
        }
        return returliste;
    }

    private ArbeidsInntektIdent lagArbeidsinntektIdent(Aktoer aktoer, LocalDate fom) {
        ArbeidsInntektIdent arbeidsInntektIdent = objectFactory.createArbeidsInntektIdent();
        arbeidsInntektIdent.setIdent(aktoer);
        arbeidsInntektIdent.getArbeidsInntektMaaned().add(lagarbeidsInntektMaaned(fom));
        return arbeidsInntektIdent;
    }

    private ArbeidsInntektMaaned lagarbeidsInntektMaaned(LocalDate fom) {
        ArbeidsInntektMaaned arbeidsInntektMaaned = objectFactory.createArbeidsInntektMaaned();
        arbeidsInntektMaaned.setAarMaaned(ConversionUtils.convertToXMLGregorianCalendar(fom));
        arbeidsInntektMaaned.setArbeidsInntektInformasjon(lagArbeidsinntektInformasjon(fom));
        return arbeidsInntektMaaned;
    }

    private ArbeidsInntektInformasjon lagArbeidsinntektInformasjon(LocalDate fom) {
        ArbeidsInntektInformasjon arbeidsInntektInformasjon = objectFactory.createArbeidsInntektInformasjon();
        arbeidsInntektInformasjon.getInntektListe().add(lagInntekt(fom));
        return arbeidsInntektInformasjon;
    }

    private Inntekt lagInntekt(LocalDate fom) {
        Inntekt inntekt = objectFactory.createInntekt();
        inntekt.setBeloep(BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(MIN_INNTEKT, MAX_INNTEKT)));
        inntekt.setUtbetaltIPeriode(ConversionUtils.convertToXMLGregorianCalendar(fom));
        return inntekt;
    }
}
