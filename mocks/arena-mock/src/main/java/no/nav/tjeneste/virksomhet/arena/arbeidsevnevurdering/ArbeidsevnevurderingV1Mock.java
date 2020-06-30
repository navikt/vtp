package no.nav.tjeneste.virksomhet.arena.arbeidsevnevurdering;

import no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.binding.ArbeidsevnevurderingV1;
import no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.binding.FinnArbeidsevnevurderingPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.binding.FinnArbeidsevnevurderingSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.binding.FinnArbeidsevnevurderingUgyldigInput;
import no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.informasjon.arbeidsevnevurdering.Arbeidsevnevurdering;
import no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.meldinger.FinnArbeidsevnevurderingRequest;
import no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.meldinger.FinnArbeidsevnevurderingResponse;

import javax.jws.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.GregorianCalendar;

@WebService(name = "Arbeidsevnevurdering_v1", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsevnevurdering/v1")
@HandlerChain(file = "Handler-chain.xml")
public class ArbeidsevnevurderingV1Mock implements ArbeidsevnevurderingV1 {
    /**
     * @param request
     * @return returns no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.meldinger.FinnArbeidsevnevurderingResponse
     * @throws FinnArbeidsevnevurderingPersonIkkeFunnet
     * @throws FinnArbeidsevnevurderingUgyldigInput
     * @throws FinnArbeidsevnevurderingSikkerhetsbegrensning
     */
    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/arbeidsevnevurdering/v1/BindingfinnArbeidsevnevurdering/")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "finnArbeidsevnevurdering", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsevnevurdering/v1", className = "no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.FinnArbeidsevnevurdering")
    @ResponseWrapper(localName = "finnArbeidsevnevurderingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsevnevurdering/v1", className = "no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.FinnArbeidsevnevurderingResponse")
    public FinnArbeidsevnevurderingResponse finnArbeidsevnevurdering(
            @WebParam(name = "request", targetNamespace = "")
                    FinnArbeidsevnevurderingRequest request)
            throws FinnArbeidsevnevurderingPersonIkkeFunnet, FinnArbeidsevnevurderingSikkerhetsbegrensning, FinnArbeidsevnevurderingUgyldigInput
    {
        FinnArbeidsevnevurderingResponse response = new FinnArbeidsevnevurderingResponse();
        response.setForeliggerArbeidsevnevurdering(true);

        Arbeidsevnevurdering arbeidsevnevurdering = new Arbeidsevnevurdering();
        arbeidsevnevurdering.setErVarigTilpassetInnsats(true);
        arbeidsevnevurdering.setFomVarigTilrettelagtArbeid(xmlGregorianCalendarFromLocalDate(LocalDate.now().minusMonths(10)));
        arbeidsevnevurdering.setGodkjentArbeidsevnevurdering(true);
        arbeidsevnevurdering.setUtfallArbeidsevnevurdering("Veldig god vurdering YES");
        response.setArbeidsevnevurdering(arbeidsevnevurdering);

        return response;
    }

    /**
     * @param plassholder
     */
    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/arbeidsevnevurdering/v1/Bindingping/")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsevnevurdering/v1", className = "no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsevnevurdering/v1", className = "no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.PingResponse")
    public void ping(
            @WebParam(name = "plassholder", targetNamespace = "", mode = WebParam.Mode.INOUT)
                    Holder<Object> plassholder) {

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
