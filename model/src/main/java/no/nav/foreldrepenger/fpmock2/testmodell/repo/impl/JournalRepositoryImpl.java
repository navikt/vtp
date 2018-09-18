package no.nav.foreldrepenger.fpmock2.testmodell.repo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.DokumentModell;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.JournalRepository;


public class JournalRepositoryImpl implements JournalRepository {

    private HashMap<String,JournalpostModell> journalposterPåId;
    private HashMap<String, DokumentModell> dokumenterPåId;

    private Integer journalpostId;
    private Integer dokumentId;

    public JournalRepositoryImpl() {
        journalposterPåId = new HashMap<>();
        dokumenterPåId = new HashMap<>();
        journalpostId = 500; //Sett til psudotilfeldig offset
        dokumentId =  500; //Sett til psudotilfeldig offset
    }

    @Override
    public Optional<DokumentModell> finnDokumentMedDokumentId(String dokumentId) {
        if(dokumenterPåId.containsKey(dokumentId)){
            return Optional.ofNullable(dokumenterPåId.get(dokumentId));
        } else {
            return Optional.ofNullable(null);
        }
    }

    @Override
    public List<JournalpostModell> finnJournalposterPåFnr(String fnr){
        //TODO: Søk og returner
        return new ArrayList<>();
    }

    @Override
    public List<JournalpostModell> finnJournalposterMedSakId(String sakId){
        //TODO: Søk og returner
        return new ArrayList<>();
    }

    @Override
    public Optional<JournalpostModell> finnJournalpostMedJournalpostId(String journalpostId){
        if(journalposterPåId.containsKey(journalpostId)){
            return Optional.ofNullable(journalposterPåId.get(journalpostId));
        } else {
            return Optional.ofNullable(null);
        }
    }

    @Override
    public String leggTilJournalpost(JournalpostModell journalpostModell){
        String id = "";
        if(!journalpostModell.getId().isEmpty()){
            id = journalpostModell.getId();
        } else {
            id = genererJournalpostId();
        }

        if(journalposterPåId.containsKey(id)){
            throw new IllegalStateException("Forsøker å opprette allerede eksisterende journalpostId");
        } else {
            journalposterPåId.put(id, journalpostModell);
            return id;
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
