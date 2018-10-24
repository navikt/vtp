package no.nav.foreldrepenger.autotest.klienter.spberegning.dokument;

import java.io.IOException;

import no.nav.foreldrepenger.autotest.klienter.spberegning.SpBeregningKlient;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.StatusRange;

public class DokumentKlient extends SpBeregningKlient {
    
    private static final String DOKUMENT_HENT_DOKUMENT_URL = "/dokument/hent-dokument";

    public DokumentKlient(HttpSession session) {
        super(session);
    }
    
    public void hentDokument(String mottattDokumentIdDto) throws IOException {
        String url = hentRestRotUrl() + DOKUMENT_HENT_DOKUMENT_URL + "?mottattDokumentIdDto=" + mottattDokumentIdDto;
        ValidateResponse(get(url), StatusRange.STATUS_SUCCESS);
    }

}
