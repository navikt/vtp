package no.nav.tjeneste.virksomhet.person.v3;

import java.time.LocalDate;
import java.util.List;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.vtp.felles.ConversionUtils;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.AdresseType;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.FamilierelasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.FamilierelasjonModell.Rolle;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentEkteskapshistorikkPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentEkteskapshistorikkSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentGeografiskTilknytningPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentGeografiskTilknytningSikkerhetsbegrensing;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentPersonPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentPersonSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentPersonerMedSammeAdresseIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentPersonerMedSammeAdresseSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentPersonhistorikkPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentPersonhistorikkSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentSikkerhetstiltakPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentVergePersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentVergeSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.person.v3.binding.PersonV3;
import no.nav.tjeneste.virksomhet.person.v3.feil.PersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Aktoer;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.BostedsadressePeriode;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Bruker;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Diskresjonskoder;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Familierelasjon;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.PersonstatusPeriode;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.StatsborgerskapPeriode;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentEkteskapshistorikkRequest;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentEkteskapshistorikkResponse;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentGeografiskTilknytningRequest;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentGeografiskTilknytningResponse;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonRequest;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonResponse;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonerMedSammeAdresseRequest;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonerMedSammeAdresseResponse;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonhistorikkRequest;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonhistorikkResponse;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonnavnBolkRequest;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonnavnBolkResponse;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentSikkerhetstiltakRequest;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentSikkerhetstiltakResponse;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentVergeRequest;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentVergeResponse;

