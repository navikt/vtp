package no.nav.tjeneste.virksomhet.organisasjon.v4;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import no.nav.tjeneste.virksomhet.organisasjon.v4.informasjon.Enhetstyper;
import no.nav.tjeneste.virksomhet.organisasjon.v4.informasjon.Organisasjon;
import no.nav.tjeneste.virksomhet.organisasjon.v4.informasjon.ObjectFactory;

public class OrganisasjonGeneratorTest {

    private ObjectFactory objectFactory = new ObjectFactory();
    private static final String ID = "987656323";
    private static final String SESAM_AS = "976030788";
    private static final String SESAM_BEDR = "976037286";
    private static final String EPLE_AS = "986498192";
    private static final String EPLE_BEDR = "986507035";

    @Test
    public void skal_hente_organisasjon(){
        OrganisasjonGenerator gen = new OrganisasjonGenerator();
        Organisasjon org = gen.lagOrganisasjon(ID);
        Organisasjon sesam = gen.lagOrganisasjon(SESAM_AS);
        Organisasjon eple = gen.lagOrganisasjon(EPLE_BEDR);
        Enhetstyper enhetAS = gen.lagEnhetstype(EPLE_AS);
        Enhetstyper enhetBedr = gen.lagEnhetstype(ID);

        assertThat(org).hasFieldOrPropertyWithValue("orgnummer", ID);
        assertThat(sesam).hasFieldOrPropertyWithValue("orgnummer", SESAM_AS);
        assertThat(eple).hasFieldOrPropertyWithValue("orgnummer", EPLE_BEDR);
    }
}
