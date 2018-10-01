package no.nav.foreldrepenger.autotest.klienter.fpsak.fordel;

import java.io.IOException;

import no.nav.foreldrepenger.autotest.klienter.fpsak.FpsakKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.fordel.dto.*;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.StatusRange;

public class FordelKlient extends FpsakKlient{
    
    private static final String FORDEL_URL = "/fordel";
    private static final String VURDER_FAGSYSTEM_URL = FORDEL_URL + "/vurderFagsystem";
    private static final String JOURNALPOST_URL = FORDEL_URL + "/journalpost";
    
    private static final String FAGSAK_URL = FORDEL_URL + "/fagsak";
    private static final String FAGSAK_OPPRETT_URL = FAGSAK_URL + "/opprett";
    private static final String FAGSAK_INFORMASJON_URL = FAGSAK_URL + "/informasjon";
    private static final String FAGSAK_KNYTT_JOURNALPOST_URL = FAGSAK_URL + "/knyttJournalpost";
    
    
    public FordelKlient(HttpSession session) {
        super(session);
    }
    
    
    public BehandlendeFagsystem vurderFagsystem(VurderFagsystem vurderFagsystem) throws IOException {
        String url = hentRestRotUrl() + VURDER_FAGSYSTEM_URL;
        return postOgHentJson(url, vurderFagsystem, BehandlendeFagsystem.class, StatusRange.STATUS_SUCCESS);
    }
    
    public void journalpost(JournalpostMottak journalpostMottak) throws IOException {
        String url = hentRestRotUrl() + JOURNALPOST_URL;
        postOgVerifiser(url, journalpostMottak, StatusRange.STATUS_SUCCESS);
    }
    
    public Saksnummer fagsakOpprett(OpprettSak journalpost) throws IOException {
        String url = hentRestRotUrl() + FAGSAK_OPPRETT_URL;
        return postOgHentJson(url, journalpost, Saksnummer.class, StatusRange.STATUS_SUCCESS);
    }
    
    public FagsakInformasjon fagsakInformasjon(Saksnummer id) throws IOException {
        String url = hentRestRotUrl() + FAGSAK_INFORMASJON_URL;
        return postOgHentJson(url, id, FagsakInformasjon.class, StatusRange.STATUS_SUCCESS);
    }
    
    public void fagsakKnyttJournalpost(JournalpostKnyttning knyttJournalpost) throws IOException {
        String url = hentRestRotUrl() + FAGSAK_KNYTT_JOURNALPOST_URL;
        postOgVerifiser(url, knyttJournalpost, StatusRange.STATUS_SUCCESS);
    }
}
