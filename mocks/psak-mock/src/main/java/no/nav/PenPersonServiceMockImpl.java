package no.nav;

import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.inf.pen.person.*;
import no.nav.lib.pen.psakpselv.asbo.person.ASBOPenFinnAdresseListeRequest;
import no.nav.lib.pen.psakpselv.asbo.person.ASBOPenFinnAdresseListeResponse;
import no.nav.lib.pen.psakpselv.asbo.person.ASBOPenHentFamilierelasjonerRequest;
import no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import java.util.Optional;


@WebService(targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson", name = "PENPerson")
@XmlSeeAlso({no.nav.lib.pen.psakpselv.fault.ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.person.ObjectFactory.class, ObjectFactory.class})
@HandlerChain(file = "Handler-chain.xml")
public class PenPersonServiceMockImpl implements PENPerson {
    private static final Logger LOG = LoggerFactory.getLogger(PenPersonServiceMockImpl.class);
    private final TestscenarioBuilderRepository repo;

    public PenPersonServiceMockImpl(TestscenarioBuilderRepository repo) {
        this.repo = repo;
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "hentPerson", targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson", className = "no.nav.inf.pen.person.HentPerson")
    @ResponseWrapper(localName = "hentPersonResponse", targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson", className = "no.nav.inf.pen.person.HentPersonResponse")
    @WebResult(name = "hentPersonResponse", targetNamespace = "")
    public no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson hentPerson(
            @WebParam(name = "hentPersonRequest", targetNamespace = "")
                    no.nav.lib.pen.psakpselv.asbo.person.ASBOPenHentPersonRequest hentPersonRequest
    ) throws HentPersonFaultPenPersonIkkeFunnetMsg {
        return getASBOPenPerson(hentPersonRequest.getPerson().getFodselsnummer()).orElseThrow(HentPersonFaultPenPersonIkkeFunnetMsg::new);
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "hentFamilierelasjonsHistorikk", targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson", className = "no.nav.inf.pen.person.HentFamilierelasjonsHistorikk")
    @ResponseWrapper(localName = "hentFamilierelasjonsHistorikkResponse", targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson", className = "no.nav.inf.pen.person.HentFamilierelasjonsHistorikkResponse")
    @WebResult(name = "hentFamilierelasjonsHistorikkResponse", targetNamespace = "")
    public no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson hentFamilierelasjonsHistorikk(
            @WebParam(name = "hentFamilierelasjonsHistorikkRequest", targetNamespace = "")
                    no.nav.lib.pen.psakpselv.asbo.person.ASBOPenHentFamilierelasjonsHistorikkRequest hentFamilierelasjonsHistorikkRequest
    ) throws HentFamilierelasjonsHistorikkFaultPenPersonIkkeFunnetMsg, HentFamilierelasjonsHistorikkFaultPenGeneriskMsg {
        return getASBOPenPerson(hentFamilierelasjonsHistorikkRequest.getFnr()).orElseThrow(HentFamilierelasjonsHistorikkFaultPenPersonIkkeFunnetMsg::new);
    }

    @Override
    public ASBOPenFinnAdresseListeResponse finnAdresseListe(ASBOPenFinnAdresseListeRequest finnAdresseListeRequest) throws FinnAdresseListeFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "hentPersonUtland", targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson", className = "no.nav.inf.pen.person.HentPersonUtland")
    @ResponseWrapper(localName = "hentPersonUtlandResponse", targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson", className = "no.nav.inf.pen.person.HentPersonUtlandResponse")
    @WebResult(name = "hentPersonUtlandResponse", targetNamespace = "")
    public no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson hentPersonUtland(
            @WebParam(name = "hentPersonUtlandRequest", targetNamespace = "")
                    no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson hentPersonUtlandRequest
    ) throws HentPersonUtlandFaultPenPersonIkkeFunnetMsg, HentPersonUtlandFaultPenGeneriskMsg {
        return getASBOPenPerson(hentPersonUtlandRequest.getFodselsnummer()).orElseThrow(HentPersonUtlandFaultPenPersonIkkeFunnetMsg::new);
    }

    @Override
    public ASBOPenPerson hentFamilierelasjoner(ASBOPenHentFamilierelasjonerRequest hentFamilierelasjonerRequest) throws HentFamilierelasjonerFaultPenPersonIkkeFunnetMsg, HentFamilierelasjonerFaultPenGeneriskMsg {
        return getASBOPenPerson(hentFamilierelasjonerRequest.getFodselsnummer()).orElseThrow(HentFamilierelasjonerFaultPenPersonIkkeFunnetMsg::new);
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "hentHistorikk", targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson", className = "no.nav.inf.pen.person.HentHistorikk")
    @ResponseWrapper(localName = "hentHistorikkResponse", targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson", className = "no.nav.inf.pen.person.HentHistorikkResponse")
    @WebResult(name = "hentHistorikkResponse", targetNamespace = "")
    public no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson hentHistorikk(
            @WebParam(name = "hentHistorikkRequest", targetNamespace = "")
                    no.nav.lib.pen.psakpselv.asbo.person.ASBOPenHentHistorikkRequest hentHistorikkRequest
    ) throws HentHistorikkFaultPenPersonIkkeFunnetMsg, HentHistorikkFaultPenGeneriskMsg {
        return getASBOPenPerson(hentHistorikkRequest.getFodselsnummer()).orElseThrow(HentHistorikkFaultPenPersonIkkeFunnetMsg::new);
    }

    private Optional<ASBOPenPerson> getASBOPenPerson(String fodselsnummer) {
        return repo.getPersonIndeks().getAlleSøkere().parallelStream()
                        .filter(p -> p.getSøker().getIdent().equals(fodselsnummer))
                        .map(PsakPersonAdapter::toASBOPerson)
                        .findFirst()
                        .or(() -> PsakPersonAdapter.getPreviouslyConverted(fodselsnummer))
                        .or(() -> logIkkeFunnet(fodselsnummer));
    }

    private Optional<ASBOPenPerson> logIkkeFunnet(String fodselsnummer) {
        LOG.warn("Klarte ikke å finne person med fnr: " + fodselsnummer + " verken i repo eller blant konverterte " + PsakPersonAdapter.getPreviouslyConverted());
        return Optional.empty();
    }
}
