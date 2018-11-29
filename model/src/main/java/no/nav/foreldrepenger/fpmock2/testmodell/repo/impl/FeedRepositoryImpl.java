package no.nav.foreldrepenger.fpmock2.testmodell.repo.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("Mdkm");
        hendelser = new ArrayList<>();
        sekvensnummer = Long.parseLong(LocalDateTime.now().format(formatter)) * 10;
    }

    @Override
    public Long leggTilHendelse(PersonHendelse personHendelse){

        if(personHendelse.getSekvensnummer() == null || personHendelse.getSekvensnummer().longValue() <= 0){
            personHendelse.setSekvensnummer(genererSekvensnummer());
        }
        hendelser.add(personHendelse);
        return personHendelse.getSekvensnummer();
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
