package no.nav.tjeneste.virksomhet.person.v2.modell;

import static no.nav.tjeneste.virksomhet.person.v2.modell.PersonBygger.tilXmlGregorian;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import no.nav.tjeneste.virksomhet.person.v2.informasjon.Familierelasjon;
import no.nav.tjeneste.virksomhet.person.v2.informasjon.Familierelasjoner;
import no.nav.tjeneste.virksomhet.person.v2.informasjon.Foedselsdato;
import no.nav.tjeneste.virksomhet.person.v2.informasjon.NorskIdent;
import no.nav.tjeneste.virksomhet.person.v2.informasjon.Person;
import no.nav.tjeneste.virksomhet.person.v2.informasjon.Personidenter;
import no.nav.tjeneste.virksomhet.person.v2.informasjon.Personnavn;

public class RelasjonBygger {

    private TpsRelasjon tpsRelasjon;

    public RelasjonBygger(TpsRelasjon tpsRelasjon) {
        this.tpsRelasjon = tpsRelasjon;
    }


    public Person byggFor(Person person) {
        Familierelasjon familierelasjon = new Familierelasjon();
        familierelasjon.setHarSammeBosted(true);
        Familierelasjoner familierelasjoner = new Familierelasjoner();
        familierelasjoner.setValue(tpsRelasjon.relasjonsType);
        familierelasjon.setTilRolle(familierelasjoner);

        Person relatertPersjon = new Person();
        // Relasjonens ident
        NorskIdent relasjonIdent = new NorskIdent();
        relasjonIdent.setIdent(tpsRelasjon.relasjonFnr);
        Personidenter relasjonIdenter = new Personidenter();
        relasjonIdenter.setValue("fnr");
        relasjonIdent.setType(relasjonIdenter);
        relatertPersjon.setIdent(relasjonIdent);

        // Relasjonens fødselsdato
        if (tpsRelasjon.relasjonFnr != null) {
            Foedselsdato relasjonFodselsdato = new Foedselsdato();
            relasjonFodselsdato.setFoedselsdato(tilXmlGregorian(tpsRelasjon.relasjonFnr));
            relatertPersjon.setFoedselsdato(relasjonFodselsdato);
        }

        // Relasjonens navn
        Personnavn relasjonPersonnavn = new Personnavn();
        relasjonPersonnavn.setEtternavn(tpsRelasjon.etternavn.toUpperCase());
        relasjonPersonnavn.setFornavn(tpsRelasjon.fornavn.toUpperCase());
        relasjonPersonnavn.setSammensattNavn(tpsRelasjon.etternavn.toUpperCase() + " "
                + tpsRelasjon.fornavn.toUpperCase());
        relatertPersjon.setPersonnavn(relasjonPersonnavn);

        // Relasjon settes på personen
        familierelasjon.setTilPerson(relatertPersjon);
        person.getHarFraRolleI().add(familierelasjon);

        return person;
    }

}
