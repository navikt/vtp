package no.nav.foreldrepenger.autotest.klienter.fpsak.dokument;

import no.nav.foreldrepenger.autotest.klienter.fpsak.FpsakKlient;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;

public class DokumentKlient extends FpsakKlient{

    public static String DOKUMENT_URL = "/dokument";
    public static String HENT_DOKUMENT_URL_FORMAT = DOKUMENT_URL +"/hent-dokument?saksnummer=%1$s&journalpostId=%2$s&dokumentId=%3$s";
    public static String HENT_DOKUMENTLISTE_URL_FORMAT = DOKUMENT_URL +"/hent-dokumentliste?saksnummer=%1$s";
    
    public DokumentKlient(HttpSession session) {
        super(session);
    }
    
    public Object hentDokument(long saksnummer, String journalpostId, String dokumentId) {
        String url = hentRestRotUrl() + String.format(HENT_DOKUMENT_URL_FORMAT, saksnummer, journalpostId, dokumentId);
        return null;
    }
    
    public Object hentDokumentliste(long saksnummer) {
        return null;
    }
    
}
