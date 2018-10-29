package no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.FpsakKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.BehandlendeEnhet;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kodeverk;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kodeverk.KodeListe;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.StatusRange;

public class KodeverkKlient extends FpsakKlient{

    private static String KODEVERK_URL = "/kodeverk";
    
    private static String KODEVERK_BEHANDLENDE_ENHETER_URL = KODEVERK_URL + "/behandlende-enheter";
    
    private static String KODEVERK_HENLEGG_ÅRSAKER = KODEVERK_URL + "/henlegg/arsaker";
    private static String KODEVERK_HENLEGG_ÅRSAKER_KLAGE = KODEVERK_HENLEGG_ÅRSAKER + "/klage";
    private static String KODEVERK_HENLEGG_ÅRSAKER_INNSYN = KODEVERK_HENLEGG_ÅRSAKER + "/innsyn";
    
    public KodeverkKlient(HttpSession session) {
        super(session);
    }
    
    
    public Kodeverk getKodeverk() throws IOException{
        String url = hentRestRotUrl() + KODEVERK_URL;
        return getOgHentJson(url, Kodeverk.class, StatusRange.STATUS_SUCCESS);
    }
    
    public List<BehandlendeEnhet> behandlendeEnheter() throws IOException{
        String url = hentRestRotUrl() + KODEVERK_BEHANDLENDE_ENHETER_URL;
        return getOgHentJson(url, hentObjectMapper().getTypeFactory().constructCollectionType(ArrayList.class, BehandlendeEnhet.class), StatusRange.STATUS_SUCCESS);
    }
    
    public KodeListe henleggArsaker() throws IOException{
        String url = hentRestRotUrl() + KODEVERK_HENLEGG_ÅRSAKER;
        return getOgHentJson(url, hentObjectMapper().getTypeFactory().constructCollectionType(KodeListe.class,Kode.class), StatusRange.STATUS_SUCCESS);
    }
    
    public KodeListe henleggArsakerKlage() throws IOException{
        String url = hentRestRotUrl() + KODEVERK_HENLEGG_ÅRSAKER_KLAGE;
        return getOgHentJson(url, hentObjectMapper().getTypeFactory().constructCollectionType(KodeListe.class,Kode.class), StatusRange.STATUS_SUCCESS);
    }
    
    public KodeListe henleggArsakerInnsyn() throws IOException{
        String url = hentRestRotUrl() + KODEVERK_HENLEGG_ÅRSAKER_INNSYN;
        return getOgHentJson(url, hentObjectMapper().getTypeFactory().constructCollectionType(KodeListe.class,Kode.class), StatusRange.STATUS_SUCCESS);
    }
    
    
}
