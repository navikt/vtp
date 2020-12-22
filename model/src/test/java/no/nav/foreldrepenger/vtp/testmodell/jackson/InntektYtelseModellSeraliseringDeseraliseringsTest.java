package no.nav.foreldrepenger.vtp.testmodell.jackson;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.vtp.testmodell.identer.FiktiveFnr;
import no.nav.foreldrepenger.vtp.testmodell.identer.LokalIdentIndeks;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.util.JsonMapperUtvider;
import no.nav.foreldrepenger.vtp.testmodell.util.VariabelContainer;

class InntektYtelseModellSeraliseringDeseraliseringsTest extends SerializationTestBase {

    private static ArbeidsforholdSeraliseringDeseraliseringsTest arbeidsforhold;
    private static ArenaSeraliseringDeseraliseringsTest arena;
    private static InfotrygdSeraliseringDeseraliseringsTest infotrygd;
    private static InntektskomponentSeraliseringDeseraliseringsTest inntektskomponent;
    private static OmsorgpengerSeraliseringDeseraliseringsTest omsorgpenger;
    private static SigrunSeraliseringDeseraliseringsTest sigrun;
    private static TrexModellSeraliseringDeseralseringsTest trex;

    @BeforeAll
    public static void instansiererTestKlasser() {
        arbeidsforhold = new ArbeidsforholdSeraliseringDeseraliseringsTest();
        arena = new ArenaSeraliseringDeseraliseringsTest();
        infotrygd = new InfotrygdSeraliseringDeseraliseringsTest();
        inntektskomponent = new InntektskomponentSeraliseringDeseraliseringsTest();
        omsorgpenger = new OmsorgpengerSeraliseringDeseraliseringsTest();
        sigrun = new SigrunSeraliseringDeseraliseringsTest();
        trex = new TrexModellSeraliseringDeseralseringsTest();
    }

    @BeforeAll
    public static void utviderObjectmapperMedInjectables() {
        var jsonMapper = new JsonMapperUtvider(new VariabelContainer(Map.of("key", "value")));
        var lokalIdentIndeks = new LokalIdentIndeks("12345", new FiktiveFnr());
        jsonMapper.addInjectable(LokalIdentIndeks.class, lokalIdentIndeks);
        mapper = jsonMapper.lagCopyAvObjectMapperOgUtvideMedVars();
    }

    @Test
    public void InntektYtelseModellSeraliseringDeseraliseringTest() {
        test(new InntektYtelseModell(
                arena.lagArenaModell(),
                infotrygd.lagInfotrygdModell(),
                trex.lagTRexModell(),
                inntektskomponent.lagInntektskomponentModell(),
                arbeidsforhold.lagArbeidsforholdModell(),
                sigrun.lagSigrunModell(),
                omsorgpenger.lagOmsorgspengerModell()));
    }

}
