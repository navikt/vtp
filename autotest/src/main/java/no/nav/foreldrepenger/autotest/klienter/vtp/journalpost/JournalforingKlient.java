package no.nav.foreldrepenger.autotest.klienter.vtp.journalpost;

import java.io.IOException;

import org.apache.http.HttpResponse;

import io.qameta.allure.Step;
import no.nav.foreldrepenger.autotest.klienter.vtp.VTPKlient;
import no.nav.foreldrepenger.autotest.klienter.vtp.journalpost.dto.JournalpostId;
import no.nav.foreldrepenger.autotest.util.http.BasicHttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.StatusRange;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.JournalpostModell;

public class JournalforingKlient extends VTPKlient{

    
    private static final String JOURNALFØRING_URL = "/journalforing";
    private static final String JOURNALFØR_FORELDREPENGER_SØKNAD_URL_FORMAT = JOURNALFØRING_URL + "/foreldrepengesoknadxml/fnr/%s/dokumenttypeid/%s";
    private static final String KNYTT_SAK_TIL_JOURNALPOST = JOURNALFØRING_URL + "/knyttsaktiljournalpost/journalpostid/%s/saksnummer/%s";
    
    public JournalforingKlient(BasicHttpSession session) {
        super(session);
    }

    @Step("Journalfører sak i VTP")
    public JournalpostId journalfør(JournalpostModell journalpostModell) throws IOException {
        String url = hentRestRotUrl() + String.format(JOURNALFØR_FORELDREPENGER_SØKNAD_URL_FORMAT, journalpostModell.getAvsenderFnr(), journalpostModell.getDokumentModellList().get(0).getDokumentType().getKode());
        //Hack for innhold
        HttpResponse response = post(url, journalpostModell.getDokumentModellList().get(0).getInnhold());
        String json = hentResponseBody(response);
        return json.equals("") ? null : hentObjectMapper().readValue(json, JournalpostId.class);
    }

    @Step("Knytter journalpost id {journalpostId} til sak {saksnummer} i VTP")
    public JournalpostId knyttSakTilJournalpost(String journalpostId, String saksnummer) throws IOException{
        String url = hentRestRotUrl() + String.format(KNYTT_SAK_TIL_JOURNALPOST, journalpostId, saksnummer);
        return postOgHentJson(url, null, JournalpostId.class, StatusRange.STATUS_SUCCESS);
    }
    
}
