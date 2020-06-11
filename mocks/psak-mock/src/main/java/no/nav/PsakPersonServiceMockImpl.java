package no.nav;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.SøkerModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.inf.psak.person.*;
import no.nav.lib.pen.psakpselv.asbo.ASBOPenTomRespons;
import no.nav.lib.pen.psakpselv.asbo.person.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.*;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Addressing
@WebService(targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", name = "PSAKPerson")
@HandlerChain(file = "Handler-chain.xml")
public class PsakPersonServiceMockImpl implements PSAKPerson {
    private static final Logger LOG = LoggerFactory.getLogger(PsakPersonServiceMockImpl.class);

    private TestscenarioBuilderRepository repo;

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
        LOG.warn("Kall på finnPerson i PsakPerson");
        ASBOPenFinnPersonResponse asboPenFinnPersonResponse = new ASBOPenFinnPersonResponse();
        LocalDate localDate = finnPersonRequest.getFodselsdatosok().getFodselsdato().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();

        ASBOPenPersonListe liste = new ASBOPenPersonListe();
        liste.setPersoner(repo.getPersonIndeks().getAlleSøkere().parallelStream()
                .map(Personopplysninger::getSøker)
                //.filter(p -> p.getFødselsdato().isEqual(localDate))
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
    public ASBOPenPerson hentBrukerprofil(ASBOPenPerson hentBrukerprofilRequest) throws HentBrukerprofilFaultPenBrukerprofilIkkeFunnetMsg, HentBrukerprofilFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
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
    public ASBOPenPerson hentFamilierelasjoner(ASBOPenHentFamilierelasjonerRequest hentFamilierelasjonerRequest) throws HentFamilierelasjonerFaultPenPersonIkkeFunnetMsg, HentFamilierelasjonerFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenPerson hentSamboerforhold(ASBOPenHentSamboerforholdRequest hentSamboerforholdRequest) throws HentSamboerforholdFaultPenPersonIkkeFunnetMsg, HentSamboerforholdFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
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
    public ASBOPenPerson hentKontoinformasjon(ASBOPenPerson hentKontoinformasjonRequest) throws HentKontoinformasjonFaultPenPersonIkkeFunnetMsg, HentKontoinformasjonFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "hentPerson", targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", className = "no.nav.inf.psak.person.HentPerson")
    @ResponseWrapper(localName = "hentPersonResponse", targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", className = "no.nav.inf.psak.person.HentPersonResponse")
    @WebResult(name = "hentPersonResponse", targetNamespace = "")
    public ASBOPenPerson hentPerson(
            @WebParam(name = "hentPersonRequest", targetNamespace = "") no.nav.lib.pen.psakpselv.asbo.person.ASBOPenHentPersonRequest hentPersonRequest)
            throws HentPersonFaultPenPersonIkkeFunnetMsg, HentPersonFaultPenGeneriskMsg {
        return repo.getPersonIndeks().getAlleSøkere().parallelStream()
                .map(Personopplysninger::getSøker)
                .map(PsakPersonAdapter::toASBOPerson)
                .findFirst()
                .orElseThrow(HentPersonFaultPenPersonIkkeFunnetMsg::new);
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
