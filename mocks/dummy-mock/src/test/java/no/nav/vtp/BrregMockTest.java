package no.nav.vtp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class BrregMockTest {

    @Test
    void skalReturnereTomRolleutskrift() {
        assertThat(new BrregMock().hentRolleutskrift().enheter()).isEmpty();
    }
}
