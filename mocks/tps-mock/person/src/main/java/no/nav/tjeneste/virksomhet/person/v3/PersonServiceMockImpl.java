package no.nav.tjeneste.virksomhet.person.v3;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.mock.felles.ConversionUtils;
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
import no.nav.tjeneste.virksomhet.person.v3.data.PersonDbLeser;
import no.nav.tjeneste.virksomhet.person.v3.data.RelasjonDbLeser;
import no.nav.tjeneste.virksomhet.person.v3.feil.PersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Aktoer;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.AktoerId;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Bostedsadresse;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.BostedsadressePeriode;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Bruker;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Landkoder;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Periode;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.PersonIdent;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.PersonstatusPeriode;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Personstatuser;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Postadresse;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.PostboksadresseNorsk;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Postnummer;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Statsborgerskap;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.StatsborgerskapPeriode;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.StrukturertAdresse;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.UstrukturertAdresse;
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
import no.nav.tjeneste.virksomhet.person.v3.metadata.Endringstyper;
import no.nav.tjeneste.virksomhet.person.v3.modell.RelasjonBygger;
import no.nav.tjeneste.virksomhet.person.v3.modell.TpsRelasjon;

@Addressing
@WebService(name = "Person_v3", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3")
@HandlerChain(file="Handler-chain.xml")
public class PersonServiceMockImpl implements PersonV3 {

    private static final Logger LOG = LoggerFactory.getLogger(PersonServiceMockImpl.class);
    private static final EntityManager entityManager = Persistence.createEntityManagerFactory("tps").createEntityManager();

    public static XMLGregorianCalendar TIDENES_MORGEN = ConversionUtils.convertToXMLGregorianCalendar(LocalDate.of(1900, 1, 1));

    private static Set<Long> aktørerCase1 = new HashSet<>();
    private static Set<Long> aktørerCase2 = new HashSet<>();
    private static Set<Long> aktørerCase5 = new HashSet<>();
    private static Set<Long> aktørerCase6 = new HashSet<>();
    private static Set<Long> aktørerCase7 = new HashSet<>();

    static {
        aktørerCase1.add(9000000030670L);
        aktørerCase1.add(9000000030671L);
        aktørerCase1.add(9000000030672L);
        aktørerCase1.add(9000000030673L);

        aktørerCase2.add(9000000030674L);
        aktørerCase2.add(9000000030677L);
        aktørerCase2.add(9000000030678L);
        aktørerCase2.add(9000000030675L);
        aktørerCase2.add(9000000030676L);

        aktørerCase5.add(9000000030679L);
        aktørerCase5.add(9000000030680L);
        aktørerCase5.add(9000000030681L);
        aktørerCase5.add(9000000030682L);
        aktørerCase5.add(9000000030683L);

        aktørerCase6.add(9000000030684L);
        aktørerCase6.add(9000000030685L);
        aktørerCase6.add(9000000030686L);
        aktørerCase6.add(9000000030687L);
        aktørerCase6.add(9000000030688L);

        aktørerCase7.add(9000000030689L);
        aktørerCase7.add(9000000030690L);
        aktørerCase7.add(9000000030691L);
        aktørerCase7.add(9000000030692L);
        aktørerCase7.add(9000000030693L);
    }

    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentPersonRequest"
    )
    @WebResult(
            name = "response",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "hentPerson",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
            className = "no.nav.tjeneste.virksomhet.person.v3.HentPerson"
    )
    @ResponseWrapper(
            localName = "hentPersonResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
            className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonResponse"
    )
    @Override
    public HentPersonResponse hentPerson(@WebParam(name = "request",targetNamespace = "") HentPersonRequest hentPersonRequest) throws HentPersonPersonIkkeFunnet, HentPersonSikkerhetsbegrensning {

        Bruker bruker;
        String fnr;
        if (hentPersonRequest.getAktoer() instanceof PersonIdent) {
            PersonIdent ident = (PersonIdent) hentPersonRequest.getAktoer();
            bruker = new PersonDbLeser(entityManager).finnPerson(ident.getIdent().getIdent());
            if (bruker == null) {
                throw new HentPersonPersonIkkeFunnet("Fant ingen bruker for ident: " + ident.getIdent().getIdent(), new PersonIkkeFunnet());
            }
            fnr = ident.getIdent().getIdent();
        } else {
            AktoerId aktoerId = (AktoerId) hentPersonRequest.getAktoer();
            bruker = new PersonDbLeser(entityManager).finnPersonMedAktørId(aktoerId.getAktoerId());

            if (bruker == null) {
                throw new HentPersonPersonIkkeFunnet("Fant ingen bruker for aktørId: " + aktoerId.getAktoerId(), new PersonIkkeFunnet());
            }

            fnr = new PersonDbLeser(entityManager).finnIdent(aktoerId.getAktoerId());
        }

        List<TpsRelasjon> relasjoner = new RelasjonDbLeser(entityManager).finnRelasjon(fnr);
        for (TpsRelasjon relasjon : relasjoner) {
            new RelasjonBygger(relasjon).byggFor(bruker);
        }

        HentPersonResponse response = new HentPersonResponse();
        response.setPerson(bruker);
        return response;
    }


    public static final String ENDRET_AV = "Testhub-gjengen";


    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentGeografiskTilknytningRequest"
    )
    @WebResult(
            name = "response",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "hentGeografiskTilknytning",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
            className = "no.nav.tjeneste.virksomhet.person.v3.HentGeografiskTilknytning"
    )
    @ResponseWrapper(
            localName = "hentGeografiskTilknytningResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
            className = "no.nav.tjeneste.virksomhet.person.v3.HentGeografiskTilknytningResponse"
    )
    @Override
    public HentGeografiskTilknytningResponse hentGeografiskTilknytning(@WebParam(name = "request",targetNamespace = "") HentGeografiskTilknytningRequest hentGeografiskTilknytningRequest) throws HentGeografiskTilknytningPersonIkkeFunnet, HentGeografiskTilknytningSikkerhetsbegrensing {
        PersonIdent personIdent = (PersonIdent)hentGeografiskTilknytningRequest.getAktoer();
        String ident = personIdent.getIdent().getIdent();

        Bruker bruker = new PersonDbLeser(entityManager).finnPerson(ident);
        if (bruker == null) {
            throw new HentGeografiskTilknytningPersonIkkeFunnet("Fant ingen bruker for ident: " + ident, new PersonIkkeFunnet());
        }

        HentGeografiskTilknytningResponse response = new HentGeografiskTilknytningResponse();
        response.setGeografiskTilknytning(bruker.getGeografiskTilknytning());
        response.setDiskresjonskode(bruker.getDiskresjonskode());
        return response;
    }

    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentVergeRequest"
    )
    @WebResult(
            name = "response",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "hentVerge",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
            className = "no.nav.tjeneste.virksomhet.person.v3.HentVerge"
    )
    @ResponseWrapper(
            localName = "hentVergeResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
            className = "no.nav.tjeneste.virksomhet.person.v3.HentVergeResponse"
    )
    @Override
    public HentVergeResponse hentVerge(@WebParam(name = "request",targetNamespace = "") HentVergeRequest var1) throws HentVergePersonIkkeFunnet, HentVergeSikkerhetsbegrensning {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentEkteskapshistorikkRequest"
    )
    @WebResult(
            name = "response",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "hentEkteskapshistorikk",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
            className = "no.nav.tjeneste.virksomhet.person.v3.HentEkteskapshistorikk"
    )
    @ResponseWrapper(
            localName = "hentEkteskapshistorikkResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
            className = "no.nav.tjeneste.virksomhet.person.v3.HentEkteskapshistorikkResponse"
    )
    public HentEkteskapshistorikkResponse hentEkteskapshistorikk(@WebParam(name = "request",targetNamespace = "") HentEkteskapshistorikkRequest hentEkteskapshistorikkRequest) throws HentEkteskapshistorikkPersonIkkeFunnet, HentEkteskapshistorikkSikkerhetsbegrensning {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentPersonerMedSammeAdresseRequest"
    )
    @WebResult(
            name = "response",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "hentPersonerMedSammeAdresse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
            className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonerMedSammeAdresse"
    )
    @ResponseWrapper(
            localName = "hentPersonerMedSammeAdresseResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
            className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonerMedSammeAdresseResponse"
    )
    @Override
    public HentPersonerMedSammeAdresseResponse hentPersonerMedSammeAdresse(@WebParam(name = "request",targetNamespace = "") HentPersonerMedSammeAdresseRequest var1) throws HentPersonerMedSammeAdresseIkkeFunnet, HentPersonerMedSammeAdresseSikkerhetsbegrensning{
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentPersonhistorikkRequest"
    )
    @WebResult(
            name = "response",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "hentPersonhistorikk",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
            className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonhistorikk"
    )
    @ResponseWrapper(
            localName = "hentPersonhistorikkResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3",
            className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonhistorikkResponse"
    )
    @Override
    public HentPersonhistorikkResponse hentPersonhistorikk(@WebParam(name = "request",targetNamespace = "") HentPersonhistorikkRequest hentPersonhistorikkRequest) throws HentPersonhistorikkPersonIkkeFunnet, HentPersonhistorikkSikkerhetsbegrensning {

        sjekkAtPersonErImportertITesthub(hentPersonhistorikkRequest);

        AktoerId aktoerId = (AktoerId) hentPersonhistorikkRequest.getAktoer();

        HentPersonhistorikkResponse response = new HentPersonhistorikkResponse();
        response.withAktoer(hentPersonhistorikkRequest.getAktoer());

        // case {1,2,5,6,7}, ref https://jira.adeo.no/browse/REG-296
        if (aktørerCase1.contains(Long.valueOf(aktoerId.getAktoerId()))) {
            return new PersonhistorikkMockCase1().mockedPerson(aktoerId);
        }

        if (aktørerCase2.contains(Long.valueOf(aktoerId.getAktoerId()))) {
            return new PersonhistorikkMockCase2().mockedPerson(aktoerId);
        }

        if (aktørerCase5.contains(Long.valueOf(aktoerId.getAktoerId()))) {
            return new PersonhistorikkMockCase5().mockedPerson(aktoerId);
        }

        if (aktørerCase6.contains(Long.valueOf(aktoerId.getAktoerId()))) {
            return new PersonhistorikkMockCase6().mockedPerson(aktoerId);
        }

        if (aktørerCase7.contains(Long.valueOf(aktoerId.getAktoerId()))) {
            return new PersonhistorikkMockCase7().mockedPerson(aktoerId);
        }

        response.withPersonstatusListe(hentPersonstatusPerioder(hentPersonhistorikkRequest.getAktoer()));
        response.withBostedsadressePeriodeListe(hentBostedadressePerioder(hentPersonhistorikkRequest.getAktoer()));
        response.withStatsborgerskapListe(hentStatsborgerskapPerioder(hentPersonhistorikkRequest.getAktoer()));
        return response;
    }

    private void sjekkAtPersonErImportertITesthub(@WebParam(name = "request", targetNamespace = "") HentPersonhistorikkRequest hentPersonhistorikkRequest) throws HentPersonhistorikkPersonIkkeFunnet {
        HentPersonRequest request = new HentPersonRequest();
        request.setAktoer(hentPersonhistorikkRequest.getAktoer());
        try {
            hentPerson(request);
        } catch (HentPersonPersonIkkeFunnet hentPersonPersonIkkeFunnet) {
            throw new HentPersonhistorikkPersonIkkeFunnet(hentPersonPersonIkkeFunnet.getMessage(), new PersonIkkeFunnet());
        } catch (HentPersonSikkerhetsbegrensning hentPersonSikkerhetsbegrensning) {
            hentPersonSikkerhetsbegrensning.printStackTrace();
        }
    }


    public static XMLGregorianCalendar lagDato(int year, int month, int day) {
        return ConversionUtils.convertToXMLGregorianCalendar(LocalDate.of(year, month, day));
    }

    private List<StatsborgerskapPeriode> hentStatsborgerskapPerioder(Aktoer aktoer) {
        List<StatsborgerskapPeriode> resultat = new ArrayList<>();

        StatsborgerskapPeriode periode1 = new StatsborgerskapPeriode();
        periode1.withEndretAv(ENDRET_AV);
        periode1.withEndringstidspunkt(ConversionUtils.convertToXMLGregorianCalendar(LocalDate.now()));
        periode1.withEndringstype(Endringstyper.NY);
        periode1.withPeriode(lagPeriode(LocalDate.now().minusMonths(2), LocalDate.now().minusMonths(1)));
        periode1.withEndringstype(Endringstyper.NY);
        periode1.withStatsborgerskap(lagStatsborgerskap());

        StatsborgerskapPeriode periode2 = new StatsborgerskapPeriode();
        periode2.withEndretAv(ENDRET_AV);
        periode2.withEndringstidspunkt(ConversionUtils.convertToXMLGregorianCalendar(LocalDate.now()));
        periode2.withEndringstype(Endringstyper.NY);
        periode2.withPeriode(lagPeriode(LocalDate.now().minusMonths(1), LocalDate.now()));
        periode2.withEndringstype(Endringstyper.NY);
        periode2.withStatsborgerskap(lagStatsborgerskap());

        resultat.add(periode1);
        resultat.add(periode2);
        return resultat;
    }

    private Statsborgerskap lagStatsborgerskap() {
        Statsborgerskap statsborgerskap = new Statsborgerskap();
        statsborgerskap.withEndretAv(ENDRET_AV);
        statsborgerskap.withEndringstidspunkt(ConversionUtils.convertToXMLGregorianCalendar(LocalDate.now()));
        statsborgerskap.withEndringstype(Endringstyper.NY);

        Landkoder land = new Landkoder();
        land.withKodeRef("Landkoder");
        land.setValue("NOR");
        land.setKodeRef("NOR");
        statsborgerskap.withLand(land);
        return statsborgerskap;
    }

    private List<BostedsadressePeriode> hentBostedadressePerioder(Aktoer aktoer) {
        List<BostedsadressePeriode> resultat = new ArrayList<>();

        BostedsadressePeriode adr1 = new BostedsadressePeriode();
        adr1.withEndretAv(ENDRET_AV);
        adr1.withEndringstidspunkt(ConversionUtils.convertToXMLGregorianCalendar(LocalDate.now()));
        adr1.withEndringstype(Endringstyper.NY);
        adr1.withBostedsadresse(lagBostedadresse());
        adr1.withPeriode(lagPeriode(LocalDate.now().minusMonths(2), LocalDate.now().minusMonths(1)));

        BostedsadressePeriode adr2 = new BostedsadressePeriode();
        adr2.withEndretAv(ENDRET_AV);
        adr2.withEndringstidspunkt(ConversionUtils.convertToXMLGregorianCalendar(LocalDate.now()));
        adr2.withEndringstype(Endringstyper.NY);
        adr2.withBostedsadresse(lagBostedadresse());
        adr2.withPeriode(lagPeriode(LocalDate.now().minusMonths(2), LocalDate.now().minusMonths(1)));

        resultat.add(adr1);
        resultat.add(adr2);
        return resultat;
    }

    public static Bostedsadresse lagBostedadresse() {
        Bostedsadresse bostedsadresse = new Bostedsadresse();
        bostedsadresse.withEndretAv(ENDRET_AV);
        bostedsadresse.withEndringstidspunkt(ConversionUtils.convertToXMLGregorianCalendar(LocalDate.now()));
        bostedsadresse.withEndringstype(Endringstyper.ENDRET);
        bostedsadresse.withStrukturertAdresse(lagStrukturertAdresse());

        return bostedsadresse;
    }

    public static Bostedsadresse lagBostedadresse(XMLGregorianCalendar endringstidspunkt, Endringstyper endringstyper) {
        Bostedsadresse bostedsadresse = new Bostedsadresse();
        bostedsadresse.withEndretAv(ENDRET_AV);
        bostedsadresse.withEndringstidspunkt(endringstidspunkt);
        bostedsadresse.withEndringstype(endringstyper);
        bostedsadresse.withStrukturertAdresse(lagStrukturertAdresse());
        return bostedsadresse;
    }

    public static Postadresse lagUtenlandsAdresse(XMLGregorianCalendar endringstidspunkt, Endringstyper endringstyper, String landkode) {

        UstrukturertAdresse adresse = new UstrukturertAdresse();
        Landkoder landkoder = new Landkoder();
        landkoder.setKodeverksRef("Landkoder");
        landkoder.setKodeRef(landkode);
        landkoder.setValue(landkode);

        adresse.withLandkode(landkoder);
        adresse.withAdresselinje1("anglegg123" + new Random().nextInt(9));
        adresse.withPostnr("095" + new Random().nextInt(9));
        adresse.withPoststed("sdffsdfs");

        Postadresse postadresse = new Postadresse();
        postadresse.withEndretAv(ENDRET_AV);
        postadresse.withEndringstidspunkt(endringstidspunkt);
        postadresse.withEndringstype(endringstyper);

        postadresse.withUstrukturertAdresse(adresse);

        return postadresse;
    }

    public static StrukturertAdresse lagStrukturertAdresse() {
        PostboksadresseNorsk postboksadresseNorsk = new PostboksadresseNorsk();

        Landkoder landkoder = new Landkoder();
        landkoder.setKodeverksRef("Landkoder");
        landkoder.setKodeRef("NOR");
        landkoder.setValue("NOR");

        postboksadresseNorsk.withLandkode(landkoder);
        postboksadresseNorsk.withPostboksanlegg("anglegg123" + new Random().nextInt(9));
        postboksadresseNorsk.withPostboksnummer("095" + new Random().nextInt(9));

        Postnummer postnummer = new Postnummer();
        postnummer.setKodeverksRef("Postnummer");
        postnummer.setKodeRef("Oslo");
        postnummer.setValue("Oslo");
        postboksadresseNorsk.withPoststed(postnummer);
        postboksadresseNorsk.withPostboksnummer("0103");

        return postboksadresseNorsk;
    }

    private List<PersonstatusPeriode> hentPersonstatusPerioder(Aktoer aktoer) {
        List<PersonstatusPeriode> resultat = new ArrayList<>();

        PersonstatusPeriode personstatusPeriode = new PersonstatusPeriode();
        personstatusPeriode.withEndretAv(ENDRET_AV);
        personstatusPeriode.withEndringstidspunkt(ConversionUtils.convertToXMLGregorianCalendar(LocalDate.now()));
        personstatusPeriode.withEndringstype(Endringstyper.NY);
        personstatusPeriode.withPersonstatus(lagPersonstatuser("BOSA"));
        personstatusPeriode.withPeriode(lagPeriode(LocalDate.now().minusMonths(2), LocalDate.now().minusMonths(1)));

        PersonstatusPeriode personstatusPeriode1 = new PersonstatusPeriode();
        personstatusPeriode1.withEndretAv(ENDRET_AV);
        personstatusPeriode1.withEndringstidspunkt(ConversionUtils.convertToXMLGregorianCalendar(LocalDate.now()));
        personstatusPeriode1.withEndringstype(Endringstyper.NY);
        personstatusPeriode1.withPersonstatus(lagPersonstatuser("ADNR"));
        personstatusPeriode1.withPeriode(lagPeriode(LocalDate.now().minusMonths(1), LocalDate.now()));

        resultat.add(personstatusPeriode);
        resultat.add(personstatusPeriode1);

        return resultat;
    }

    public static Personstatuser lagPersonstatuser(String personstatus) {
        Personstatuser personstatuser = new Personstatuser();
        personstatuser.setKodeverksRef("Personstatuser");
        personstatuser.setKodeRef(personstatus);
        personstatuser.setValue(personstatus);
        return personstatuser;
    }

    public static Periode lagPeriode(LocalDate fom, LocalDate tom) {
        Periode periode = new Periode();
        periode.withFom(ConversionUtils.convertToXMLGregorianCalendar(fom));
        periode.withFom(ConversionUtils.convertToXMLGregorianCalendar(tom));
        return periode;
    }

    public static Periode lagPeriode(XMLGregorianCalendar fom, XMLGregorianCalendar tom) {
        Periode periode = new Periode();
        periode.withFom(fom);
        periode.withTom(tom);
        return periode;
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
    public HentSikkerhetstiltakResponse hentSikkerhetstiltak(@WebParam(name = "request", targetNamespace = "") HentSikkerhetstiltakRequest var1) throws HentSikkerhetstiltakPersonIkkeFunnet {
        throw new UnsupportedOperationException("Ikke implementert");
    }
}
