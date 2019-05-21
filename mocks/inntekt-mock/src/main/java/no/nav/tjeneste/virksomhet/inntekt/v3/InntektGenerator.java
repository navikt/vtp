package no.nav.tjeneste.virksomhet.inntekt.v3;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import no.nav.foreldrepenger.fpmock2.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.Aktoer;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.ArbeidsInntektIdent;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.ArbeidsInntektInformasjon;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.ArbeidsInntektMaaned;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.Inntekt;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.ObjectFactory;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.Organisasjon;

/**
 * Genererer tilfeldige inntekter til testformål. BrukerModell random generator til
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
        arbeidsInntektIdent.getArbeidsInntektMaaned().add(lagarbeidsInntektMaaned(fom, aktoer));
        return arbeidsInntektIdent;
    }

    private ArbeidsInntektMaaned lagarbeidsInntektMaaned(LocalDate fom, Aktoer aktoer) {
        ArbeidsInntektMaaned arbeidsInntektMaaned = objectFactory.createArbeidsInntektMaaned();
        arbeidsInntektMaaned.setAarMaaned(ConversionUtils.convertToXMLGregorianCalendar(fom));
        arbeidsInntektMaaned.setArbeidsInntektInformasjon(lagArbeidsinntektInformasjon(fom, aktoer));
        return arbeidsInntektMaaned;
    }

    private ArbeidsInntektInformasjon lagArbeidsinntektInformasjon(LocalDate fom, Aktoer aktoer) {
        ArbeidsInntektInformasjon arbeidsInntektInformasjon = objectFactory.createArbeidsInntektInformasjon();
        arbeidsInntektInformasjon.getInntektListe().add(lagInntekt(fom, aktoer));
        return arbeidsInntektInformasjon;
    }

    private Inntekt lagInntekt(LocalDate fom, Aktoer aktoer) {
        Inntekt inntekt = objectFactory.createInntekt();
        inntekt.setBeloep(BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(MIN_INNTEKT, MAX_INNTEKT)));
        inntekt.setUtbetaltIPeriode(ConversionUtils.convertToXMLGregorianCalendar(fom));
        // Midlertidig løsning for å få et gyldig org.nummer som matcher AAREG-mock. Før brukte man ident/fnr.
        Organisasjon utbetaler = objectFactory.createOrganisasjon();
        utbetaler.setOrgnummer("976037286");
        inntekt.setVirksomhet(utbetaler);
        return inntekt;
    }
}
