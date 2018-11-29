package no.nav.foreldrepenger.fpmock2.testmodell.repo;

import java.util.List;

import no.nav.foreldrepenger.fpmock2.testmodell.feed.PersonHendelse;

public interface FeedRepository {


    Long leggTilHendelse(PersonHendelse personHendelse);

    Long genererSekvensnummer();

    List<PersonHendelse> hentAlleHendelser();
}
