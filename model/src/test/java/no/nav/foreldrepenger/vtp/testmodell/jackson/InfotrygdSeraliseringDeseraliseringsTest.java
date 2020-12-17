package no.nav.foreldrepenger.vtp.testmodell.jackson;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.InfotrygdBehandlingstema;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.InfotrygdSakResultat;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.InfotrygdSakStatus;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.InfotrygdSakType;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.InfotrygdTema;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdInntektsperiodeType;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.beregningsgrunnlag.InfotrygdVedtak;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd.ytelse.InfotrygdYtelse;

class InfotrygdSeraliseringDeseraliseringsTest extends SerializationTestBase {

//    @BeforeAll
//    private static void utvidObjectmapperMedInjectables() {
//        var jsonMapper = new JsonMapperUtvider(new VariabelContainer(Map.of("key", "value")));
//        var lokalIdentIndeks = new LokalIdentIndeks("12345", new FiktiveFnr());
//        jsonMapper.addInjectable(LokalIdentIndeks.class, lokalIdentIndeks);
//        mapper = jsonMapper.lagCopyAvObjectMapperOgUtvideMedVars();
//    }

    @Test
    public void InfotrygdTemaSeraliseringDeseraliseringTest() {
        test(InfotrygdTema.FA);
    }

    @Test
    public void InfotrygdBehandlingstemaSeraliseringDeseraliseringTest() {
        test(InfotrygdBehandlingstema.SP);
    }

    @Test
    public void InfotrygdSakTypeSeraliseringDeseraliseringTest() {
        test(InfotrygdSakType.K);
    }

    @Test
    public void InfotrygdSakStatusSeraliseringDeseraliseringTest() {
        test(InfotrygdSakStatus.IP);
    }

    @Test
    public void InfotrygdSakResultatStatusSeraliseringDeseraliseringTest() {
        test(InfotrygdSakResultat.I);
    }

    @Test
    public void InfotrygdInntektsperiodeTypeSeraliseringDeseraliseringTest() {
        test(InfotrygdInntektsperiodeType.D);
    }

    @Test
    public void InfotrygdYtelseSeraliseringDeseraliseringTest() {
        test(lagInfotrygdYtelse());
    }

    @Test
    public void InfotrygdVedtakSeraliseringDeseraliseringTest() {
        test(lagInfotrygdVedtak());
    }

    private InfotrygdYtelse lagInfotrygdYtelse() {
        return new InfotrygdYtelse("250034000", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(),
                LocalDateTime.now(), LocalDateTime.now(), "CAL-ID:12345678", InfotrygdTema.BS, InfotrygdBehandlingstema.AE,
                InfotrygdSakType.A, InfotrygdSakStatus.I, InfotrygdSakResultat.A);
    }

    private InfotrygdVedtak lagInfotrygdVedtak() {
        return new InfotrygdVedtak(LocalDate.now().minusMonths(2), LocalDate.now(), 100);
    }


}
