package no.nav.foreldrepenger.vtp.server.api.scenario.mapper;

import no.nav.foreldrepenger.vtp.kontrakter.v2.ArbeidsavtaleDto;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Arbeidsavtale;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Avlønningstype;

import java.util.List;

import static no.nav.foreldrepenger.fpwsproxy.UtilKlasse.safeStream;

class ArbeidsavtaleMapper {
    private ArbeidsavtaleMapper() {
        // sonar
    }

    public static List<Arbeidsavtale> tilArbeidsavtaler(List<ArbeidsavtaleDto> arbeidsavtaler) {
        return safeStream(arbeidsavtaler)
                .map(ArbeidsavtaleMapper::tilArbeidsavtale)
                .toList();
    }

    private static Arbeidsavtale tilArbeidsavtale(ArbeidsavtaleDto a) {
        return new Arbeidsavtale(
                null,
                Avlønningstype.FASTLØNN,
                a.avtaltArbeidstimerPerUke(),
                a.stillingsprosent(),
                a.beregnetAntallTimerPerUke(),
                a.sisteLønnsendringsdato(),
                a.fomGyldighetsperiode(),
                a.tomGyldighetsperiode()
        );
    }
}
