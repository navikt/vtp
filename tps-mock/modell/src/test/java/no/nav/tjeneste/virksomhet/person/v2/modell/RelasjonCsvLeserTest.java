package no.nav.tjeneste.virksomhet.person.v2.modell;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

public class RelasjonCsvLeserTest {

    @Test
    public void skal_lese_relasjoner() throws Exception {
        List<TpsRelasjon> relasjoner = RelasjonCsvLeser.lesFil("relasjoner.csv");
        assertThat(relasjoner).hasSize(12);
    }

}