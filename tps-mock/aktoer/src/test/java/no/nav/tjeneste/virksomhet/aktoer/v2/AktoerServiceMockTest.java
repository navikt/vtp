package no.nav.tjeneste.virksomhet.aktoer.v2;

import no.nav.tjeneste.virksomhet.aktoer.v2.binding.HentAktoerIdForIdentPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.aktoer.v2.binding.HentIdentForAktoerIdPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentAktoerIdForIdentListeRequest;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentAktoerIdForIdentListeResponse;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentAktoerIdForIdentRequest;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentAktoerIdForIdentResponse;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentIdentForAktoerIdListeRequest;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentIdentForAktoerIdListeResponse;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentIdentForAktoerIdRequest;
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.HentIdentForAktoerIdResponse;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
    public void testHentIdentForAktoerIdHardkodetVerdi() throws HentIdentForAktoerIdPersonIkkeFunnet {
        HentIdentForAktoerIdRequest request = new HentIdentForAktoerIdRequest();
        request.setAktoerId("1000021543419");
        AktoerServiceMockImpl aktoerServiceMock = new AktoerServiceMockImpl();
        HentIdentForAktoerIdResponse response = aktoerServiceMock.hentIdentForAktoerId(request);
        assertThat(response).isNotNull();
        assertThat(response.getIdent()).isNotNull();
        assertThat(response.getIdent()).isEqualTo("06016518156");
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
