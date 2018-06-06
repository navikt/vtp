package no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1;

import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.binding.FinnGrunnlagListePersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.binding.FinnGrunnlagListeSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.binding.FinnGrunnlagListeUgyldigInput;
import no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.meldinger.*;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class FinnGrunnlagListeMockTest {

    private final String IDENT = "19069011368";

    @Test
    public void testFinnGrunnlagListeResponse() throws FinnGrunnlagListePersonIkkeFunnet, FinnGrunnlagListeSikkerhetsbegrensning, FinnGrunnlagListeUgyldigInput
    {

        FinnGrunnlagListeMockImpl finnGrunnlagListeMockImpl = new FinnGrunnlagListeMockImpl();
        FinnGrunnlagListeRequest request = new FinnGrunnlagListeRequest();
        request.setPersonident(IDENT);
        no.nav.tjeneste.virksomhet.infotrygdberegningsgrunnlag.v1.meldinger.FinnGrunnlagListeResponse response = finnGrunnlagListeMockImpl.finnGrunnlagListe(request);

        assertThat(response).isNotNull();
        assertThat(response.getSykepengerListe().get(0).getBehandlingstema().getValue().equals("SP")).isTrue();
        assertThat(response.getSykepengerListe().get(0).getVedtakListe().isEmpty()).isFalse();
        //assertThat(response.getForeldrepengerListe()).isNotNull();
        //assertThat(response.getForeldrepengerListe()).isNotEmpty();

    }
}
