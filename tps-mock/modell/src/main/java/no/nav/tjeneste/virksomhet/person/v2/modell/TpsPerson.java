package no.nav.tjeneste.virksomhet.person.v2.modell;

import no.nav.tjeneste.virksomhet.person.v2.informasjon.Person;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name = "TpsPerson")
@Table(name = "TPSPERSON")
class TpsPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TPSPERSON")
    private Long id;

    @Column(name = "AKTORID", nullable = false)
    long aktørId;

    @Column(name = "FNR", nullable = false)
    String fnr;

    @Transient
    Person person;

    TpsPerson() {
    }

    TpsPerson(long aktørId, PersonBygger personBygger) {
        this.aktørId = aktørId;
        this.fnr = personBygger.getFnr();
        this.person = personBygger.bygg();
    }

    public Long getId() {
        return id;
    }

    public long getAktørId() {
        return aktørId;
    }

    public String getFnr() {
        return fnr;
    }

    @Override
    public String toString() {
        return "TpsPerson{" +
                "id=" + id +
                ", aktørId=" + aktørId +
                ", fnr='" + fnr + '\'' +
                ", person=" + person +
                '}';
    }
}