package no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1;

import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.binding.FinnGrunnlagListePersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.binding.FinnGrunnlagListeSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.binding.FinnGrunnlagListeUgyldigInput;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.meldinger.*;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;


public class FinnGrunnlagListeMockTest {

    private final String IDENT = "19069011368";

    @Test
    public void testFinnGrunnlagListeResponse() throws FinnGrunnlagListePersonIkkeFunnet, FinnGrunnlagListeSikkerhetsbegrensning, FinnGrunnlagListeUgyldigInput
    {
        try {
            FinnGrunnlagListeMockImpl finnGrunnlagListeMockImpl = new FinnGrunnlagListeMockImpl();
            FinnGrunnlagListeRequest request = new FinnGrunnlagListeRequest();
            request.setPersonident(IDENT);
            no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.meldinger.FinnGrunnlagListeResponse response = finnGrunnlagListeMockImpl.finnGrunnlagListe(request);

            assertThat(response).isNotNull();
//        assertThat(response.getSakListe()).isNotNull();
//        assertThat(response.getSakListe().get(0).getSakId()).isNotNull();
//        assertThat(response.getSakListe().get(0).getRegistrert()).isNotNull();
        assertThat(response.getSykepengerListe().get(0).getBehandlingstema().getValue().equals("SP")).isTrue();
        assertThat(response.getSykepengerListe().get(0).getVedtakListe().isEmpty()).isFalse();
//        assertThat(response.getSakListe().get(0).getBehandlingstema()).isNotNull();
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
