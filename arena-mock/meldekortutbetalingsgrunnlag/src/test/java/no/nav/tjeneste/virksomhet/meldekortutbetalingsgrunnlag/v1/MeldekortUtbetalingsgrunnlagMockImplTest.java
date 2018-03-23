package no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1;


import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.binding.FinnMeldekortUtbetalingsgrunnlagListeAktoerIkkeFunnet;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.binding.FinnMeldekortUtbetalingsgrunnlagListeSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.binding.FinnMeldekortUtbetalingsgrunnlagListeUgyldigInput;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.informasjon.AktoerId;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.meldinger.FinnMeldekortUtbetalingsgrunnlagListeRequest;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.informasjon.ObjectFactory;

import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MeldekortUtbetalingsgrunnlagMockImplTest {
    private final String IDENT = "9000000000803";
    private ObjectFactory objectFactory = new ObjectFactory();

    @Test
    public void testFinnMeldekortUtbetalingsgrunnlagListeResponse() throws FinnMeldekortUtbetalingsgrunnlagListeSikkerhetsbegrensning, FinnMeldekortUtbetalingsgrunnlagListeAktoerIkkeFunnet, FinnMeldekortUtbetalingsgrunnlagListeUgyldigInput {

        MeldekortUtbetalingsgrunnlagMockImpl hentYtelse = new MeldekortUtbetalingsgrunnlagMockImpl();
        FinnMeldekortUtbetalingsgrunnlagListeRequest request = new FinnMeldekortUtbetalingsgrunnlagListeRequest();
        AktoerId aktoerId = objectFactory.createAktoerId();
        aktoerId.setAktoerId(IDENT);
        request.setIdent(aktoerId);

        try {
            no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.meldinger.FinnMeldekortUtbetalingsgrunnlagListeResponse response =
                    hentYtelse.finnMeldekortUtbetalingsgrunnlagListe(request);
            assertThat(response).isNotNull();
        } catch (Exception e) {
            throw e;
        }
    }
}
