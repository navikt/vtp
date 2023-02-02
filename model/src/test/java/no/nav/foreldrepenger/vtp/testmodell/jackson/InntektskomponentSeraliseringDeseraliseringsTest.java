package no.nav.foreldrepenger.vtp.testmodell.jackson;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.vtp.testmodell.identer.FiktiveFnr;
import no.nav.foreldrepenger.vtp.testmodell.identer.LokalIdentIndeks;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.FrilansArbeidsforholdsperiode;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektFordel;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektType;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.Inntektsperiode;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonArbeidsgiver;
import no.nav.foreldrepenger.vtp.testmodell.util.JacksonObjectMapperTestscenarioUtvider;
import no.nav.foreldrepenger.vtp.testmodell.util.VariabelContainer;

class InntektskomponentSeraliseringDeseraliseringsTest extends TestscenarioSerializationTestBase {

    @BeforeAll
    static void utvidObjectmapperMedInjectables() {
        var jsonMapper = new JacksonObjectMapperTestscenarioUtvider(new VariabelContainer(Map.of("key", "value")));
        var lokalIdentIndeks = new LokalIdentIndeks("12345", new FiktiveFnr());
        jsonMapper.addInjectable(LokalIdentIndeks.class, lokalIdentIndeks);
        mapper = jsonMapper.lagCopyAvObjectMapperOgUtvideMedVars();
    }

    @Test
    public void FrilansArbeidsforholdsperiodeSeraliseringDeseraliseringTest() {
        test(lagFrilansArbeidsforholdsperiode());
    }

    @Test
    public void InntektFordelSeraliseringDeseraliseringTest() {
        test(InntektFordel.KONTANTYTELSE);
    }

    @Test
    public void InntektTypeSeraliseringDeseraliseringTest() {
        test(InntektType.LØNNSINNTEKT);
    }

    @Test
    public void InntektsperiodeSeraliseringDeseraliseringTest() {
        test(lagInntektsperiode());
    }

    @Test
    public void InntektskomponentModellSeraliseringDeseraliseringTest() {
        test(lagInntektskomponentModell());
    }

    protected InntektskomponentModell lagInntektskomponentModell() {
        return new InntektskomponentModell(List.of(lagInntektsperiode()), List.of(lagFrilansArbeidsforholdsperiode()));
    }

    private FrilansArbeidsforholdsperiode lagFrilansArbeidsforholdsperiode() {
        return new FrilansArbeidsforholdsperiode(LocalDate.now(), LocalDate.now(), "90807060", 100,
                "1233456789", new PersonArbeidsgiver("987654321"));
    }

    private Inntektsperiode lagInntektsperiode() {
        return new Inntektsperiode(LocalDate.now(), LocalDate.now(), "990234022", 250_000, "90807060",
                InntektType.LØNNSINNTEKT, InntektFordel.KONTANTYTELSE, "Beskrivelse", "Regel1",
                true, false, new PersonArbeidsgiver("987654321"));
    }
}
