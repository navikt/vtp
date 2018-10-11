package no.nav.foreldrepenger.autotest.klienter.vtp.journalpost;

import java.io.IOException;

import no.nav.foreldrepenger.autotest.klienter.vtp.VTPKlient;
import no.nav.foreldrepenger.autotest.klienter.vtp.journalpost.dto.JournalpostId;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.StatusRange;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.JournalpostModell;

public class JournalforingKlient extends VTPKlient{

    
    private static final String JOURNALFØRING_URL = "/journalforing";
    private static final String JOURNALFØR_FORELDREPENGER_SØKNAD_URL_FORMAT = JOURNALFØRING_URL + "/foreldrepengesoknadxml/fnr/%s/dokumenttypeid/%s";
    private static final String KNYTT_SAK_TIL_JOURNALPOST = JOURNALFØRING_URL + "/knyttsaktiljournalpost/journalpostid/%s/saksnummer/%s";
    
    public JournalforingKlient(HttpSession session) {
        super(session);
    }

    public JournalpostId journalfør(JournalpostModell journalpostModell) throws IOException {
        String url = hentRestRotUrl() + String.format(JOURNALFØR_FORELDREPENGER_SØKNAD_URL_FORMAT, journalpostModell.getAvsenderFnr(), journalpostModell.getDokumentModellList().get(0).getDokumentType().getKode());
        return postOgHentJson(url, null, JournalpostId.class, StatusRange.STATUS_SUCCESS);
    }

    public JournalpostId knyttSakTilJournalpost(String journalpostId, String saksnummer) throws IOException{
        String url = hentRestRotUrl() + String.format(KNYTT_SAK_TIL_JOURNALPOST, journalpostId, saksnummer);
        return postOgHentJson(url, null, JournalpostId.class, StatusRange.STATUS_SUCCESS);
    }
    
}
