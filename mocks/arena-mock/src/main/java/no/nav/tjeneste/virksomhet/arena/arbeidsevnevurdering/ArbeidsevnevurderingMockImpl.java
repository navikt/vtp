package no.nav.tjeneste.virksomhet.arena.arbeidsevnevurdering;

import no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.binding.ArbeidsevnevurderingV1;
import no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.binding.FinnArbeidsevnevurderingPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.binding.FinnArbeidsevnevurderingSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.binding.FinnArbeidsevnevurderingUgyldigInput;
import no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.informasjon.arbeidsevnevurdering.Arbeidsevnevurdering;
import no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.meldinger.FinnArbeidsevnevurderingRequest;
import no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.meldinger.FinnArbeidsevnevurderingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.HandlerChain;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;
import javax.xml.ws.soap.Addressing;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.GregorianCalendar;

@Addressing
@WebService(endpointInterface = "no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.binding.ArbeidsevnevurderingV1")
@HandlerChain(file = "Handler-chain.xml")
public class ArbeidsevnevurderingMockImpl implements ArbeidsevnevurderingV1 {
    private static final Logger LOG = LoggerFactory.getLogger(ArbeidsevnevurderingMockImpl.class);


    @Override
    @WebResult(name = "response", targetNamespace = "")
    public FinnArbeidsevnevurderingResponse finnArbeidsevnevurdering(@WebParam(name = "request", targetNamespace = "")FinnArbeidsevnevurderingRequest finnArbeidsevnevurderingRequest)
            throws FinnArbeidsevnevurderingPersonIkkeFunnet, FinnArbeidsevnevurderingSikkerhetsbegrensning, FinnArbeidsevnevurderingUgyldigInput {
        LOG.info("ArbeidsevnevurderingV1:finnArbeidsevnevurdering kalt");

        FinnArbeidsevnevurderingResponse response = new FinnArbeidsevnevurderingResponse();
        response.setForeliggerArbeidsevnevurdering(true);

        Arbeidsevnevurdering arbeidsevnevurdering = new Arbeidsevnevurdering();
        arbeidsevnevurdering.setErVarigTilpassetInnsats(true);
        arbeidsevnevurdering.setFomVarigTilrettelagtArbeid(xmlGregorianCalendarFromLocalDate(LocalDate.now().minusMonths(10)));
        arbeidsevnevurdering.setGodkjentArbeidsevnevurdering(true);
        arbeidsevnevurdering.setUtfallArbeidsevnevurdering("Veldig god vurdering");
        response.setArbeidsevnevurdering(arbeidsevnevurdering);

        return response;

    }

    @Override
    public void ping(Holder<Object> holder) {
        LOG.info("ArbeidsevnevurderingV1:ping kalt");

    }


    private XMLGregorianCalendar xmlGregorianCalendarFromLocalDate(LocalDate date){
        GregorianCalendar gcal = GregorianCalendar.from(date.atStartOfDay(ZoneId.systemDefault()));
        XMLGregorianCalendar xcal = null;
        try {
            xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        return xcal;
    }
}
