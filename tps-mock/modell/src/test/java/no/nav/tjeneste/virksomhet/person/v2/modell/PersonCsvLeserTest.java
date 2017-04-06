package no.nav.tjeneste.virksomhet.person.v2.modell;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

public class PersonCsvLeserTest {

    @Test
    public void skal_lese_personer() throws Exception {
        List<TpsPerson> personer = PersonCsvLeser.lesFil("personer.csv");
        assertThat(personer).hasSize(52);
    }

}