@Addressing
@WebService(name = "Person_v3", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3")
@HandlerChain(file = "Handler-chain.xml")
public class PersonServiceMockImpl implements PersonV3 {

    private static final Logger LOG = LoggerFactory.getLogger(PersonServiceMockImpl.class);

    private TestscenarioBuilderRepository repo;

    public PersonServiceMockImpl() {
    }

    public PersonServiceMockImpl(TestscenarioBuilderRepository repo) {
        this.repo = repo;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentPersonRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentPerson", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentPerson")
    @ResponseWrapper(localName = "hentPersonResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonResponse")
    @Override
    public HentPersonResponse hentPerson(@WebParam(name = "request", targetNamespace = "") HentPersonRequest hentPersonRequest)
            throws HentPersonPersonIkkeFunnet, HentPersonSikkerhetsbegrensning {

        LOG.info("hentPerson. Aktoer: {}", hentPersonRequest.getAktoer().toString());
        Aktoer aktoer = hentPersonRequest.getAktoer();
        PersonModell bruker = finnPerson(aktoer);
        Bruker person = new PersonAdapter().fra(bruker);

        Personopplysninger pers = repo.getPersonIndeks().finnPersonopplysningerByIdent(bruker.getIdent());

        boolean erBarnet = false;
        for (FamilierelasjonModell relasjon : pers.getFamilierelasjoner()) {
            if(relasjon.getRolle().equals(Rolle.BARN) && relasjon.getTil().getAktørIdent().equals(bruker.getAktørIdent())) {
                erBarnet = true;
            }
        }

        List<Familierelasjon> familierelasjoner;
        if(pers.getAnnenPart() != null && pers.getAnnenPart().getAktørIdent().equals(bruker.getAktørIdent())) { //TODO HACK for annenpart (annenpart burde ha en egen personopplysning fil eller liknende)
            familierelasjoner = new FamilierelasjonAdapter().tilFamilerelasjon(pers.getFamilierelasjonerForAnnenPart());
            familierelasjoner.forEach(fr -> person.getHarFraRolleI().add(fr));
        }
        else if(erBarnet) { //TODO HACK Familierelasjon for barnet
            familierelasjoner = new FamilierelasjonAdapter().tilFamilerelasjon(pers.getFamilierelasjonerForBarnet());
            familierelasjoner.forEach(fr -> person.getHarFraRolleI().add(fr));
        }
        else {
            familierelasjoner = new FamilierelasjonAdapter().tilFamilerelasjon(pers.getFamilierelasjoner());
            familierelasjoner.forEach(fr -> person.getHarFraRolleI().add(fr));
        }

        System.out.println("Relasjoner for person: " + hentPersonRequest.getAktoer().toString());
        for (Familierelasjon familierelasjon : familierelasjoner) {
            System.out.println("    " + familierelasjon.getTilPerson().getAktoer().toString());
        }

        HentPersonResponse response = new HentPersonResponse();
        response.setPerson(person);
        return response;
    }

    public PersonModell finnPerson(Aktoer aktoer) throws HentPersonPersonIkkeFunnet {
        return new FinnPerson(repo).finnPerson(aktoer);
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentGeografiskTilknytningRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentGeografiskTilknytning", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentGeografiskTilknytning")
    @ResponseWrapper(localName = "hentGeografiskTilknytningResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentGeografiskTilknytningResponse")
    @Override
    public HentGeografiskTilknytningResponse hentGeografiskTilknytning(@WebParam(name = "request", targetNamespace = "") HentGeografiskTilknytningRequest hentGeografiskTilknytningRequest)
            throws HentGeografiskTilknytningPersonIkkeFunnet, HentGeografiskTilknytningSikkerhetsbegrensing {
        LOG.info("hentGeografiskTilknytning. {}", hentGeografiskTilknytningRequest.toString());
        PersonModell bruker;
        try {
            bruker = finnPerson(hentGeografiskTilknytningRequest.getAktoer());
        } catch (HentPersonPersonIkkeFunnet e) {
            throw new HentGeografiskTilknytningPersonIkkeFunnet(e.getMessage(), new PersonIkkeFunnet());
        }

        HentGeografiskTilknytningResponse response = new HentGeografiskTilknytningResponse();
        PersonAdapter personAdapter = new PersonAdapter();
        Diskresjonskoder diskresjonskoder = personAdapter.tilDiskresjonskode(bruker);
        response.setDiskresjonskode(diskresjonskoder);
        response.setGeografiskTilknytning(personAdapter.tilGeografiskTilknytning(bruker));
        return response;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentVergeRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentVerge", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentVerge")
    @ResponseWrapper(localName = "hentVergeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentVergeResponse")
    @Override
    public HentVergeResponse hentVerge(@WebParam(name = "request", targetNamespace = "") HentVergeRequest var1)
            throws HentVergePersonIkkeFunnet, HentVergeSikkerhetsbegrensning {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentEkteskapshistorikkRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentEkteskapshistorikk", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentEkteskapshistorikk")
    @ResponseWrapper(localName = "hentEkteskapshistorikkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentEkteskapshistorikkResponse")
    public HentEkteskapshistorikkResponse hentEkteskapshistorikk(@WebParam(name = "request", targetNamespace = "") HentEkteskapshistorikkRequest hentEkteskapshistorikkRequest)
            throws HentEkteskapshistorikkPersonIkkeFunnet, HentEkteskapshistorikkSikkerhetsbegrensning {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentPersonerMedSammeAdresseRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentPersonerMedSammeAdresse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonerMedSammeAdresse")
    @ResponseWrapper(localName = "hentPersonerMedSammeAdresseResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonerMedSammeAdresseResponse")
    @Override
    public HentPersonerMedSammeAdresseResponse hentPersonerMedSammeAdresse(@WebParam(name = "request", targetNamespace = "") HentPersonerMedSammeAdresseRequest var1)
            throws HentPersonerMedSammeAdresseIkkeFunnet, HentPersonerMedSammeAdresseSikkerhetsbegrensning {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentPersonhistorikkRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentPersonhistorikk", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonhistorikk")
    @ResponseWrapper(localName = "hentPersonhistorikkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonhistorikkResponse")
    @Override
    public HentPersonhistorikkResponse hentPersonhistorikk(@WebParam(name = "request", targetNamespace = "") HentPersonhistorikkRequest hentPersonhistorikkRequest)
            throws HentPersonhistorikkPersonIkkeFunnet, HentPersonhistorikkSikkerhetsbegrensning {

        LOG.info("hentPersonhistorikk. AktoerId: {}", hentPersonhistorikkRequest.getAktoer().toString());
        BrukerModell bruker;
        try {
            bruker = finnPerson(hentPersonhistorikkRequest.getAktoer());
        } catch (HentPersonPersonIkkeFunnet e) {
            throw new HentPersonhistorikkPersonIkkeFunnet(e.getMessage(), new PersonIkkeFunnet());
        }

        HentPersonhistorikkResponse response = new HentPersonhistorikkResponse();
        response.withAktoer(hentPersonhistorikkRequest.getAktoer());
        if (bruker instanceof PersonModell) {
            PersonModell voksen = (PersonModell) bruker;
            response.withPersonstatusListe(hentPersonstatusPerioder(voksen));
            response.withBostedsadressePeriodeListe(hentBostedadressePerioder(voksen));
            response.withStatsborgerskapListe(hentStatsborgerskapPerioder(voksen));
        }
        return response;
    }

    private List<BostedsadressePeriode> hentBostedadressePerioder(PersonModell bruker) {
        return new BostedsadresseAdapter().fra(bruker.getAdresser(AdresseType.BOSTEDSADRESSE));
    }

    public static XMLGregorianCalendar lagDato(int year, int month, int day) {
        return ConversionUtils.convertToXMLGregorianCalendar(LocalDate.of(year, month, day));
    }

    private List<StatsborgerskapPeriode> hentStatsborgerskapPerioder(PersonModell bruker) {
        return new StatsborgerskapAdapter().fra(bruker.getAlleStatsborgerskap());
    }

    private List<PersonstatusPeriode> hentPersonstatusPerioder(PersonModell bruker) {
        return new PersonstatusAdapter().fra(bruker.getAllePersonstatus());
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.PingResponse")
    @Override
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentPersonnavnBolkRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentPersonnavnBolk", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonnavnBolk")
    @ResponseWrapper(localName = "hentPersonnavnBolkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonnavnBolkResponse")
    @Override
    public HentPersonnavnBolkResponse hentPersonnavnBolk(@WebParam(name = "request", targetNamespace = "") HentPersonnavnBolkRequest var1) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentSikkerhetstiltakRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentSikkerhetstiltak", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentSikkerhetstiltak")
    @ResponseWrapper(localName = "hentSikkerhetstiltakResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentSikkerhetstiltakResponse")
    @Override
    public HentSikkerhetstiltakResponse hentSikkerhetstiltak(@WebParam(name = "request", targetNamespace = "") HentSikkerhetstiltakRequest var1)
            throws HentSikkerhetstiltakPersonIkkeFunnet {
        throw new UnsupportedOperationException("Ikke implementert");
    }
}


