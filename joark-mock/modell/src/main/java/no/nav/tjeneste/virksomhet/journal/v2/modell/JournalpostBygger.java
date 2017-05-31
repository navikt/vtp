package no.nav.tjeneste.virksomhet.journal.v2.modell;

import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Journalpost;
import no.nav.tjeneste.virksomhet.journalmodell.JournalDokument;

public class JournalpostBygger {
    protected long id;
    protected String dokumentId;
    protected String journalpostId;
    protected String innhold;
    protected String sakId;

    public JournalpostBygger(JournalDokument journalDokument){
        this.id = journalDokument.getId();
        //this.dokumentId = journalDokument.dokumentId;
        this.journalpostId = journalDokument.getJournalpostId();
        this.innhold = journalDokument.getDokument().toString();
        this.sakId = journalDokument.getSakId();
    }

    public JournalpostBygger(long id, String journalpostId, String innhold, String sakId) {
        this.id = id;
        this.journalpostId = journalpostId;
        this.innhold = innhold;
        this.sakId = sakId;
    }

    public Journalpost ByggJournalpost(){
        Journalpost j = new Journalpost();
        j.setJournalpostId(this.journalpostId);
        j.setInnhold(this.innhold);
        return j;
    }
}
