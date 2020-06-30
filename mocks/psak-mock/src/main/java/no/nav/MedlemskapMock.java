package no.nav;

import no.nav.virksomhet.tjenester.medlemskap.meldinger.v1.HentPeriodeListeResponse;
import no.nav.virksomhet.tjenester.medlemskap.meldinger.v1.HentPeriodeRequest;
import no.nav.virksomhet.tjenester.medlemskap.meldinger.v1.HentPeriodeResponse;
import no.nav.virksomhet.tjenester.medlemskap.v1.HentPeriodeListePersonIkkeFunnet;
import no.nav.virksomhet.tjenester.medlemskap.v1.Medlemskap;
import no.nav.virksomhet.tjenester.medlemskap.v1.ObjectFactory;

import javax.jws.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(targetNamespace = "http://nav.no/virksomhet/tjenester/medlemskap/v1", name = "Medlemskap")
@XmlSeeAlso({ObjectFactory.class, no.nav.virksomhet.tjenester.medlemskap.feil.v1.ObjectFactory.class, no.nav.virksomhet.tjenester.medlemskap.meldinger.v1.ObjectFactory.class, no.nav.virksomhet.tjenester.felles.v1.ObjectFactory.class, no.nav.virksomhet.grunnlag.medlemskap.v1.ObjectFactory.class})
@HandlerChain(file = "Handler-chain.xml")
public class MedlemskapMock implements Medlemskap {
    /**
     * <p>Operasjonen skal tilby å hente en liste med medlemskapsperioder til en person.</p>
     *
     * @param request
     */
    @Override
    @WebMethod
    @RequestWrapper(localName = "hentPeriodeListe", targetNamespace = "http://nav.no/virksomhet/tjenester/medlemskap/v1", className = "no.nav.virksomhet.tjenester.medlemskap.v1.HentPeriodeListe")
    @ResponseWrapper(localName = "hentPeriodeListeResponse", targetNamespace = "http://nav.no/virksomhet/tjenester/medlemskap/v1", className = "no.nav.virksomhet.tjenester.medlemskap.v1.HentPeriodeListeResponse")
    @WebResult(name = "response", targetNamespace = "")
    public no.nav.virksomhet.tjenester.medlemskap.meldinger.v1.HentPeriodeListeResponse hentPeriodeListe(
            @WebParam(name = "request", targetNamespace = "")
                    no.nav.virksomhet.tjenester.medlemskap.meldinger.v1.HentPeriodeListeRequest request
    ) throws HentPeriodeListePersonIkkeFunnet {
        return new HentPeriodeListeResponse();
    }

    /**
     * <p>Operasjonen skal tilby å hente en medlemskapsperiode.</p>
     *
     * @param request
     */
    @Override
    public HentPeriodeResponse hentPeriode(HentPeriodeRequest request) {
        throw new UnsupportedOperationException("Ikke implementert");
    }
}
