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

    @Test
    public void testHentKjerneinformasjonMedRelasjon() throws HentKjerneinformasjonPersonIkkeFunnet, HentKjerneinformasjonSikkerhetsbegrensning {
        PersonServiceMockImpl personServiceMock = new PersonServiceMockImpl();
        HentKjerneinformasjonRequest request = new HentKjerneinformasjonRequest();
        request.setIdent("11111655830");
        HentKjerneinformasjonResponse response = personServiceMock.hentKjerneinformasjon(request);
        assertThat(response).isNotNull();
        assertThat(response.getPerson()).isNotNull();
        assertThat(response.getPerson().getIdent()).isNotNull();
        assertThat(response.getPerson().getIdent().getIdent()).isEqualTo("11111655830");
        assertThat(response.getPerson().getHarFraRolleI()).isNotNull();
        assertThat(response.getPerson().getHarFraRolleI()).isNotEmpty();
        assertThat(response.getPerson().getHarFraRolleI().get(0).getTilPerson()).isNotNull();
    }

    @Test
    public void testHentKjerneinformasjonHardkodetVerdi() throws HentKjerneinformasjonPersonIkkeFunnet, HentKjerneinformasjonSikkerhetsbegrensning {
        PersonServiceMockImpl personServiceMock = new PersonServiceMockImpl();
        HentKjerneinformasjonRequest request = new HentKjerneinformasjonRequest();
        request.setIdent("11111655830");
        HentKjerneinformasjonResponse response = personServiceMock.hentKjerneinformasjon(request);
        assertThat(response).isNotNull();
        assertThat(response.getPerson()).isNotNull();
        assertThat(response.getPerson().getIdent()).isNotNull();
        assertThat(response.getPerson().getIdent().getIdent()).isEqualTo("11111655830");
    }
}
