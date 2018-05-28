package no.nav.engangsstønad.mock;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import no.nav.foreldrepenger.mock.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentPersonhistorikkPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentPersonhistorikkSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.person.v3.binding.PersonV3;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.AktoerId;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Periode;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonhistorikkRequest;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonhistorikkResponse;

/**
 * For å debugge og teste service lokalt.
 */
public class PersonServiceMockTest {

    public static void main(String[] args) throws MalformedURLException, HentPersonhistorikkSikkerhetsbegrensning, HentPersonhistorikkPersonIkkeFunnet {
        URL wsdlUrl = new URL("https://localhost:8088/person?wsdl");
        QName serviceName = new QName("http://nav.no/tjeneste/virksomhet/person/v3", "PersonServiceMockImplService");
        Service service = Service.create(wsdlUrl, serviceName);
        PersonV3 port = service.getPort(PersonV3.class);

        HentPersonhistorikkResponse response = port.hentPersonhistorikk(personhistorikkRequest());
    }

    private static HentPersonhistorikkRequest personhistorikkRequest() {
        HentPersonhistorikkRequest request = new HentPersonhistorikkRequest();

        Periode periode = new Periode();
        periode.setFom(ConversionUtils.convertToXMLGregorianCalendar(LocalDate.of(1900, 1, 1)));
        periode.setTom(ConversionUtils.convertToXMLGregorianCalendar(LocalDate.now()));
        request.setPeriode(periode);

        AktoerId aktoer = new AktoerId();
        aktoer.setAktoerId("9000000029091");
        request.setAktoer(aktoer);

        return request;
    }
}
