package no.nav.foreldrepenger.vtp.testmodell.repo.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import no.nav.foreldrepenger.vtp.testmodell.feed.HendelseContent;
import no.nav.foreldrepenger.vtp.testmodell.repo.FeedRepository;
import no.nav.tjenester.person.feed.common.v1.Feed;
import no.nav.tjenester.person.feed.common.v1.FeedEntry;
import no.nav.tjenester.person.feed.common.v1.Metadata;

public class FeedRepositoryImpl implements FeedRepository {

    private List<FeedEntry> feedEntries;
    private Long sekvensnummer;

    private static FeedRepositoryImpl instance;

    public static synchronized FeedRepositoryImpl getInstance(){
        if(instance==null){
            instance = new FeedRepositoryImpl();
        }
        return instance;
    }

    private FeedRepositoryImpl(){
        feedEntries = new ArrayList<>();
        sekvensnummer = 1L;
    }


    @Override
    public FeedEntry leggTilHendelse(HendelseContent hendelseContent){
        FeedEntry feedEntry = FeedEntry.builder()
                .sequence(genererSekvensnummer())
                .metadata(genererMetaData())
                .content(hendelseContent)
                .type(hendelseContent.hentType())
                .build();
        feedEntries.add(feedEntry);
        return feedEntry;
    }

    public Feed hentFeed(Integer sequenceId, Integer pageSize){
        List<FeedEntry> returnEntries = feedEntries.stream()
                .filter(entry -> entryHarSequenceIVindu(entry, sequenceId, pageSize))
                .collect(Collectors.toList());

        return Feed.builder()
                .title("PersonFeed_v2")
                .items(returnEntries)
                .build();
    }

    private boolean entryHarSequenceIVindu(FeedEntry entry, Integer sequenceId, Integer pageSize) {
        return sequenceId != null && pageSize != null && entry.getSequence() >= sequenceId.longValue()
                && entry.getSequence() < sequenceId+pageSize;
    }

    private static Metadata genererMetaData() {
        return Metadata.builder()
                .guid(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .build();
    }

    @Override
    public Long genererSekvensnummer(){
        return sekvensnummer++;
    }

}
