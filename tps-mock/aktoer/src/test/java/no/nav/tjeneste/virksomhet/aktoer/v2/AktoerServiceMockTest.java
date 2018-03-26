package no.nav.tjeneste.virksomhet.aktoer.v2;

import static org.assertj.core.api.Assertions.assertThat;

import no.nav.tjeneste.virksomhet.aktoer.v2.binding.HentAktoerIdForIdentPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.aktoer.v2.binding.HentIdentForAktoerIdPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentAktoerIdForIdentRequest;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentAktoerIdForIdentResponse;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentIdentForAktoerIdRequest;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentIdentForAktoerIdResponse;
import org.junit.Test;

public class AktoerServiceMockTest {

    @Test
    public void testHentIdentForAktoerId() throws HentIdentForAktoerIdPersonIkkeFunnet {
        HentIdentForAktoerIdRequest request = new HentIdentForAktoerIdRequest();
        request.setAktoerId("9000000000000");
        AktoerServiceMockImpl aktoerServiceMock = new AktoerServiceMockImpl();
        HentIdentForAktoerIdResponse response = aktoerServiceMock.hentIdentForAktoerId(request);
        assertThat(response).isNotNull();
        assertThat(response.getIdent()).isNotNull();
        assertThat(response.getIdent()).isEqualTo("07078518434");
    }

    @Test
    public void testHentAktoerIdForIdent() throws HentAktoerIdForIdentPersonIkkeFunnet {
        HentAktoerIdForIdentRequest request = new HentAktoerIdForIdentRequest();
        request.setIdent("07078518434");
        AktoerServiceMockImpl aktoerServiceMock = new AktoerServiceMockImpl();
        HentAktoerIdForIdentResponse response = aktoerServiceMock.hentAktoerIdForIdent(request);
        assertThat(response).isNotNull();
        assertThat(response.getAktoerId()).isNotNull();
        assertThat(response.getAktoerId()).isEqualTo("9000000000000");
    }

}
