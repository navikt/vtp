package no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.FpsakKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fagsak.dto.*;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.StatusRange;

public class FagsakKlient extends FpsakKlient{
    
    private static String STATUS_URL_FORMAT = "/fagsak/status?saksnummer=%1$s&gruppe=%2$s";
    private static String FAGSAK_URL_FORMAT = "/fagsak?saksnummer=%1$s";
    private static String FAGSAK_SØK_URL_FORMAT = "/fagsak/sok";

    public FagsakKlient(HttpSession session) {
        super(session);
    }

    public Status status(int saksnummer, int gruppe) throws IOException {
        String url = hentRestRotUrl() + String.format(STATUS_URL_FORMAT, saksnummer, gruppe);
        return getOgHentJson(url, Status.class, StatusRange.STATUS_NO_SERVER_ERROR);
    }
    
    public Fagsak getFagsak(String saksnummer) throws IOException {
        String url = hentRestRotUrl() + String.format(FAGSAK_URL_FORMAT, saksnummer);
        return getOgHentJson(url, Fagsak.class, StatusRange.STATUS_200);
    }
    
    public ArrayList<Fagsak> søk(String søk) throws IOException {
        return søk(new Sok(søk));
    }
    
    public ArrayList<Fagsak> søk(Sok søk) throws IOException {
        String url = hentRestRotUrl() + FAGSAK_SØK_URL_FORMAT;
        return postOgHentJson(url, søk, hentObjectMapper().getTypeFactory().constructCollectionType(List.class, Fagsak.class), StatusRange.STATUS_200);
    }
}
