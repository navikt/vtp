package no.nav.tjeneste.virksomhet.person.v3.modell;

import no.nav.tjeneste.virksomhet.person.v3.informasjon.Bruker;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Bydel;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Diskresjonskoder;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Foedselsdato;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.GeografiskTilknytning;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Kjoenn;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Kjoennstyper;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Kommune;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Land;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Landkoder;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.NorskIdent;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.PersonIdent;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Personidenter;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Personnavn;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Personstatus;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Personstatuser;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Postadressetyper;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Spraak;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Statsborgerskap;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class PersonBygger {
    private final String fnr;
    private final String fornavn;
    private final String etternavn;
    private final String kjønn;
    private final String maalform;
    private final String geografiskTilknytning;
    private final String diskresjonskode;
    private final String statsborgerskap;
    private final String gjeldendeAdresseType;

    public PersonBygger(TpsPerson tpsPerson) {
        this.fnr = tpsPerson.getFnr();
        this.fornavn = tpsPerson.getFornavn();
        this.etternavn = tpsPerson.getEtternavn();
        this.maalform = tpsPerson.getMaalform();
        this.geografiskTilknytning = tpsPerson.getGeografiskTilknytning();
        this.diskresjonskode = tpsPerson.getDiskresjonskode();
        this.statsborgerskap = tpsPerson.getStatsborgerskap();
        this.gjeldendeAdresseType = tpsPerson.getGjeldendeAdresseType();
        this.kjønn = tpsPerson.getKjønn();
    }

    public String getFnr() {
        return fnr;
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
        kjonnstype.setValue(kjønn);
        kjonn.setKjoenn(kjonnstype);
        bruker.setKjoenn(kjonn);

        // Fødselsdato
        Foedselsdato fodselsdato = new Foedselsdato();
        XMLGregorianCalendar xcal = tilXmlGregorian(fnr);
        fodselsdato.setFoedselsdato(xcal);
        bruker.setFoedselsdato(fodselsdato);

        // Navn
        Personnavn personnavn = new Personnavn();
        personnavn.setEtternavn(etternavn.toUpperCase());
        personnavn.setFornavn(fornavn.toUpperCase());
        personnavn.setSammensattNavn(etternavn.toUpperCase() + " " + fornavn.toUpperCase());
        bruker.setPersonnavn(personnavn);

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
        if (diskresjonskode != null) {
            Diskresjonskoder kode = new Diskresjonskoder();
            kode.setValue(diskresjonskode);
            bruker.setDiskresjonskode(kode);
        }

        //statsborgerskap
        Statsborgerskap s = new Statsborgerskap();
        Landkoder landkoder = new Landkoder();
        landkoder.setValue(statsborgerskap);
        s.setLand(landkoder);
        bruker.setStatsborgerskap(s);

        //Gjeldende adressetype:
        Postadressetyper postadressetyper = new Postadressetyper();
        postadressetyper.setValue(gjeldendeAdresseType);
        bruker.setGjeldendePostadressetype(postadressetyper);

        return bruker;
    }

    private GeografiskTilknytning opprettGeografiskTilknytning(String geografiskTilknytning) {
        //Regel fra tjenesten:
        //geografisk tilknytning returners enten som bydelsnr (6-sifret), kommunenr (4-sifret), landkode (3-sifret) eller BLANK
        if (geografiskTilknytning == null) {
            return null;
        }

        if (geografiskTilknytning.length() == 3) {
            Land land = new Land();
            land.setGeografiskTilknytning(geografiskTilknytning);
            return land;
        } else if (geografiskTilknytning.length() == 4) {
            Kommune kommune = new Kommune();
            kommune.setGeografiskTilknytning(geografiskTilknytning);
            return kommune;
        } else if (geografiskTilknytning.length() == 6) {
            Bydel bydel = new Bydel();
            bydel.setGeografiskTilknytning(geografiskTilknytning);
            return bydel;
        } else {
            throw new IllegalArgumentException("Ukjent geografisk tilknytning. MÅ være 3,4 eller 6 sifre, men var '" + geografiskTilknytning + "'");
        }
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
