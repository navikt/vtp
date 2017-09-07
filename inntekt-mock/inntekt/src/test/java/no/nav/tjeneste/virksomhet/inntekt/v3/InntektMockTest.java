package no.nav.tjeneste.virksomhet.inntekt.v3;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.Test;

import no.nav.foreldrepenger.mock.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.ObjectFactory;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.PersonIdent;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.Uttrekksperiode;
import no.nav.tjeneste.virksomhet.inntekt.v3.meldinger.HentInntektListeBolkRequest;
import no.nav.tjeneste.virksomhet.inntekt.v3.meldinger.HentInntektListeBolkResponse;

public class InntektMockTest {

    private ObjectFactory objectFactory = new ObjectFactory();
    private static final String FNR = "08088049846";

    @Test
    public void skal_hente_inntekter() throws Exception {
        HentInntektListeBolkRequest request = new HentInntektListeBolkRequest();

        PersonIdent personIdent = objectFactory.createPersonIdent();
        personIdent.setPersonIdent(FNR);
        request.getIdentListe().add(personIdent);

        Uttrekksperiode uttrekksperiode = objectFactory.createUttrekksperiode();
        uttrekksperiode.setMaanedFom(ConversionUtils.convertToXMLGregorianCalendar(LocalDate.of(2017, 1, 1)));
        uttrekksperiode.setMaanedTom(ConversionUtils.convertToXMLGregorianCalendar(LocalDate.of(2017, 12, 31)));
        request.setUttrekksperiode(uttrekksperiode);

        HentInntektListeBolkResponse response = new InntektMockImpl().hentInntektListeBolk(request);

        assertThat(response).isNotNull();
        assertThat(response.getArbeidsInntektIdentListe()).isNotEmpty();
    }
}
