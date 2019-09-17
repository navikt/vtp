package no.nav.foreldrepenger.vtp.felles;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import no.nav.foreldrepenger.vtp.felles.ExpectRepository.Mock;

public class ExpectRepositoryTest {

    @Test
    public void expectNotMet_Mock() {
        ExpectRepository.registerToken(Mock.ARBEIDSFORHOLD, "test", "token", null);
        ExpectRepository.hit(Mock.ARENA, "test", null);
        assertTrue("Token was hit", !ExpectRepository.popToken(Mock.ARBEIDSFORHOLD, "test", "token").isHit);
    }

    @Test
    public void expectNotMet_WebMethod() {
        ExpectRepository.registerToken(Mock.ARBEIDSFORHOLD, "test", "token", null);
        ExpectRepository.hit(Mock.ARBEIDSFORHOLD, "ikke test", null);
        assertTrue("Token was hit", !ExpectRepository.popToken(Mock.ARBEIDSFORHOLD, "test", "token").isHit);
    }

    @Test
    public void expectMet() {
        ExpectRepository.registerToken(Mock.ARBEIDSFORHOLD, "test", "token", null);
        ExpectRepository.hit(Mock.ARBEIDSFORHOLD, "test", null);
        assertTrue("Token was not hit", ExpectRepository.popToken(Mock.ARBEIDSFORHOLD, "test", "token").isHit);
    }

    @Test
    public void expectWithPredicateMet() {
        ExpectRepository.registerToken(Mock.ARBEIDSFORHOLD, "test", "token", new ExpectPredicate("id", "1"));
        ExpectRepository.hit(Mock.ARBEIDSFORHOLD, "test", null);
        assertTrue("Token was not hit", ExpectRepository.popToken(Mock.ARBEIDSFORHOLD, "test", "token").isHit);
    }

    public void expectWithPredicateNotMet() {
        ExpectRepository.registerToken(Mock.ARBEIDSFORHOLD, "test", "token", new ExpectPredicate("id", "1"));
        ExpectRepository.hit(Mock.ARBEIDSFORHOLD, "test", new ExpectPredicate("id", "1"));
        assertTrue("Token was hit", !ExpectRepository.popToken(Mock.ARBEIDSFORHOLD, "test", "token").isHit);
    }
}
