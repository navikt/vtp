package no.nav.tjeneste.virksomhet.person.v2.modell;

import no.nav.tjeneste.virksomhet.person.v2.informasjon.*;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public class PersonBygger {
    private String fnr;
    private String fornavn;
    private String etternavn;
    private final Kjønn kjønn;
    private LocalDate fødselsdato;
    private String barnetsFnr;
    private String barnFornavn;
    private String barnEtternavn;

    public enum Kjønn {
        MANN("M"),
        KVINNE("K");

        String verdi;

        Kjønn(String verdi) {
            this.verdi = verdi;
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

    public PersonBygger medBarn(String barnetsFnr, String barnetsFornavn, String barnetsEtternavn) {
        this.barnetsFnr = barnetsFnr;
        this.barnFornavn = barnetsFornavn;
        this.barnEtternavn = barnetsEtternavn;
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

        // Barn
        if(barnFornavn != null) {
            Familierelasjon familierelasjon = new Familierelasjon();
            familierelasjon.setHarSammeBosted(true);
            Familierelasjoner familierelasjoner = new Familierelasjoner();
            familierelasjoner.setValue("BARN");
            familierelasjon.setTilRolle(familierelasjoner);

            Person barn = new Person();
            // Barnets ident
            NorskIdent barnIdent = new NorskIdent();
            barnIdent.setIdent(fnr);
            Personidenter barnidenter = new Personidenter();
            barnidenter.setValue("fnr");
            barnIdent.setType(barnidenter);
            barn.setIdent(barnIdent);

            // Barnets fødselsdato
            if(barnetsFnr != null) {
                Foedselsdato barnetsFodselsdato = new Foedselsdato();
                barnetsFodselsdato.setFoedselsdato(tilXmlGregorian(barnetsFnr));
                barn.setFoedselsdato(barnetsFodselsdato);
            }

            // Barnets navn
            Personnavn barnnavn  = new Personnavn();
            barnnavn.setEtternavn(barnEtternavn.toUpperCase());
            barnnavn.setFornavn(barnFornavn.toUpperCase());
            barnnavn.setSammensattNavn(barnEtternavn.toUpperCase() + " " + barnFornavn.toUpperCase());
            barn.setPersonnavn(barnnavn);

            familierelasjon.setTilPerson(barn);
            person.getHarFraRolleI().add(familierelasjon);
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

    private XMLGregorianCalendar tilXmlGregorian(String fødselsnummer) {
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
