package no.nav.tjeneste.virksomhet.infotrygdsak.v1;

import no.nav.tjeneste.virksomhet.infotrygdsak.v1.binding.FinnSakListePersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.binding.FinnSakListeSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.binding.FinnSakListeUgyldigInput;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.meldinger.*;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;


public class FinnSakListeMockImplTest {

    private final String IDENT_MED_SVAR_YTELSE_OG_VEDTAK = "19069011368";
    private final String IDENT_UTEN_TABELLINNSLAG = "19069011369";

    @Test
    public void finnSakListeResponseGrunnleggendeTest() throws FinnSakListePersonIkkeFunnet, FinnSakListeSikkerhetsbegrensning, FinnSakListeUgyldigInput
    {
        FinnSakListeMockImpl finnSakListeMockImpl = new FinnSakListeMockImpl();
        FinnSakListeRequest request = new FinnSakListeRequest();
        request.setPersonident(IDENT_MED_SVAR_YTELSE_OG_VEDTAK);
        no.nav.tjeneste.virksomhet.infotrygdsak.v1.meldinger.FinnSakListeResponse response = finnSakListeMockImpl.finnSakListe(request);

        assertThat(response).isNotNull();
        assertThat(response.getVedtakListe()).isNotNull();
        assertThat(response.getVedtakListe()).isNotEmpty();
        assertThat(response.getVedtakListe().get(0).getSakId()).isNotNull();
        assertThat(response.getVedtakListe().get(0).getRegistrert()).isNotNull();
        assertThat(response.getVedtakListe().get(0).getTema().getValue().equals("SP")).isTrue();
        assertThat(response.getVedtakListe().get(0).getBehandlingstema().getValue().equals("SP")).isTrue();

    }

    @Test
    public void finnSakListeResponseHaandtererFnrMedSakSomIkkeFinnes() throws FinnSakListePersonIkkeFunnet, FinnSakListeSikkerhetsbegrensning, FinnSakListeUgyldigInput
    {
        FinnSakListeMockImpl finnSakListeMockImpl = new FinnSakListeMockImpl();
        FinnSakListeRequest request = new FinnSakListeRequest();
        request.setPersonident(IDENT_UTEN_TABELLINNSLAG);
        no.nav.tjeneste.virksomhet.infotrygdsak.v1.meldinger.FinnSakListeResponse response = finnSakListeMockImpl.finnSakListe(request);

        assertThat(response).isNull();

    }
}
