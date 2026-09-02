package no.nav.foreldrepenger.vtp.server.api.scenario;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.vtp.kontrakter.person.v2.Landkode;

class LandkodeTest {

    @Test
    void normalisererAlpha2OgAlpha3TilUppercaseAlpha3() {
        assertThat(Landkode.normaliser("NO")).isEqualTo("NOR");
        assertThat(Landkode.normaliser("no")).isEqualTo("NOR");
        assertThat(Landkode.normaliser("NOR")).isEqualTo("NOR");
        assertThat(Landkode.normaliser("nor")).isEqualTo("NOR");
        assertThat(Landkode.normaliser("DE")).isEqualTo("DEU");
    }

    @Test
    void beholderNull() {
        assertThat(Landkode.normaliser(null)).isNull();
    }

    @Test
    void avviserUgyldigLandkode() {
        for (var ugyldig : new String[]{"", " ", "N", "N0", "ZZ", "ZZZ"}) {
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> Landkode.normaliser(ugyldig))
                    .withMessage("Ugyldig landkode");
        }
    }
}
