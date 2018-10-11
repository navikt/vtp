package no.nav.foreldrepenger.fpmock2.testmodell.repo.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.DokumentModell;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.JournalRepository;


public class JournalRepositoryImpl implements JournalRepository {

    private HashMap<String,JournalpostModell> journalposter;
    private HashMap<String, DokumentModell> dokumenter;

    private Integer journalpostId;
    private Integer dokumentId;

    private static JournalRepositoryImpl instance;


    public static synchronized JournalRepositoryImpl getInstance(){

        if(instance == null){
            instance = new JournalRepositoryImpl();
        }

        return instance;
    }


    private JournalRepositoryImpl() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("Mdkm");
        journalposter = new HashMap<>();
        dokumenter = new HashMap<>();
        journalpostId = Integer.parseInt(LocalDateTime.now().format(formatter)) * 100;
        dokumentId = Integer.parseInt(LocalDateTime.now().format(formatter)) * 100;
    }

    @Override
    public Optional<DokumentModell> finnDokumentMedDokumentId(String dokumentId) {
        if(dokumenter.containsKey(dokumentId)){
            return Optional.ofNullable(dokumenter.get(dokumentId));
        } else {
            return Optional.ofNullable(null);
        }
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
        if(journalposter.containsKey(journalpostId)){
            return Optional.ofNullable(journalposter.get(journalpostId));
        } else {
            return Optional.ofNullable(null);
        }
    }

    @Override
    public String leggTilJournalpost(JournalpostModell journalpostModell){
        String journalpostId = "";
        if(journalpostModell.getJournalpostId() != null && !journalpostModell.getJournalpostId().isEmpty()){
            journalpostId = journalpostModell.getJournalpostId();
        } else {
            journalpostId = genererJournalpostId();
            journalpostModell.setJournalpostId(journalpostId);
        }

        for(DokumentModell dokumentModell : journalpostModell.getDokumentModellList()){
            String dokumentId = "";
            if(dokumentModell.getDokumentId() != null && !journalpostModell.getJournalpostId().isEmpty()){
                dokumentId = dokumentModell.getDokumentId();
            } else {
                dokumentId = genererDokumentId();
                dokumentModell.setDokumentId(dokumentId);
            }
            if(dokumenter.containsKey(dokumentId)){
                throw new IllegalStateException("Forsøker å opprette dokument allerede eksisterende dokumentId");
            } else {
                dokumenter.put(dokumentId, dokumentModell);
            }
        }

        if(journalposter.containsKey(journalpostId)){
            throw new IllegalStateException("Forsøker å opprette journalpost allerede eksisterende journalpostId");
        } else {
            journalposter.put(journalpostId, journalpostModell);
            return journalpostId;
        }
    }


    public String genererJournalpostId(){
        journalpostId++;
        return journalpostId.toString();
    }

    public String genererDokumentId(){
        dokumentId++;
        return dokumentId.toString();
    }




}
