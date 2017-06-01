package no.nav.tjeneste.virksomhet.person.v2.modell;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import no.nav.tjeneste.virksomhet.person.v2.informasjon.Familierelasjon;
import no.nav.tjeneste.virksomhet.person.v2.informasjon.Familierelasjoner;
import no.nav.tjeneste.virksomhet.person.v2.informasjon.Foedselsdato;
import no.nav.tjeneste.virksomhet.person.v2.informasjon.Kjoenn;
import no.nav.tjeneste.virksomhet.person.v2.informasjon.Kjoennstyper;
import no.nav.tjeneste.virksomhet.person.v2.informasjon.NorskIdent;
import no.nav.tjeneste.virksomhet.person.v2.informasjon.Person;
import no.nav.tjeneste.virksomhet.person.v2.informasjon.Personidenter;
import no.nav.tjeneste.virksomhet.person.v2.informasjon.Personnavn;

public class PersonBygger {
    private String fnr;
    private String fornavn;
    private String etternavn;
    private final Kjønn kjønn;
    private LocalDate fødselsdato;;
    private TpsRelasjon tpsRelasjon;

    public enum Kjønn {
        MANN("M"),
        KVINNE("K");

        String verdi;

        Kjønn(String verdi) {
            this.verdi = verdi;
        }
    }

    public PersonBygger(TpsPerson tpsPerson) {
        this.fnr = tpsPerson.fnr;
        this.fornavn = tpsPerson.fornavn;
        this.etternavn = tpsPerson.etternavn;

        if ("M".equals(tpsPerson.kjønn)) {
            this.kjønn = Kjønn.MANN;

        } else if ("K".equals(tpsPerson.kjønn)) {
            this.kjønn = Kjønn.KVINNE;

        } else {
            this.kjønn = null;
        }
    }

    public PersonBygger(String fnr, String fornavn, String etternavn, Kjønn kjønn) {
        Objects.requireNonNull(fnr, "Fødselsnummer er obligatorisk");
        Objects.requireNonNull(kjønn, "Kjønn er obligatorisk");
        Objects.requireNonNull(fornavn, "Fornavn er obligatorisk");
        Objects.requireNonNull(etternavn, "Etternavn er obligatorisk");
        this.fnr = fnr;
        this.kjønn = kjønn;
        this.fornavn = fornavn;
        this.etternavn = etternavn;
    }

    public String getFnr() {
        return fnr;
    }

    public PersonBygger medFødseldato(LocalDate fødselsdato) {
        this.fødselsdato = fødselsdato;
        return this;
    }

    public PersonBygger medRelasjon(String relasjonsType, String relasjonFnr, String fornavn, String etternavn) {
        tpsRelasjon = new TpsRelasjon();
        tpsRelasjon.relasjonsType = relasjonsType;
        tpsRelasjon.relasjonFnr = relasjonFnr;
        tpsRelasjon.fornavn = fornavn;
        tpsRelasjon.etternavn = etternavn;
        return this;
    }

    public Person bygg() {
        Person person = new Person();

        // Ident
        NorskIdent norskIdent = new NorskIdent();
        norskIdent.setIdent(fnr);
        Personidenter personidenter = new Personidenter();
        personidenter.setValue("fnr");
        norskIdent.setType(personidenter);
        person.setIdent(norskIdent);

        // Kjønn
        Kjoenn kjonn = new Kjoenn();
        Kjoennstyper kjonnstype = new Kjoennstyper();
        kjonnstype.setValue(kjønn.verdi);
        kjonn.setKjoenn(kjonnstype);
        person.setKjoenn(kjonn);

        // Fødselsdato
        Foedselsdato fodselsdato = new Foedselsdato();
        XMLGregorianCalendar xcal;
        if (fødselsdato == null) {
            xcal = tilXmlGregorian(fnr);
        } else {
            xcal = tilXmlGregorian(fødselsdato);
        }
        fodselsdato.setFoedselsdato(xcal);
        person.setFoedselsdato(fodselsdato);

        // Navn
        Personnavn personnavn  = new Personnavn();
        personnavn.setEtternavn(etternavn.toUpperCase());
        personnavn.setFornavn(fornavn.toUpperCase());
        personnavn.setSammensattNavn(etternavn.toUpperCase() + " " + fornavn.toUpperCase());
        person.setPersonnavn(personnavn);

        // Relasjoner
        if(tpsRelasjon != null) {
            person = new RelasjonBygger(tpsRelasjon).byggFor(person);
        }

        return person;
    }

    private XMLGregorianCalendar tilXmlGregorian(LocalDate fødselsdato) {
        XMLGregorianCalendar xcal;
        try {
            GregorianCalendar gcal = GregorianCalendar.from(fødselsdato.atStartOfDay(ZoneId.systemDefault()));
            xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
        } catch (DatatypeConfigurationException e) {
            throw new IllegalStateException("Kunne ikke konvertere dato", e);
        }
        return xcal;
    }

    static XMLGregorianCalendar tilXmlGregorian(String fødselsnummer) {
        XMLGregorianCalendar xcal;
        try {
            // Simpel algoritme for å konvertere fnr. Må utbedres ved behov.
            DateFormat format = new SimpleDateFormat("ddMMyy");
            Date dato = format.parse(fødselsnummer.substring(0, 6));
            GregorianCalendar gcal = new GregorianCalendar();
            gcal.setTime(dato);
            xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
        } catch (DatatypeConfigurationException | ParseException e) {
            throw new IllegalStateException("Kunne ikke konvertere dato", e);
        }
        return xcal;
    }


}
