package no.nav.tjeneste.virksomhet.person.v3;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import no.nav.tjeneste.virksomhet.person.v3.informasjon.PostadressePeriode;
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

    private XMLGregorianCalendar TIDENES_MORGEN = ConversionUtils.convertToXMLGregorianCalendar(LocalDate.of(1900, 1, 1));

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

        PersonIdent personIdent = (PersonIdent)hentPersonRequest.getAktoer();
        String ident = personIdent.getIdent().getIdent();

        Bruker bruker = new PersonDbLeser(entityManager).finnPerson(ident);
        if (bruker == null) {
            throw new HentPersonPersonIkkeFunnet("Fant ingen bruker for ident: " + ident, new PersonIkkeFunnet());
        }

        List<TpsRelasjon> relasjoner = new RelasjonDbLeser(entityManager).finnRelasjon(ident);
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

        AktoerId aktoerId = (AktoerId) hentPersonhistorikkRequest.getAktoer();

        Bruker bruker = new PersonDbLeser(entityManager).finnPersonMedAktørId(aktoerId.getAktoerId());
        if (bruker == null) {
            //throw new HentPersonhistorikkPersonIkkeFunnet("Fant ingen bruker for aktørId: " + aktoerId.getAktoerId(), new PersonIkkeFunnet());
        }

        HentPersonhistorikkResponse response = new HentPersonhistorikkResponse();
        response.withAktoer(hentPersonhistorikkRequest.getAktoer());

        // person 1
        if (aktoerId.getAktoerId().equals("9000000030670")) {
            return person1(response);
        }

        response.withPersonstatusListe(hentPersonstatusPerioder(hentPersonhistorikkRequest.getAktoer()));
        response.withBostedsadressePeriodeListe(hentBostedadressePerioder(hentPersonhistorikkRequest.getAktoer()));
        response.withStatsborgerskapListe(hentStatsborgerskapPerioder(hentPersonhistorikkRequest.getAktoer()));
        return response;
    }

    private HentPersonhistorikkResponse person1(HentPersonhistorikkResponse response) {
        response.withPersonstatusListe(personstatusPerson1());
        response.withStatsborgerskapListe(personStatsborgerskapPerson1());
        response.withBostedsadressePeriodeListe(personNorskAdresserPerson1());
        response.withPostadressePeriodeListe(personUtenlandsadresserPerson1());

        return response;
    }

    private List<BostedsadressePeriode> personNorskAdresserPerson1() {
        List<BostedsadressePeriode> resultat = new ArrayList<>();

        BostedsadressePeriode norskAdresse = new BostedsadressePeriode();
        norskAdresse.withEndretAv(ENDRET_AV);
        norskAdresse.withEndringstidspunkt(TIDENES_MORGEN);
        norskAdresse.withEndringstype(Endringstyper.NY);
        norskAdresse.withPeriode(lagPeriode(TIDENES_MORGEN, lagDato(2018, 4, 16)));
        norskAdresse.withBostedsadresse(lagBostedadresse(TIDENES_MORGEN, Endringstyper.NY));

        resultat.add(norskAdresse);

        return resultat;
    }

    private List<PostadressePeriode> personUtenlandsadresserPerson1() {
        List<PostadressePeriode> resultat = new ArrayList<>();

        PostadressePeriode annenAdresse = new PostadressePeriode();
        annenAdresse.withEndretAv(ENDRET_AV);
        annenAdresse.withEndringstidspunkt(lagDato(2018, 4, 17));
        annenAdresse.withEndringstype(Endringstyper.ENDRET);
        annenAdresse.withPostadresse(lagUtenlandsAdresse(lagDato(2018, 4, 17), Endringstyper.ENDRET, "GBR"));
        annenAdresse.withPeriode(lagPeriode(lagDato(2018, 4, 17), ConversionUtils.convertToXMLGregorianCalendar(LocalDate.now())));

        resultat.add(annenAdresse);
        return resultat;
    }

    private List<StatsborgerskapPeriode> personStatsborgerskapPerson1() {
        List<StatsborgerskapPeriode> resultat = new ArrayList<>();

        StatsborgerskapPeriode periode1 = new StatsborgerskapPeriode();
        periode1.withEndretAv(ENDRET_AV);
        periode1.withEndringstidspunkt(TIDENES_MORGEN);
        periode1.withPeriode(lagPeriode(TIDENES_MORGEN, lagDato(2018, 4, 16)));
        periode1.withEndringstype(Endringstyper.NY);

        Statsborgerskap norsk = new Statsborgerskap();
        norsk.withEndretAv(ENDRET_AV);
        norsk.withEndringstidspunkt(TIDENES_MORGEN);
        norsk.withEndringstype(Endringstyper.NY);
        Landkoder norge = new Landkoder();
        norge.withKodeRef("Landkoder");
        norge.setValue("NOR");
        norge.setKodeRef("NOR");
        norsk.withLand(norge);

        StatsborgerskapPeriode periode2 = new StatsborgerskapPeriode();
        periode2.withEndretAv(ENDRET_AV);
        periode2.withEndringstidspunkt(lagDato(2018, 4, 17));
        periode2.withEndringstype(Endringstyper.NY);
        periode2.withPeriode(lagPeriode(lagDato(2018, 4, 17), ConversionUtils.convertToXMLGregorianCalendar(LocalDate.now())));
        periode2.withEndringstype(Endringstyper.NY);

        Statsborgerskap britisk = new Statsborgerskap();
        britisk.withEndretAv(ENDRET_AV);
        britisk.withEndringstidspunkt(TIDENES_MORGEN);
        britisk.withEndringstype(Endringstyper.NY);
        Landkoder england = new Landkoder();
        england.withKodeRef("Landkoder");
        england.setValue("NOR");
        england.setKodeRef("NOR");
        britisk.withLand(england);

        periode1.withStatsborgerskap(norsk);
        periode2.withStatsborgerskap(britisk);

        resultat.add(periode1);
        resultat.add(periode2);

        return resultat;
    }

    private List<PersonstatusPeriode> personstatusPerson1() {
        List<PersonstatusPeriode> resultat = new ArrayList<>();

        PersonstatusPeriode bosa = new PersonstatusPeriode();
        bosa.withEndretAv(ENDRET_AV);
        bosa.withEndringstidspunkt(TIDENES_MORGEN);
        bosa.withEndringstype(Endringstyper.NY);
        bosa.withPersonstatus(lagPersonstatuser("BOSA"));
        bosa.withPeriode(lagPeriode(TIDENES_MORGEN, lagDato(2018, 4, 16)));

        PersonstatusPeriode utva = new PersonstatusPeriode();
        utva.withEndretAv(ENDRET_AV);
        utva.withEndringstidspunkt(lagDato(2018, 4, 17));
        utva.withEndringstype(Endringstyper.ENDRET);
        utva.withPersonstatus(lagPersonstatuser("UTVA"));
        utva.withPeriode(lagPeriode(lagDato(2018, 4, 17), ConversionUtils.convertToXMLGregorianCalendar(LocalDate.now())));
        resultat.add(bosa);
        resultat.add(utva);

        return resultat;
    }

    private XMLGregorianCalendar lagDato(int year, int month, int day) {
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

    private Bostedsadresse lagBostedadresse() {
        Bostedsadresse bostedsadresse = new Bostedsadresse();
        bostedsadresse.withEndretAv(ENDRET_AV);
        bostedsadresse.withEndringstidspunkt(ConversionUtils.convertToXMLGregorianCalendar(LocalDate.now()));
        bostedsadresse.withEndringstype(Endringstyper.ENDRET);
        bostedsadresse.withStrukturertAdresse(lagStrukturertAdresse());

        return bostedsadresse;
    }

    private Bostedsadresse lagBostedadresse(XMLGregorianCalendar endringstidspunkt, Endringstyper endringstyper) {
        Bostedsadresse bostedsadresse = new Bostedsadresse();
        bostedsadresse.withEndretAv(ENDRET_AV);
        bostedsadresse.withEndringstidspunkt(endringstidspunkt);
        bostedsadresse.withEndringstype(endringstyper);
        bostedsadresse.withStrukturertAdresse(lagStrukturertAdresse());
        return bostedsadresse;
    }

    private Postadresse lagUtenlandsAdresse(XMLGregorianCalendar endringstidspunkt, Endringstyper endringstyper, String landkode) {

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

    private StrukturertAdresse lagStrukturertAdresse() {
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

    private Personstatuser lagPersonstatuser(String personstatus) {
        Personstatuser personstatuser = new Personstatuser();
        personstatuser.setKodeverksRef("Personstatuser");
        personstatuser.setKodeRef(personstatus);
        personstatuser.setValue(personstatus);
        return personstatuser;
    }

    private Periode lagPeriode(LocalDate fom, LocalDate tom) {
        Periode periode = new Periode();
        periode.withFom(ConversionUtils.convertToXMLGregorianCalendar(fom));
        periode.withFom(ConversionUtils.convertToXMLGregorianCalendar(tom));
        return periode;
    }

    private Periode lagPeriode(XMLGregorianCalendar fom, XMLGregorianCalendar tom) {
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
