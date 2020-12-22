package no.nav.foreldrepenger.vtp.testmodell.jackson;

import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.sigrun.Inntektsår;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.sigrun.Oppføring;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.sigrun.SigrunModell;

class SigrunSeraliseringDeseraliseringsTest extends SerializationTestBase {

    @Test
    public void InntektsårSeraliseringDeseraliseringTest() {
        test(lagInntektsår());
    }

    @Test
    public void OppføringSeraliseringDeseraliseringTest() {
        test(lagOppføring());
    }

    @Test
    public void SigrunModellSeraliseringDeseraliseringTest() {
        test(lagSigrunModell());
    }

    protected SigrunModell lagSigrunModell() {
        return new SigrunModell(List.of(lagInntektsår()));
    }

    private Inntektsår lagInntektsår() {
        return new Inntektsår("2020", List.of(lagOppføring()));
    }

    private Oppføring lagOppføring() {
        return new Oppføring("TekniskNavn", "25000");
    }
}
