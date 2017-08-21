package no.nav.tjeneste.virksomhet.person.v3;

import no.nav.tjeneste.virksomhet.person.v3.binding.HentPersonPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentPersonSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Bruker;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.NorskIdent;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.PersonIdent;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonRequest;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonResponse;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonServiceMockTest {

    @Test
    public void testHentPerson() throws HentPersonPersonIkkeFunnet, HentPersonSikkerhetsbegrensning {
        PersonServiceMockImpl personServiceMock = new PersonServiceMockImpl();
        HentPersonRequest request = new HentPersonRequest();
        PersonIdent personIdent = new PersonIdent();
        NorskIdent norskIdent = new NorskIdent();
        norskIdent.setIdent("07078518434");
        personIdent.setIdent(norskIdent);
        request.setAktoer(personIdent);

        HentPersonResponse response = personServiceMock.hentPerson(request);
        assertThat(response).isNotNull();
        Bruker person = (Bruker) response.getPerson();
        assertThat(person).isNotNull();
        assertThat(person.getMaalform().getValue()).isNull();
        PersonIdent aktoer = (PersonIdent)person.getAktoer();
        assertThat(aktoer).isNotNull();
        assertThat(aktoer.getIdent().getIdent()).isEqualTo("07078518434");

    }

    @Test
    public void testHentPersonMaalformSatt() throws HentPersonPersonIkkeFunnet, HentPersonSikkerhetsbegrensning {
        PersonServiceMockImpl personServiceMock = new PersonServiceMockImpl();
        HentPersonRequest request = new HentPersonRequest();
        PersonIdent personIdent = new PersonIdent();
        NorskIdent norskIdent = new NorskIdent();
        norskIdent.setIdent("19069209028");
        personIdent.setIdent(norskIdent);
        request.setAktoer(personIdent);

        HentPersonResponse response = personServiceMock.hentPerson(request);
        assertThat(response).isNotNull();
        Bruker person = (Bruker) response.getPerson();
        assertThat(person).isNotNull();
        assertThat(person.getMaalform().getValue()).isEqualTo("EN");
        PersonIdent aktoer = (PersonIdent)person.getAktoer();
        assertThat(aktoer).isNotNull();
        assertThat(aktoer.getIdent().getIdent()).isEqualTo("19069209028");

    }

    @Test
    public void testHentPersonMedRelasjon() throws HentPersonPersonIkkeFunnet, HentPersonSikkerhetsbegrensning {
        PersonServiceMockImpl personServiceMock = new PersonServiceMockImpl();
        HentPersonRequest request = new HentPersonRequest();
        PersonIdent personIdent = new PersonIdent();
        NorskIdent norskIdent = new NorskIdent();
        norskIdent.setIdent("11111655830");
        personIdent.setIdent(norskIdent);
        request.setAktoer(personIdent);

        HentPersonResponse response = personServiceMock.hentPerson(request);
        assertThat(response).isNotNull();
        assertThat(response.getPerson()).isNotNull();
        PersonIdent aktoer = (PersonIdent)response.getPerson().getAktoer();
        assertThat(aktoer).isNotNull();
        assertThat(aktoer.getIdent().getIdent()).isEqualTo("11111655830");
        assertThat(response.getPerson().getHarFraRolleI()).isNotNull();
        assertThat(response.getPerson().getHarFraRolleI()).isNotEmpty();
        assertThat(response.getPerson().getHarFraRolleI().get(0).getTilPerson()).isNotNull();
    }

    @Test
    public void testHentPersonHardkodetVerdi() throws HentPersonPersonIkkeFunnet, HentPersonSikkerhetsbegrensning {
        PersonServiceMockImpl personServiceMock = new PersonServiceMockImpl();
        HentPersonRequest request = new HentPersonRequest();
        PersonIdent personIdent = new PersonIdent();
        NorskIdent norskIdent = new NorskIdent();
        norskIdent.setIdent("11111655830");
        personIdent.setIdent(norskIdent);
        request.setAktoer(personIdent);

        HentPersonResponse response = personServiceMock.hentPerson(request);
        assertThat(response).isNotNull();
        assertThat(response.getPerson()).isNotNull();
        PersonIdent aktoer = (PersonIdent)response.getPerson().getAktoer();
        assertThat(aktoer).isNotNull();
        assertThat(aktoer.getIdent().getIdent()).isEqualTo("11111655830");
    }
}
