package no.nav.foreldrepenger.autotest.klienter.vtp.journalpost;

import no.nav.foreldrepenger.autotest.klienter.vtp.VTPKlient;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.JournalpostModell;

public class JournalpostKlient extends VTPKlient{

    
    private static final String JOURNALPOST_URL = "/journalpost";
    
    public JournalpostKlient(HttpSession session) {
        super(session);
    }

    public String leggTilJournalpost(JournalpostModell journalpostModell) {
        throw new RuntimeException("Ikke implementert: leggTilJournalpost");
    }
    
}
