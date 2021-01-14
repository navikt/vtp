package no.nav.foreldrepenger.vtp.testmodell.jackson;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.AntallTimerIPerioden;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Arbeidsavtale;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Arbeidsforhold;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.ArbeidsforholdModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Arbeidsforholdstype;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Avlønningstype;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Permisjon;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Permisjonstype;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Yrke;


class ArbeidsforholdSeraliseringDeseraliseringsTest extends TestscenarioSerializationTestBase {

    @Test
    public void AntallTimerIPeriodenSeraliseringDeseraliseringTest() {
        test(lagAntallTimerIPerioden());
    }

    @Test
    public void YrkeSeraliseringDeseraliseringTest() {
        test(lagYrke());
    }

    @Test
    public void AvlønningstypeSeraliseringDeseraliseringTest() {
        test(Avlønningstype.FASTLØNN);
    }

    @Test
    public void ArbeidsavtaleSeraliseringDeseraliseringTest() {
        test(lagArbeidsavtale());
    }

    @Test
    public void PermisjonstypeSeraliseringDeseraliseringTest() {
        test(Permisjonstype.PERMISJON_VED_MILITÆRTJENESTE);
    }

    @Test
    public void PermisjonSeraliseringDeseraliseringTest() {
        test(lagPermisjon());
    }

    @Test
    public void ArbeidsforholdstypeSeraliseringDeseraliseringTest() {
        test(Arbeidsforholdstype.MARITIMT_ARBEIDSFORHOLD);
    }

    @Test
    public void ArbeidsforholdSeraliseringDeseraliseringTest() {
        test(lagArbeidsforhold());
    }

    @Test
    public void ArbeidsforholdModellSeraliseringDeseraliseringTest() {
        test(lagArbeidsforholdModell());
    }

    protected ArbeidsforholdModell lagArbeidsforholdModell() {
        return new ArbeidsforholdModell(List.of(lagArbeidsforhold()));
    }


    private Arbeidsforhold lagArbeidsforhold() {
        return new Arbeidsforhold(List.of(lagArbeidsavtale()), List.of(lagPermisjon()), "123456789",
                10000L, LocalDate.now(), LocalDate.now(), Arbeidsforholdstype.MARITIMT_ARBEIDSFORHOLD,
                List.of(lagAntallTimerIPerioden()), "90807060", "90807061", "99123456789");
    }

    private Permisjon lagPermisjon() {
        return new Permisjon(100, LocalDate.now(), LocalDate.now(), Permisjonstype.PERMITTERING);
    }

    private Arbeidsavtale lagArbeidsavtale() {
        return new Arbeidsavtale(lagYrke(), Avlønningstype.FASTLØNN, 38, 100,
                35, LocalDate.now(), LocalDate.now(), LocalDate.now());
    }

    private AntallTimerIPerioden lagAntallTimerIPerioden() {
        return new AntallTimerIPerioden(45, LocalDate.now());
    }

    private Yrke lagYrke() {
        return new Yrke("SNEKKER");
    }
}
