package no.nav.foreldrepenger.vtp.testmodell.repo.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.DokumentModell;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.JournalRepository;


public class JournalRepositoryImpl implements JournalRepository {

    private ConcurrentMap<String, JournalpostModell> journalposter;
    private ConcurrentMap<String, DokumentModell> dokumenter;

    private AtomicInteger journalpostId;
    private AtomicInteger dokumentId;
    private AtomicInteger eksternReferanseId;

    private static JournalRepositoryImpl instance;


    public static synchronized JournalRepositoryImpl getInstance(){
        if(instance == null){
            instance = new JournalRepositoryImpl();
        }
        return instance;
    }


    private JournalRepositoryImpl() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("Mdkm");
        journalposter = new ConcurrentHashMap<>();
        dokumenter = new ConcurrentHashMap<>();
        journalpostId = new AtomicInteger(Integer.parseInt(LocalDateTime.now().format(formatter)) * 100);
        dokumentId = new AtomicInteger(Integer.parseInt(LocalDateTime.now().format(formatter)) * 100);
        eksternReferanseId = new AtomicInteger(0);
    }

    @Override
    public Optional<DokumentModell> finnDokumentMedDokumentId(String dokumentId) {
        return Optional.ofNullable(dokumenter.getOrDefault(dokumentId, null));
    }

    @Override
    public List<JournalpostModell> finnJournalposterMedFnr(String fnr){
        return journalposter.values().stream()
                .filter(e -> (e.getAvsenderFnr() != null && e.getAvsenderFnr().equalsIgnoreCase(fnr)))
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalpostModell> finnJournalposterMedSakId(String sakId){
        return journalposter.values().stream()
                .filter(e -> (e.getSakId() != null && e.getSakId().equalsIgnoreCase(sakId)))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<JournalpostModell> finnJournalpostMedJournalpostId(String journalpostId){
        return Optional.ofNullable(journalposter.getOrDefault(journalpostId, null));
    }

    @Override
    public String leggTilJournalpost(JournalpostModell journalpostModell){
        String journalpostIdTemp;
        if(journalpostModell.getJournalpostId() != null && !journalpostModell.getJournalpostId().isEmpty()){
            journalpostIdTemp = journalpostModell.getJournalpostId();
        } else {
            journalpostIdTemp = genererJournalpostId();
            journalpostModell.setJournalpostId(journalpostIdTemp);
        }

        journalpostModell.setEksternReferanseId(journalpostModell.getEksternReferanseId() != null ?
                journalpostModell.getEksternReferanseId() : genererEksternReferanseId());

        for(DokumentModell dokumentModell : journalpostModell.getDokumentModellList()){
            String dokumentIdTemp;
            if(dokumentModell.getDokumentId() != null && !journalpostModell.getJournalpostId().isEmpty()){
                dokumentIdTemp = dokumentModell.getDokumentId();
            } else {
                dokumentIdTemp = genererDokumentId();
                dokumentModell.setDokumentId(dokumentIdTemp);
            }
            if(dokumenter.containsKey(dokumentIdTemp)){
                throw new IllegalStateException("Forsøker å opprette dokument allerede eksisterende dokumentId");
            } else {
                dokumenter.put(dokumentIdTemp, dokumentModell);
            }
        }

        if(journalposter.containsKey(journalpostIdTemp)){
            throw new IllegalStateException("Forsøker å opprette journalpost allerede eksisterende journalpostId");
        } else {
            journalposter.put(journalpostIdTemp, journalpostModell);
            return journalpostIdTemp;
        }
    }


    public String genererJournalpostId(){
        return Integer.toString(journalpostId.incrementAndGet());
    }

    public String genererDokumentId(){
        return Integer.toString(dokumentId.incrementAndGet());
    }

    public String genererEksternReferanseId() {
        return "AR" + String.format("%08d", eksternReferanseId.incrementAndGet());
    }

}
