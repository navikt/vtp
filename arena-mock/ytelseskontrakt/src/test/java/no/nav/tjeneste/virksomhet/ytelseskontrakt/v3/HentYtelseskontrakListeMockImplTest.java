package no.nav.tjeneste.virksomhet.ytelseskontrakt.v3;


import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.binding.HentYtelseskontraktListeSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.meldinger.*;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.meldinger.HentYtelseskontraktListeResponse;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HentYtelseskontrakListeMockImplTest {
    private final String IDENT = "26048121054";

    @Test
    public void testHentYtelseskontraktListeResponse() throws HentYtelseskontraktListeSikkerhetsbegrensning {

            HentYtelseskontraktListeMockImpl hentYtelse = new HentYtelseskontraktListeMockImpl();
            HentYtelseskontraktListeRequest request = new HentYtelseskontraktListeRequest();
            request.setPersonidentifikator(IDENT);
            no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.meldinger.HentYtelseskontraktListeResponse response = hentYtelse.hentYtelseskontraktListe(request);

            assertThat(response).isNotNull();
        }
    }

