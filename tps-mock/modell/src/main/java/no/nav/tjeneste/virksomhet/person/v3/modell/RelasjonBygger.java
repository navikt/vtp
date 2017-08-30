package no.nav.tjeneste.virksomhet.person.v3.modell;

import no.nav.tjeneste.virksomhet.person.v3.informasjon.Bruker;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Familierelasjon;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Familierelasjoner;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Foedselsdato;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.NorskIdent;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.PersonIdent;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Personidenter;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Personnavn;

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

        // Relasjon settes på personen
        familierelasjon.setTilPerson(relatertPerson);
        person.getHarFraRolleI().add(familierelasjon);

        return person;
    }

}
