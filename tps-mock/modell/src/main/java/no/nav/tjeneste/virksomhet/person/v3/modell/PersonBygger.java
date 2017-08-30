package no.nav.tjeneste.virksomhet.person.v3.modell;

import no.nav.tjeneste.virksomhet.person.v3.informasjon.Bruker;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Bydel;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Diskresjonskoder;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Foedselsdato;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.GeografiskTilknytning;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Kjoenn;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Kjoennstyper;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Landkoder;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Kommune;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Land;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.NorskIdent;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.PersonIdent;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Personidenter;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Personnavn;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Personstatus;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Personstatuser;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Spraak;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Statsborgerskap;

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
    private LocalDate fødselsdato;;
    private String maalform;
    private String geografiskTilknytning;
    private String diskresjonskode;
    private String statsborgerskap;
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
        this.maalform = tpsPerson.maalform;
        this.geografiskTilknytning = tpsPerson.geografiskTilknytning;
        this.diskresjonskode = tpsPerson.diskresjonskode;
        this.statsborgerskap = tpsPerson.statsborgerskap;

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

    public Bruker bygg() {
        Bruker bruker = new Bruker();

        // Ident
        NorskIdent norskIdent = new NorskIdent();
        norskIdent.setIdent(fnr);
        Personidenter personidenter = new Personidenter();
        personidenter.setValue("fnr");
        norskIdent.setType(personidenter);

        PersonIdent personIdent = new PersonIdent();
        personIdent.setIdent(norskIdent);
        bruker.setAktoer(personIdent);

        // Kjønn
        Kjoenn kjonn = new Kjoenn();
        Kjoennstyper kjonnstype = new Kjoennstyper();
        kjonnstype.setValue(kjønn.verdi);
        kjonn.setKjoenn(kjonnstype);
        bruker.setKjoenn(kjonn);

        // Fødselsdato
        Foedselsdato fodselsdato = new Foedselsdato();
        XMLGregorianCalendar xcal;
        if (fødselsdato == null) {
            xcal = tilXmlGregorian(fnr);
        } else {
            xcal = tilXmlGregorian(fødselsdato);
        }
        fodselsdato.setFoedselsdato(xcal);
        bruker.setFoedselsdato(fodselsdato);

        // Navn
        Personnavn personnavn  = new Personnavn();
        personnavn.setEtternavn(etternavn.toUpperCase());
        personnavn.setFornavn(fornavn.toUpperCase());
        personnavn.setSammensattNavn(etternavn.toUpperCase() + " " + fornavn.toUpperCase());
        bruker.setPersonnavn(personnavn);

        // Relasjoner
        if(tpsRelasjon != null) {
            bruker = new RelasjonBygger(tpsRelasjon).byggFor(bruker);
        }

        // Personstatus
        Personstatus personstatus = new Personstatus();
        Personstatuser personstatuser = new Personstatuser();
        personstatuser.setValue("BOSA");
        personstatus.setPersonstatus(personstatuser);
        bruker.setPersonstatus(personstatus);

        //Målform
        Spraak spraak = new Spraak();
        spraak.setValue(maalform);
        bruker.setMaalform(spraak);

        //Geografisk tilknytning
        GeografiskTilknytning tilknytning = opprettGeografiskTilknytning(geografiskTilknytning);
        bruker.setGeografiskTilknytning(tilknytning);

        //Diskresjonskode
        Diskresjonskoder kode = new Diskresjonskoder();
        kode.setValue(diskresjonskode);
        bruker.setDiskresjonskode(kode);

        //statsborgerskap
        Statsborgerskap s = new Statsborgerskap();
        Landkoder landkoder = new Landkoder();
        landkoder.setValue(statsborgerskap);
        s.setLand(landkoder);
        bruker.setStatsborgerskap(s);

        return bruker;
    }

    private GeografiskTilknytning opprettGeografiskTilknytning(String geoTilkn) {
        //Regel fra tjenesten:
        //geografisk tilknytning returners enten som bydelsnr (6-sifret), kommunenr (4-sifret), landkode (3-sifret) eller BLANK
        if (geoTilkn == null) {
            return null;
        }

        if (geoTilkn.length() == 3) {
            Land land = new Land();
            land.setGeografiskTilknytning(geoTilkn);
            return land;
        } else if (geoTilkn.length() == 4) {
            Kommune kommune = new Kommune();
            kommune.setGeografiskTilknytning(geoTilkn);
            return kommune;
        } else if (geoTilkn.length() == 6) {
            Bydel bydel = new Bydel();
            bydel.setGeografiskTilknytning(geoTilkn);
            return bydel;
        }

        return null;
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
