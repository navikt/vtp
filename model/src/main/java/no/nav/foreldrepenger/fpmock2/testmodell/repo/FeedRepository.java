package no.nav.foreldrepenger.fpmock2.testmodell.repo;


import no.nav.foreldrepenger.fpmock2.testmodell.feed.HendelseContent;
import no.nav.tjenester.person.feed.common.v1.FeedEntry;

public interface FeedRepository {


    FeedEntry leggTilHendelse(HendelseContent hendelseContent);

    Long genererSekvensnummer();

}
