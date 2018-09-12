package no.nav.foreldrepenger.fpmock2.testmodell.journal;

import java.util.HashMap;
import java.util.Map;

import no.nav.foreldrepenger.fpmock2.testmodell.journal.dokument.DokumentModell;

class JournalpostRepository {


    private Map<String, DokumentModell> byIdent = new HashMap<>();
    private Map<String, DokumentModell> bySaknr = new HashMap<>();

    public JournalpostRepository(){}


    public void leggTil(DokumentModell dokumentModell){
        if(dokumentModell == null){
            return;
        }

    }
}
