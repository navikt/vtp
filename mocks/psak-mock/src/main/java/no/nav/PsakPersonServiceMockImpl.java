package no.nav;

import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.inf.psak.person.*;
import no.nav.lib.pen.psakpselv.asbo.ASBOPenTomRespons;
import no.nav.lib.pen.psakpselv.asbo.person.*;

import javax.jws.*;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

@Addressing
@WebService(targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", name = "PSAKPerson")
@HandlerChain(file = "Handler-chain.xml")
public class PsakPersonServiceMockImpl implements PSAKPerson {
    private final TestscenarioBuilderRepository repo;

    public PsakPersonServiceMockImpl(TestscenarioBuilderRepository repo) {
        this.repo = repo;
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "finnPerson", targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", className = "no.nav.inf.psak.person.FinnPerson")
    @ResponseWrapper(localName = "finnPersonResponse", targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", className = "no.nav.inf.psak.person.FinnPersonResponse")
    @WebResult(name = "finnPersonResponse", targetNamespace = "")
    public ASBOPenFinnPersonResponse finnPerson(
            @WebParam(name = "finnPersonRequest", targetNamespace = "") no.nav.lib.pen.psakpselv.asbo.person.ASBOPenFinnPersonRequest finnPersonRequest
    ) throws FinnPersonFaultPenGeneriskMsg {
        ASBOPenFinnPersonResponse asboPenFinnPersonResponse = new ASBOPenFinnPersonResponse();
        ASBOPenPersonListe liste = new ASBOPenPersonListe();
        liste.setPersoner(repo.getPersonIndeks().getAlleSøkere().parallelStream()
                .map(PsakPersonAdapter::toASBOPerson)
                .toArray(ASBOPenPerson[]::new));

        asboPenFinnPersonResponse.setPersoner(liste);
        return asboPenFinnPersonResponse;
    }

    @Override
    public ASBOPenTomRespons lagreSprak(ASBOPenPerson lagreSprakRequest) throws LagreSprakFaultPenPersonIkkeFunnetMsg, LagreSprakFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenTomRespons opprettSamboerforhold(ASBOPenPerson opprettSamboerforholdRequest) throws OpprettSamboerforholdFaultPenPersonIkkeFunnetMsg, OpprettSamboerforholdFaultPenSamboerDodMsg, OpprettSamboerforholdFaultPenSamboerIkkeFunnetMsg, OpprettSamboerforholdFaultPenSamboerValideringFeiletMsg, OpprettSamboerforholdFaultPenGeneriskMsg, OpprettSamboerforholdFaultPenSamboerIFamilieMsg, OpprettSamboerforholdFaultPenAlleredeRegistrertSamboerforholdMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "hentBrukerprofil", targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", className = "no.nav.inf.psak.person.HentBrukerprofil")
    @ResponseWrapper(localName = "hentBrukerprofilResponse", targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", className = "no.nav.inf.psak.person.HentBrukerprofilResponse")
    @WebResult(name = "hentBrukerprofilResponse", targetNamespace = "")
    public no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson hentBrukerprofil(
            @WebParam(name = "hentBrukerprofilRequest", targetNamespace = "")
                    no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson hentBrukerprofilRequest
    ) throws HentBrukerprofilFaultPenBrukerprofilIkkeFunnetMsg, HentBrukerprofilFaultPenGeneriskMsg {
        return PsakPersonAdapter.getPreviouslyConverted(hentBrukerprofilRequest.getFodselsnummer())
                .orElse(hentBrukerprofilRequest);
    }

    @Override
    public ASBOPenPerson hentEnhetId(ASBOPenPerson hentEnhetIdRequest) throws HentEnhetIdFaultPenPersonIkkeFunnetMsg, HentEnhetIdFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public String slettAdresse(ASBOPenSlettAdresseRequest slettAdresseRequest) throws SlettAdresseFaultPenPersonIkkeFunnetMsg, SlettAdresseFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenTomRespons lagreEpost(ASBOPenPerson lagreEpostRequest) throws LagreEpostFaultPenPersonIkkeFunnetMsg, LagreEpostFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "hentFamilierelasjoner", targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", className = "no.nav.inf.psak.person.HentFamilierelasjoner")
    @ResponseWrapper(localName = "hentFamilierelasjonerResponse", targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", className = "no.nav.inf.psak.person.HentFamilierelasjonerResponse")
    @WebResult(name = "hentFamilierelasjonerResponse", targetNamespace = "")
    public no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson hentFamilierelasjoner(
            @WebParam(name = "hentFamilierelasjonerRequest", targetNamespace = "")
                    no.nav.lib.pen.psakpselv.asbo.person.ASBOPenHentFamilierelasjonerRequest hentFamilierelasjonerRequest
    ) throws HentFamilierelasjonerFaultPenPersonIkkeFunnetMsg, HentFamilierelasjonerFaultPenGeneriskMsg{
        return PsakPersonAdapter.getPreviouslyConverted(hentFamilierelasjonerRequest.getFodselsnummer())
                    .map(p -> {p.setRelasjoner(new ASBOPenRelasjonListe()); return p;}) // konsumenten tåler ikke null
                .orElse(repo.getPersonIndeks().getAlleSøkere().parallelStream()
                    .filter(p -> p.getSøker().getIdent().equals(hentFamilierelasjonerRequest.getFodselsnummer()))
                    .map(PsakPersonAdapter::toASBOPerson)
                    .peek(p -> p.setRelasjoner(new ASBOPenRelasjonListe())) // konsumenten tåler ikke null
                    .findFirst()
                    .orElseThrow(HentFamilierelasjonerFaultPenPersonIkkeFunnetMsg::new));
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "hentSamboerforhold", targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", className = "no.nav.inf.psak.person.HentSamboerforhold")
    @ResponseWrapper(localName = "hentSamboerforholdResponse", targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", className = "no.nav.inf.psak.person.HentSamboerforholdResponse")
    @WebResult(name = "hentSamboerforholdResponse", targetNamespace = "")
    public no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson hentSamboerforhold(
            @WebParam(name = "hentSamboerforholdRequest", targetNamespace = "")
                    no.nav.lib.pen.psakpselv.asbo.person.ASBOPenHentSamboerforholdRequest hentSamboerforholdRequest
    ) throws HentSamboerforholdFaultPenPersonIkkeFunnetMsg, HentSamboerforholdFaultPenGeneriskMsg {
        return PsakPersonAdapter.getPreviouslyConverted(hentSamboerforholdRequest.getFodselsnummer())
                .orElse(repo.getPersonIndeks().getAlleSøkere().parallelStream()
                    .map(PsakPersonAdapter::toASBOPerson)
                    .findFirst()
                    .orElseThrow(HentSamboerforholdFaultPenGeneriskMsg::new));
    }

    @Override
    public String lagreDodsdato(ASBOPenPerson lagreDodsdatoRequest) throws LagreDodsdatoFaultPenPersonIkkeFunnetMsg, LagreDodsdatoFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenTomRespons lagreAdresse(ASBOPenLagreAdresseRequest lagreAdresseRequest) throws LagreAdresseFaultPenPersonIkkeFunnetMsg, LagreAdresseFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public String lagreStatsborgerskap(ASBOPenPerson lagreStatsborgerskapRequest) throws LagreStatsborgerskapFaultPenPersonIkkeFunnetMsg, LagreStatsborgerskapFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenTomRespons lagreTelefonnumre(ASBOPenLagreTelefonnumreRequest lagreTelefonnumreRequest) throws LagreTelefonnumreFaultPenPersonIkkeFunnetMsg, LagreTelefonnumreFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public String lagreFamilierelasjon(ASBOPenPerson lagreFamilierelasjonRequest) throws LagreFamilierelasjonFaultPenPersonIkkeFunnetMsg, LagreFamilierelasjonFaultPenStatusIkkeUtvandretMsg, LagreFamilierelasjonFaultPenAlleredeRegistrertFostermorMsg, LagreFamilierelasjonFaultPenAlleredeRegistrertMorMsg, LagreFamilierelasjonFaultPenGeneriskMsg, LagreFamilierelasjonFaultPenAllerdeRegistrertFosterfarMsg, LagreFamilierelasjonFaultPenRelasjonTilSegSelvMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public String opprettFamilierelasjon(ASBOPenPerson opprettFamilierelasjonRequest) throws OpprettFamilierelasjonFaultPenPersonIkkeFunnetMsg, OpprettFamilierelasjonFaultPenStatusIkkeUtvandretMsg, OpprettFamilierelasjonFaultPenAlleredeRegistrertFostermorMsg, OpprettFamilierelasjonFaultPenAlleredeRegistrertMorMsg, OpprettFamilierelasjonFaultPenGeneriskMsg, OpprettFamilierelasjonFaultPenAllerdeRegistrertFosterfarMsg, OpprettFamilierelasjonFaultPenRelasjonTilSegSelvMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public String lagreNavn(ASBOPenPerson lagreNavnRequest) throws LagreNavnFaultPenPersonIkkeFunnetMsg, LagreNavnFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public String lagreSivilstand(ASBOPenPerson lagreSivilstandRequest) throws LagreSivilstandFaultPenPersonIkkeFunnetMsg, LagreSivilstandFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public String lagreHistoriskSamboerforhold(ASBOPenLagreHistoriskSamboerforholdRequest lagreHistoriskSamboerforholdRequest) throws LagreHistoriskSamboerforholdFaultPenKorrigertPersonIkkeFunnetMsg, LagreHistoriskSamboerforholdFaultPenSamboerforholdIkkeFunnetMsg, LagreHistoriskSamboerforholdFaultPenGeneriskMsg, LagreHistoriskSamboerforholdFaultPenDatoerStemmerIkkeMedRegistrertSamboerforholdMsg, LagreHistoriskSamboerforholdFaultPenAlleredeRegistrertSamboerforholdMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenUtlandHistorikk hentPersonUtlandHistorikkListe(ASBOPenHentPersonUtlandsHistorikkListeRequest hentPersonUtlandHistorikkListeRequest) throws HentPersonUtlandHistorikkListeFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public Boolean erEgenansatt(ASBOPenPerson erEgenansattRequest) throws ErEgenansattFaultPenPersonIkkeFunnetMsg, ErEgenansattFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenPerson hentPersonUtland(ASBOPenPerson hentPersonUtlandRequest) throws HentPersonUtlandFaultPenPersonIkkeFunnetMsg, HentPersonUtlandFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenTomRespons lagreBrukerprofil(ASBOPenPerson lagreBrukerprofilRequest) throws LagreBrukerprofilFaultPenPersonIkkeFunnetMsg, LagreBrukerprofilFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenTomRespons lagreKontoinformasjon(ASBOPenPerson lagreKontoinformasjonRequest) throws LagreKontoinformasjonFaultPenPersonIkkeFunnetMsg, LagreKontoinformasjonFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenPerson hentHistorikk(ASBOPenHentHistorikkRequest hentHistorikkRequest) throws HentHistorikkFaultPenPersonIkkeFunnetMsg, HentHistorikkFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "hentKontoinformasjon", targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", className = "no.nav.inf.psak.person.HentKontoinformasjon")
    @ResponseWrapper(localName = "hentKontoinformasjonResponse", targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", className = "no.nav.inf.psak.person.HentKontoinformasjonResponse")
    @WebResult(name = "hentKontoinformasjonsResponse", targetNamespace = "")
    public no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson hentKontoinformasjon(
            @WebParam(name = "hentKontoinformasjonRequest", targetNamespace = "")
                    no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson hentKontoinformasjonRequest
    ) throws HentKontoinformasjonFaultPenPersonIkkeFunnetMsg, HentKontoinformasjonFaultPenGeneriskMsg {
        return PsakPersonAdapter.getPreviouslyConverted(hentKontoinformasjonRequest.getFodselsnummer())
                .orElse(hentKontoinformasjonRequest);
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "hentPerson", targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", className = "no.nav.inf.psak.person.HentPerson")
    @ResponseWrapper(localName = "hentPersonResponse", targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", className = "no.nav.inf.psak.person.HentPersonResponse")
    @WebResult(name = "hentPersonResponse", targetNamespace = "")
    public ASBOPenPerson hentPerson(
            @WebParam(name = "hentPersonRequest", targetNamespace = "") no.nav.lib.pen.psakpselv.asbo.person.ASBOPenHentPersonRequest hentPersonRequest)
            throws HentPersonFaultPenPersonIkkeFunnetMsg, HentPersonFaultPenGeneriskMsg {
        return PsakPersonAdapter.getPreviouslyConverted(hentPersonRequest.getPerson().getFodselsnummer())
                .orElse(repo.getPersonIndeks().getAlleSøkere().parallelStream()
                    .map(PsakPersonAdapter::toASBOPerson)
                    .findFirst()
                    .orElseThrow(HentPersonFaultPenPersonIkkeFunnetMsg::new));
    }

    @Override
    public ASBOPenTomRespons lagreSamboerforhold(ASBOPenPerson lagreSamboerforholdRequest) throws LagreSamboerforholdFaultPenPersonIkkeFunnetMsg, LagreSamboerforholdFaultPenSamboerDodMsg, LagreSamboerforholdFaultPenSamboerIkkeFunnetMsg, LagreSamboerforholdFaultPenAlleredeRegistrertSamboerMsg, LagreSamboerforholdFaultPenGeneriskMsg, LagreSamboerforholdFaultPenSamboerIFamilieMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenTomRespons slettSamboerforhold(ASBOPenPerson slettSamboerforholdRequest) throws SlettSamboerforholdFaultPenPersonIkkeFunnetMsg, SlettSamboerforholdFaultPenSamboerIkkeFunnetMsg, SlettSamboerforholdFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }
}
