package no.nav.tjeneste.virksomhet.infotrygdsak.v1;

import no.nav.tjeneste.virksomhet.infotrygdsak.v1.binding.FinnSakListePersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.binding.FinnSakListeSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.binding.FinnSakListeUgyldigInput;
import no.nav.tjeneste.virksomhet.infotrygdsak.v1.meldinger.*;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;


public class FinnSakListeMockTest {

    private final String IDENT = "19069011368";

    @Test
    public void testFinnSakListeResponse() throws FinnSakListePersonIkkeFunnet, FinnSakListeSikkerhetsbegrensning, FinnSakListeUgyldigInput
    {
        try {
            FinnSakListeMockImpl finnSakListeMockImpl = new FinnSakListeMockImpl();
            FinnSakListeRequest request = new FinnSakListeRequest();
            request.setPersonident(IDENT);
            no.nav.tjeneste.virksomhet.infotrygdsak.v1.meldinger.FinnSakListeResponse response = finnSakListeMockImpl.finnSakListe(request);

            assertThat(response).isNotNull();
//        assertThat(response.getSakListe()).isNotNull();
//        assertThat(response.getSakListe().get(0).getSakId()).isNotNull();
//        assertThat(response.getSakListe().get(0).getRegistrert()).isNotNull();
        assertThat(response.getSakListe().get(0).getTema().getValue().equals("SP")).isTrue();
        assertThat(response.getSakListe().get(0).getBehandlingstema().getValue().equals("SP")).isTrue();
//        assertThat(response.getSakListe().get(0).getStatus()).isNotNull();
//        assertThat(response.getSakListe().get(0).getType()).isNotNull();
//        assertThat(response.getSakListe().get(0).getResultat()).isNotNull();
//        assertThat(response.getSakListe().get(0).getEndret()).isNotNull();
//        assertThat(response.getSakListe().get(0).getIverksatt()).isNotNull();
//        assertThat(response.getSakListe().get(0).getVedtatt()).isNotNull();
//        assertThat(response.getSakListe().get(0).getOpphoerFom()).isNotNull();
        }
        catch (Exception e){
            e.getMessage();
        }

    }
}
