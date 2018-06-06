package no.nav.tjeneste.virksomhet.person.v3.modell;

import no.nav.tjeneste.virksomhet.person.v3.informasjon.Bostedsadresse;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Bruker;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Familierelasjon;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Familierelasjoner;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Foedselsdato;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.NorskIdent;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.PersonIdent;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Personidenter;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Personnavn;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Postadressetyper;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Postnummer;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.StedsadresseNorge;

import static no.nav.tjeneste.virksomhet.person.v3.modell.PersonBygger.tilXmlGregorian;

public class RelasjonBygger {

    private TpsRelasjon tpsRelasjon;

    public RelasjonBygger(TpsRelasjon tpsRelasjon) {
        this.tpsRelasjon = tpsRelasjon;
    }


    public Bruker byggFor(Bruker person) {
        Familierelasjon familierelasjon = new Familierelasjon();
        familierelasjon.setHarSammeBosted(true);
        Familierelasjoner familierelasjoner = new Familierelasjoner();
        familierelasjoner.setValue(tpsRelasjon.getRelasjonsType());
        familierelasjon.setTilRolle(familierelasjoner);

        Bruker relatertPerson = new Bruker();

        // Relasjonens ident
        NorskIdent relasjonIdent = new NorskIdent();
        relasjonIdent.setIdent(tpsRelasjon.getRelasjonFnr());

        Personidenter relasjonIdenter = new Personidenter();
        relasjonIdenter.setValue("fnr");
        relasjonIdent.setType(relasjonIdenter);

        PersonIdent personIdent = new PersonIdent();
        personIdent.setIdent(relasjonIdent);
        relatertPerson.setAktoer(personIdent);

        // Relasjonens fødselsdato
        if (tpsRelasjon.getRelasjonFnr() != null) {
            Foedselsdato relasjonFodselsdato = new Foedselsdato();
            relasjonFodselsdato.setFoedselsdato(tilXmlGregorian(tpsRelasjon.getRelasjonFnr()));
            relatertPerson.setFoedselsdato(relasjonFodselsdato);
        }

        // Relasjonens navn
        Personnavn relasjonPersonnavn = new Personnavn();
        relasjonPersonnavn.setEtternavn(tpsRelasjon.getEtternavn().toUpperCase());
        relasjonPersonnavn.setFornavn(tpsRelasjon.getFornavn().toUpperCase());
        relasjonPersonnavn.setSammensattNavn(tpsRelasjon.getEtternavn().toUpperCase() + " " + tpsRelasjon.getFornavn().toUpperCase());
        relatertPerson.setPersonnavn(relasjonPersonnavn);

        //Gjeldende adressetype: VI hardkoder en adresse inntil videre så mock kan benyttes. Utvid gjerne til å lese adresse info fra DB for forskjellige adressetyper.
        //TODO (jannilsen): RelatertPerson bør vel opprettes vha personbygger, men inntil videre så legger vi på adresseinfo her og.
        Postadressetyper postadressetyper = new Postadressetyper();
        postadressetyper.setValue("BOSTEDSADRESSE");
        relatertPerson.setGjeldendePostadressetype(postadressetyper);
        //Bostedsadresse krever følgende felter:
        Bostedsadresse adresse = new Bostedsadresse();
        StedsadresseNorge stedsadresseNorge = new StedsadresseNorge();
        Postnummer postnummer = new Postnummer();
        postnummer.setValue("2040");
        stedsadresseNorge.setPoststed(postnummer);
        adresse.setStrukturertAdresse(stedsadresseNorge);
        relatertPerson.setBostedsadresse(adresse);

        // Relasjon settes på personen
        familierelasjon.setTilPerson(relatertPerson);
        person.getHarFraRolleI().add(familierelasjon);

        return person;
    }

}
