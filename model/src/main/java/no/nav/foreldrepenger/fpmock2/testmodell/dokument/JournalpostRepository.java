package no.nav.foreldrepenger.fpmock2.testmodell.dokument;

import java.util.HashMap;
import java.util.Map;

import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.JournalpostModell;

class JournalpostRepository {


    private Map<String, JournalpostModell> byIdent = new HashMap<>();
    private Map<String, JournalpostModell> bySaknr = new HashMap<>();

    public JournalpostRepository(){}


    public void leggTil(JournalpostModell journalpostModell){
        if(journalpostModell == null){
            return;
        }

        byIdent.put(journalpostModell.getAvsenderFnr(),journalpostModell);
        bySaknr.put(journalpostModell.getSakId(),journalpostModell);
    }


}
