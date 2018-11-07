package no.nav.foreldrepenger.autotest.klienter.vtp.sak;

import java.io.IOException;

import no.nav.foreldrepenger.autotest.klienter.vtp.VTPKlient;
import no.nav.foreldrepenger.autotest.klienter.vtp.sak.dto.OpprettSakRequestDTO;
import no.nav.foreldrepenger.autotest.klienter.vtp.sak.dto.OpprettSakResponseDTO;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.StatusRange;

public class SakKlient extends VTPKlient {

    private static final String SAK_URL = "/sak";
    private static final String OPPRETT_SAK_URL = SAK_URL + "";

    public SakKlient(HttpSession session) {
        super(session);
    }

    public OpprettSakResponseDTO opprettSak(OpprettSakRequestDTO requestDTO) throws IOException {
        String url = hentRestRotUrl() + OPPRETT_SAK_URL;
        return postOgHentJson(url,requestDTO,OpprettSakResponseDTO.class, StatusRange.STATUS_SUCCESS);
    }
}
