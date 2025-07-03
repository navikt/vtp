package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InntektskomponentModellTest {

    @Test
    void skal_ikke_splitte_en_full_måned() {

        var modell = new InntektskomponentModell(
                List.of(new Inntektsperiode(LocalDate.of(2023, 10, 1), LocalDate.of(2023, 10, 31), null, 1000, "123456789",
                        InntektType.LØNNSINNTEKT, InntektFordel.KONTANTYTELSE, "beskrivelse", null, null, true, true, null)),
                List.of());

        var perioder = modell.getInntektsperioderSplittMånedlig();

        assertEquals(1, perioder.size(), "Skal ikke splitte full måned");
        assertEquals(LocalDate.of(2023, 10, 1), perioder.get(0).fom(), "FOM skal være 1. oktober");
        assertEquals(LocalDate.of(2023, 10, 31), perioder.get(0).tom(), "TOM skal være 31. oktober");
    }

    @Test
    void skal_splitte_to_måneder_i_samme_periode() {

        var modell = new InntektskomponentModell(
                List.of(new Inntektsperiode(LocalDate.of(2023, 10, 1), LocalDate.of(2023, 11, 15), null, 1000, "123456789",
                        InntektType.LØNNSINNTEKT, InntektFordel.KONTANTYTELSE, "beskrivelse", null, null, true, true, null)),
                List.of());

        var perioder = modell.getInntektsperioderSplittMånedlig();

        assertEquals(2, perioder.size(), "Skal splitte to måneder i samme periode");
        assertEquals(LocalDate.of(2023, 10, 1), perioder.get(0).fom(), "FOM skal være 1. oktober");
        assertEquals(LocalDate.of(2023, 10, 31), perioder.get(0).tom(), "TOM skal være 31. oktober");

        assertEquals(LocalDate.of(2023, 11, 1), perioder.get(1).fom(), "FOM skal være 1. november");
        assertEquals(LocalDate.of(2023, 11, 30), perioder.get(1).tom(), "TOM skal være 30. november");
    }

    @Test
    void skal_splitte_to_innslag_av_to_måneder_i_samme_periode() {

        var modell = new InntektskomponentModell(
                List.of(new Inntektsperiode(LocalDate.of(2023, 8, 1), LocalDate.of(2023, 9, 20), null, 1000, "123456789",
                                InntektType.LØNNSINNTEKT, InntektFordel.KONTANTYTELSE, "beskrivelse", null, null, true, true, null),
                        new Inntektsperiode(LocalDate.of(2023, 10, 1), LocalDate.of(2023, 11, 15), null, 1000, "123456789",
                                InntektType.LØNNSINNTEKT, InntektFordel.KONTANTYTELSE, "beskrivelse", null, null, true, true, null)),
                List.of());

        var perioder = modell.getInntektsperioderSplittMånedlig();

        assertEquals(4, perioder.size(), "Skal splitte to måneder i samme periode");
        assertEquals(LocalDate.of(2023, 8, 1), perioder.get(0).fom(), "FOM skal være 1. august");
        assertEquals(LocalDate.of(2023, 8, 31), perioder.get(0).tom(), "TOM skal være 31. august");

        assertEquals(LocalDate.of(2023, 9, 1), perioder.get(1).fom(), "FOM skal være 1. september");
        assertEquals(LocalDate.of(2023, 9, 30), perioder.get(1).tom(), "TOM skal være 30. september");

        assertEquals(LocalDate.of(2023, 10, 1), perioder.get(2).fom(), "FOM skal være 1. oktober");
        assertEquals(LocalDate.of(2023, 10, 31), perioder.get(2).tom(), "TOM skal være 31. oktober");

        assertEquals(LocalDate.of(2023, 11, 1), perioder.get(3).fom(), "FOM skal være 1. november");
        assertEquals(LocalDate.of(2023, 11, 30), perioder.get(3).tom(), "TOM skal være 30. november");
    }
}
