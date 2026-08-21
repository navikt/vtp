package no.nav.vtp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;

import org.junit.jupiter.api.Test;

import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

class BrregMockTest {

    @Test
    void skalReturnereStandardNæring() {
        var uriInfo = mock(UriInfo.class);
        var uriBuilder = mock(UriBuilder.class);
        when(uriInfo.getBaseUriBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.path("dummy/brreg/enheter/{organisasjonsnummer}")).thenReturn(uriBuilder);
        when(uriBuilder.build("999999999")).thenReturn(URI.create("http://localhost:8060/rest/dummy/brreg/enheter/999999999"));
        var mock = new BrregMock();

        var rolleutskrift = mock.hentRolleutskrift(uriInfo);

        assertThat(rolleutskrift.enheter()).singleElement().satisfies(enhet -> {
            assertThat(enhet.organisasjonsnummer()).isEqualTo("999999999");
            assertThat(enhet.navn()).isEqualTo("VTP FISKE");
            assertThat(enhet.roller()).singleElement()
                .satisfies(rolle -> assertThat(rolle.type().kode()).isEqualTo("INNH"));
            assertThat(enhet._links().enhet().href())
                .hasToString("http://localhost:8060/rest/dummy/brreg/enheter/999999999");
        });
        assertThat(mock.hentEnhet().organisasjonsform().kode()).isEqualTo("ENK");
        assertThat(mock.hentEnhet().naeringskode1().kode()).isEqualTo("03.110");
    }
}
