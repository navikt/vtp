package no.nav.foreldrepenger.vtp.testmodell.jackson;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.vtp.testmodell.identer.FiktiveFnr;
import no.nav.foreldrepenger.vtp.testmodell.identer.LokalIdentIndeks;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektFordel;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektType;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektYtelseType;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.Inntektsperiode;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.AdresseIndeks;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonArbeidsgiver;
import no.nav.foreldrepenger.vtp.testmodell.util.JacksonObjectMapperTestscenarioUtvider;
import no.nav.foreldrepenger.vtp.testmodell.util.VariabelContainer;

class InntektskomponentSeraliseringDeseraliseringsTest extends TestscenarioSerializationTestBase {

    @BeforeAll
    static void utvidObjectmapperMedInjectables() {
        var adresseIndeks = new AdresseIndeks();
        var mapperExtension = new JacksonObjectMapperTestscenarioUtvider(new VariabelContainer(Map.of("key", "value")));
        var lokalIdentIndeks = new LokalIdentIndeks("12345", new FiktiveFnr());
        mapperExtension.addInjectable(LokalIdentIndeks.class, lokalIdentIndeks);
        mapperExtension.addInjectable(AdresseIndeks.class, adresseIndeks);
        jsonMapper = mapperExtension.lagCopyAvObjectMapperOgUtvideMedVars();
    }

    @Test
    void InntektFordelSeraliseringDeseraliseringTest() {
        test(InntektFordel.KONTANTYTELSE);
    }

    @Test
    void InntektTypeSeraliseringDeseraliseringTest() {
        test(InntektType.LØNNSINNTEKT);
    }

    @Test
    void InntektsperiodeSeraliseringDeseraliseringTest() {
        test(lagInntektsperiode());
    }

    @Test
    void InntektskomponentModellSeraliseringDeseraliseringTest() {
        test(lagInntektskomponentModell());
    }

    protected InntektskomponentModell lagInntektskomponentModell() {
        return new InntektskomponentModell(List.of(lagInntektsperiode()));
    }

    private Inntektsperiode lagInntektsperiode() {
        return new Inntektsperiode(LocalDate.now(), LocalDate.now(), "990234022", 250_000, "90807060",
                InntektType.LØNNSINNTEKT, InntektFordel.KONTANTYTELSE, "Beskrivelse", InntektYtelseType.FASTLØNN, "Regel1",
                true, false, new PersonArbeidsgiver("987654321"));
    }
}
