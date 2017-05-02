package no.nav.tjeneste.virksomhet.person.v2;

import no.nav.tjeneste.virksomhet.person.v2.binding.HentKjerneinformasjonPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v2.binding.HentKjerneinformasjonSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.person.v2.meldinger.HentKjerneinformasjonRequest;
import no.nav.tjeneste.virksomhet.person.v2.meldinger.HentKjerneinformasjonResponse;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonServiceMockTest {

    @Test
    public void testHentKjerneinformasjon() throws HentKjerneinformasjonPersonIkkeFunnet, HentKjerneinformasjonSikkerhetsbegrensning {
        PersonServiceMockImpl personServiceMock = new PersonServiceMockImpl();
        HentKjerneinformasjonRequest request = new HentKjerneinformasjonRequest();
        request.setIdent("07078518434");
        HentKjerneinformasjonResponse response = personServiceMock.hentKjerneinformasjon(request);
        assertThat(response).isNotNull();
        assertThat(response.getPerson()).isNotNull();
        assertThat(response.getPerson().getIdent()).isNotNull();
        assertThat(response.getPerson().getIdent().getIdent()).isEqualTo("07078518434");
    }
}
