package no.nav.vtp;


import java.util.List;

import no.nav.vtp.arbeidsforhold.Arbeidsforhold;
import no.nav.vtp.inntekt.Inntektsperiode;
import no.nav.vtp.personopplysninger.Personopplysninger;
import no.nav.vtp.ytelse.Ytelse;

public record Person(Personopplysninger personopplysninger,
                     List<Arbeidsforhold> arbeidsforhold,
                     List<Inntektsperiode> inntekt,
                     List<Ytelse> ytelser) {
}
