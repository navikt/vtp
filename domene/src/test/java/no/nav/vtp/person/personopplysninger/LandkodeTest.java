package no.nav.vtp.person.personopplysninger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.Test;

class LandkodeTest {

    @Test
    void normalisererAlpha3TilStoreBokstaver() {
        assertThat(Landkode.normaliser("NOR")).isEqualTo("NOR");
        assertThat(Landkode.normaliser("nor")).isEqualTo("NOR");
        assertThat(Landkode.normaliser("DEU")).isEqualTo("DEU");
    }

    @Test
    void beholderNull() {
        assertThat(Landkode.normaliser(null)).isNull();
    }

    @Test
    void avviserAlpha2OgUgyldigLandkode() {
        for (var ugyldig : new String[]{"NO", "no", "DE", "", " ", "N", "N0", "ZZ", "ZZZ"}) {
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> Landkode.normaliser(ugyldig))
                    .withMessage("Ugyldig landkode '%s'. Kun landkoder med 3 tegn støttes", ugyldig);
        }
    }
}
