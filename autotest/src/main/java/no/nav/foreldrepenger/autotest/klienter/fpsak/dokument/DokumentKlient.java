package no.nav.foreldrepenger.autotest.klienter.fpsak.dokument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.FpsakKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.dokument.dto.DokumentListeEnhet;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.StatusRange;

public class DokumentKlient extends FpsakKlient{

    private static String DOKUMENT_URL = "/dokument";
    private static String HENT_DOKUMENT_URL_FORMAT = DOKUMENT_URL +"/hent-dokument?saksnummer=%1$s&journalpostId=%2$s&dokumentId=%3$s";
    private static String HENT_DOKUMENTLISTE_URL_FORMAT = DOKUMENT_URL +"/hent-dokumentliste?saksnummer=%1$s";
    
    public DokumentKlient(HttpSession session) {
        super(session);
    }
    
    public Object hentDokument(long saksnummer, String journalpostId, String dokumentId) {
        //String url = hentRestRotUrl() + String.format(HENT_DOKUMENT_URL_FORMAT, saksnummer, journalpostId, dokumentId);
        return null; //TODO usikker på modellen her. swagger viser ingenting
    }
    
    public List<DokumentListeEnhet> hentDokumentliste(long saksnummer) throws IOException {
        String url = hentRestRotUrl() + String.format(HENT_DOKUMENTLISTE_URL_FORMAT, saksnummer);
        return getOgHentJson(url, hentObjectMapper().getTypeFactory().constructCollectionType(ArrayList.class, DokumentListeEnhet.class), StatusRange.STATUS_SUCCESS);
    }
    
}
