package no.nav.tjeneste.virksomhet.inntekt.v3.modell;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent.InntektType;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent.Inntektsperiode;
import no.nav.tjeneste.virksomhet.inntekt.v3.ObjectFactory;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.Inntekt;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.Loennsinntekt;

public class HentInntektlistBolkMapper {
    private ObjectFactory objectFactory = new ObjectFactory();

    public List<Inntekt> mapFraModell(List<Inntektsperiode> modellList) {
        List<Inntekt> inntektListe = new ArrayList<>();

        for (Inntektsperiode ip : modellList) {
            for (Inntektsperiode ipPerMnd : getSplitInntektsperiodeToMonthly(ip))
                if (ip.getType().equals(InntektType.LØNNSINNTEKT)) {
                    Loennsinntekt inntekt = lagLoennsinntekt(ip);
                    inntektListe.add(inntekt);
                } else if (ip.getType().equals(InntektType.NÆRINGSINNTEKT)) {
                    //Lag Næringsinntekt
                } else if (ip.getType().equals(InntektType.PENSJON_ELLER_TRYGD)) {
                    // Lag pensjonEllerTrygd
                } else if (ip.getType().equals(InntektType.YTELSE_FRA_OFFENTLIGE)) {
                    //Lag ytelseFraOffetlige
                }

        }
           return inntektListe;
    }

    private List<Inntektsperiode> getSplitInntektsperiodeToMonthly(Inntektsperiode ip) {
        List<Inntektsperiode> inntektsperioderPaaMaaned = new ArrayList<>();
        Stream.iterate(ip.getFom(), d -> d.plusMonths(1))
                .limit(ChronoUnit.MONTHS.between(ip.getFom(), ip.getTom()) + 1).forEach(p -> {
            LocalDate init = LocalDate.of(p.getYear(), p.getMonth(), p.getDayOfMonth());
            inntektsperioderPaaMaaned.add(new Inntektsperiode(init.withDayOfMonth(1).atStartOfDay()
                    , init.withDayOfMonth(init.lengthOfMonth()).atStartOfDay(),
                    ip.getBeløp(), ip.getOrgnummer(), ip.getType(), ip.getFordel(), ip.getBeskrivelse()));

        });
        return inntektsperioderPaaMaaned;
    }

    private Loennsinntekt lagLoennsinntekt(Inntektsperiode ip) {
        Loennsinntekt loennsinntekt = new Loennsinntekt();
        return loennsinntekt;
    }


}
