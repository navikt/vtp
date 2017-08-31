package no.nav.tjeneste.virksomhet.medlemskap.v2;

import org.junit.Test;

import no.nav.tjeneste.virksomhet.medlemskap.v2.informasjon.Foedselsnummer;
import no.nav.tjeneste.virksomhet.medlemskap.v2.meldinger.HentPeriodeListeRequest;
import no.nav.tjeneste.virksomhet.medlemskap.v2.meldinger.HentPeriodeListeResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class MedlemServiceMockTest {

    @Test
    public void skal_hente_periodeliste() throws Exception {
        HentPeriodeListeRequest request = new HentPeriodeListeRequest().withIdent(new Foedselsnummer().withValue("08088049846"));
        HentPeriodeListeResponse response = new MedlemServiceMockImpl().hentPeriodeListe(request);
        assertThat(response).isNotNull();
        assertThat(response.getPeriodeListe()).isNotEmpty();
    }
}
