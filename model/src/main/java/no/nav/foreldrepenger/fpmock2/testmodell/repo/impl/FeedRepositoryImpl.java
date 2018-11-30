package no.nav.foreldrepenger.fpmock2.testmodell.repo.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.fpmock2.testmodell.feed.PersonHendelse;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.FeedRepository;

public class FeedRepositoryImpl implements FeedRepository {
    private static final Logger LOG = LoggerFactory.getLogger(FeedRepositoryImpl.class);

    private List<PersonHendelse> hendelser;
    private Long sekvensnummer;

    private static FeedRepositoryImpl instance;

    public static synchronized FeedRepositoryImpl getInstance(){
        if(instance==null){
            instance = new FeedRepositoryImpl();
        }
        return instance;
    }

    private FeedRepositoryImpl(){
        hendelser = new ArrayList<>();
        sekvensnummer = 1L;
    }

    @Override
    public Long leggTilHendelse(PersonHendelse personHendelse){

        if(personHendelse.getSequence() == null || personHendelse.getSequence().longValue() <= 0){
            personHendelse.setSequence(genererSekvensnummer());
        }
        hendelser.add(personHendelse);
        return personHendelse.getSequence();
    }

    @Override
    public Long genererSekvensnummer(){
        return ++sekvensnummer;
    }


    @Override
    public List<PersonHendelse> hentAlleHendelser(){
        return List.copyOf(hendelser);
    }


}
