package no.nav.tjeneste.virksomhet.arbeidsforhold.v3;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.Test;

import no.nav.foreldrepenger.mock.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.ObjectFactory;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.NorskIdent;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Periode;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Regelverker;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.meldinger.FinnArbeidsforholdPrArbeidstakerRequest;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.meldinger.FinnArbeidsforholdPrArbeidstakerResponse;



public class ArbeidsforholdMockTest {

    private ObjectFactory objectFactory = new ObjectFactory();
    private static final String FNR = "03108940181";
    private static final String REGELVERK = "ALLE";

    @Test
    public void skal_finnArbeidsforholdPrArbeidstaker() throws Exception {
        FinnArbeidsforholdPrArbeidstakerRequest request = new FinnArbeidsforholdPrArbeidstakerRequest();

        NorskIdent personIdent = objectFactory.createNorskIdent();
        personIdent.setIdent(FNR);
        request.setIdent(personIdent);

        Periode arbeidsforholdIPeriode = objectFactory.createPeriode();
        arbeidsforholdIPeriode.setFom(ConversionUtils.convertToXMLGregorianCalendar(LocalDate.of(2016, 5, 1)));
        arbeidsforholdIPeriode.setTom(ConversionUtils.convertToXMLGregorianCalendar(LocalDate.of(2017, 10, 31)));
        request.setArbeidsforholdIPeriode(arbeidsforholdIPeriode);

        Regelverker rapportertSomRegelverk = objectFactory.createRegelverker();
        rapportertSomRegelverk.setValue(REGELVERK);
        request.setRapportertSomRegelverk(rapportertSomRegelverk);

        FinnArbeidsforholdPrArbeidstakerResponse response = new ArbeidsforholdMockImpl().finnArbeidsforholdPrArbeidstaker(request);

        assertThat(response).isNotNull();
        assertThat(response.getArbeidsforhold()).isNotEmpty();
    }
}
