package no.nav.tjeneste.virksomhet.person.v2.modell;

import no.nav.tjeneste.virksomhet.person.v2.informasjon.Person;

class TpsPerson {
    long aktørId;
    final String fnr;
    Person person;

    TpsPerson(long aktørId, PersonBygger personBygger) {
        this.aktørId = aktørId;
        this.fnr = personBygger.getFnr();
        this.person = personBygger.bygg();
    }
}