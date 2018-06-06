package no.nav.tjeneste.virksomhet.organisasjon.v4;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import no.nav.tjeneste.virksomhet.organisasjon.v4.informasjon.ObjectFactory;
import no.nav.tjeneste.virksomhet.organisasjon.v4.meldinger.HentNoekkelinfoOrganisasjonRequest;
import no.nav.tjeneste.virksomhet.organisasjon.v4.meldinger.HentNoekkelinfoOrganisasjonResponse;
import no.nav.tjeneste.virksomhet.organisasjon.v4.meldinger.HentOrganisasjonRequest;
import no.nav.tjeneste.virksomhet.organisasjon.v4.meldinger.HentOrganisasjonResponse;

public class OrganisasjonMockTest {

    private ObjectFactory objectFactory = new ObjectFactory();

    private static final String ID = "987656323";
    private static final String SESAM_AS = "976030788";
    private static final String SESAM_BEDR = "976037286";
    private static final String EPLE_AS = "986498192";
    private static final String EPLE_BEDR = "986507035";

    @Test
    public void skal_hente_organisasjon() throws Exception {
        HentOrganisasjonRequest request = new HentOrganisasjonRequest();

        request.setOrgnummer(ID);

        HentOrganisasjonResponse response = new OrganisasjonMockImpl().hentOrganisasjon(request);

        assertThat(response).isNotNull();
        assertThat(response.getOrganisasjon()).isNotNull();
        assertThat(response.getOrganisasjon().getOrgnummer().equals(ID)).isTrue();
    }

    @Test
    public void skal_hente_noekkelinfo() throws Exception {
        HentNoekkelinfoOrganisasjonRequest request = new HentNoekkelinfoOrganisasjonRequest();

        request.setOrgnummer(SESAM_AS);

        HentNoekkelinfoOrganisasjonResponse response = new OrganisasjonMockImpl().hentNoekkelinfoOrganisasjon(request);

        assertThat(response).isNotNull();
        assertThat(response.getOrgnummer().equals(SESAM_AS)).isTrue();
    }
}
