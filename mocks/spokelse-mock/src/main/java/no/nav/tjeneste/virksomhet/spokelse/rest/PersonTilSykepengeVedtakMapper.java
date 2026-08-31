package no.nav.tjeneste.virksomhet.spokelse.rest;

import java.time.LocalDate;
import java.util.List;

import no.nav.tjeneste.virksomhet.spokelse.rest.SykepengeVedtak.SykepengeUtbetaling;
import no.nav.vtp.person.Person;
import no.nav.vtp.person.ytelse.LegacyKilde;
import no.nav.vtp.person.ytelse.Ytelse;
import no.nav.vtp.person.ytelse.YtelseType;

public class PersonTilSykepengeVedtakMapper {

    private PersonTilSykepengeVedtakMapper() {
    }

    public static List<SykepengeVedtak> tilSykepengeVedtak(Person person, LocalDate fom) {
        if (person == null || person.ytelser() == null) {
            return List.of();
        }
        return person.ytelser().stream()
                .filter(ytelse -> ytelse.ytelse() == YtelseType.SYKEPENGER)
                // SpøkelseMock er v2-kilden for sykepenger. Eksplisitte gamle kilder (v1) skal
                // routes til Infotrygd-mocken i stedet, for å unngå dobbelttelling hos fp-abakus.
                .filter(ytelse -> ytelse.kilde() != LegacyKilde.INFOTRYGD)
                .filter(ytelse -> erRelevantForFom(ytelse, fom))
                .map(PersonTilSykepengeVedtakMapper::tilVedtak)
                .toList();
    }


    private static boolean erRelevantForFom(Ytelse ytelse, LocalDate fom) {
        return fom == null || ytelse.tom() == null || !ytelse.tom().isBefore(fom);
    }

    private static SykepengeVedtak tilVedtak(Ytelse ytelse) {
        var utbetaling = new SykepengeUtbetaling(ytelse.fom(), ytelse.tom(), tilGrad(ytelse));
        // Vedtaksreferanse har ingen semantisk betydning for testdata - trenger bare å være unik nok
        // til at konsumenter kan skille vedtak fra hverandre. Bruker fom som grunnlag for determinisme.
        var vedtaksreferanse = ("SPOKELSE" + ytelse.fom()).replace("-", "");
        var vedtattTidspunkt = ytelse.fom() != null ? ytelse.fom().atStartOfDay() : null;
        return new SykepengeVedtak(vedtaksreferanse, List.of(utbetaling), vedtattTidspunkt);
    }

    private static java.math.BigDecimal tilGrad(Ytelse ytelse) {
        // Default 100% hvis ikke satt i testdata - ingen kjent test varierer denne i dag.
        var grad = ytelse.utbetalingsgrad() != null ? ytelse.utbetalingsgrad() : 100;
        return java.math.BigDecimal.valueOf(grad);
    }
}
