package no.nav.foreldrepenger.autotest.klienter.spberegning.beregning;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import no.nav.foreldrepenger.autotest.klienter.spberegning.SpBeregningKlient;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.ForeslaaDto;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.ForslagDto;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.LagreNotatDto;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.OppdaterBeregningDto;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.PersonDto;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning.BeregningDto;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.inntektsmelding.InntektsmeldingerDto;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.StatusRange;

public class BeregningKlient extends SpBeregningKlient{

    public static final String BEREGNING_URL = "/beregning";
    public static final String BEREGNING_HENT_BEREGNING_URL = BEREGNING_URL + "/hent-beregning";
    public static final String BEREGNING_HENT_PERSON_INFO_URL = BEREGNING_URL + "/hent-person-info";
    public static final String BEREGNING_HENT_INNTEKTSMELDING_URL = BEREGNING_URL + "/hent-inntektsmeldinger";
    public static final String BEREGNING_HENT_ALLE_INNTEKTSMELDINGER_URL = BEREGNING_URL + "/hent-alle-inntektsmeldinger";
    public static final String BEREGNING_FORESLA_BEREGNING_URL = BEREGNING_URL + "/foresla-beregning";
    public static final String BEREGNING_OPPDATER_BEREGNING_URL = BEREGNING_URL + "/oppdater-beregning";
    public static final String BEREGNING_PING_URL = BEREGNING_URL + "/ping";
    public static final String BEREGNING_LAGRE_NOTAT_URL = BEREGNING_URL + "/lagrenotat";
    
    public BeregningKlient(HttpSession session) {
        super(session);
    }
    
    public BeregningDto hentBeregning(int beregningId) throws IOException {
        String url = hentRestRotUrl() + BEREGNING_HENT_BEREGNING_URL + "?beregningId="+beregningId;
        return getOgHentJson(url, BeregningDto.class, StatusRange.STATUS_SUCCESS);
    }
    
    public PersonDto hentPersonInfo(int beregningId) throws IOException {
        String url = hentRestRotUrl() + BEREGNING_HENT_PERSON_INFO_URL + "?beregningId=" + beregningId;
        return getOgHentJson(url, PersonDto.class, StatusRange.STATUS_SUCCESS);
    }
    
    public InntektsmeldingerDto hentInntektsmeldinger(int beregningId) throws IOException {
        String url = hentRestRotUrl() + BEREGNING_HENT_INNTEKTSMELDING_URL + "?beregningId=" + beregningId;
        return getOgHentJson(url, InntektsmeldingerDto.class, StatusRange.STATUS_SUCCESS);
    }
    
    public InntektsmeldingerDto hentAlleInntektsmeldinger(int beregningId) throws IOException {
        String url = hentRestRotUrl() + BEREGNING_HENT_ALLE_INNTEKTSMELDINGER_URL + "?beregningId=" + beregningId;
        return getOgHentJson(url, InntektsmeldingerDto.class, StatusRange.STATUS_SUCCESS);
    }
    
    public void foreslaBeregningGet(String tema, String aktorId, String gosysSakId, String oppgaveId) throws IOException {
        Map<String, String> data = new HashMap<>();
        data.put("tema", tema);
        data.put("aktorId", aktorId);
        data.put("gosysSakId", gosysSakId);
        data.put("oppgaveId", oppgaveId);
        String url = UrlCompose(hentRestRotUrl() + BEREGNING_FORESLA_BEREGNING_URL, data);
        ValidateResponse(get(url), StatusRange.STATUS_SUCCESS);
    }
    
    public ForslagDto foreslaBeregningPost(ForeslaaDto foreslaa) throws IOException {
        String url = hentRestRotUrl() + BEREGNING_FORESLA_BEREGNING_URL;
        return postOgHentJson(url, foreslaa, ForslagDto.class, StatusRange.STATUS_SUCCESS);
    }
    
    public void oppdaterBeregning(OppdaterBeregningDto oppdaterBeregning) throws IOException {
        String url = hentRestRotUrl() + BEREGNING_OPPDATER_BEREGNING_URL;
        postOgVerifiser(url, oppdaterBeregning, StatusRange.STATUS_SUCCESS);
    }
    
    public void ping() throws IOException {
        String url = hentRestRotUrl() + BEREGNING_PING_URL;
        ValidateResponse(get(url), StatusRange.STATUS_SUCCESS);
    }
    
    public void lagrenotat(LagreNotatDto notat) throws IOException {
        String url = hentRestRotUrl() + BEREGNING_LAGRE_NOTAT_URL;
        
    }

}
