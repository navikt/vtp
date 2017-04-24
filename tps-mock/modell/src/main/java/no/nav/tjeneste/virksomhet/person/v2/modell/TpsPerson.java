package no.nav.tjeneste.virksomhet.person.v2.modell;

import no.nav.tjeneste.virksomhet.person.v2.informasjon.Person;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@NamedQueries({
    @NamedQuery(name="TpsPerson.findAll",
                query="SELECT t FROM TpsPerson t")
})

@Entity(name = "TpsPerson")
@Table(name = "MOCK_TPS_PERSONER")
public class TpsPerson {

    @Id
    @Column(name = "ID", nullable = false)
    long id;

    @Column(name = "AKTORID", nullable = false)
    long aktørId;

    @Column(name = "FNR", nullable = false)
    String fnr;

    @Column(name = "KJONN")
    public String kjønn;

    @Column(name = "FORNAVN", nullable = false)
    public String fornavn;

    @Column(name = "ETTERNAVN", nullable = false)
    public String etternavn;

    @Transient
    public Person person;

    TpsPerson() {
    }

    public TpsPerson(long aktørId, PersonBygger personBygger) {
        this.aktørId = aktørId;
        this.fnr = personBygger.getFnr();
        this.person = personBygger.bygg();
    }

    public long getId() {
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
                ", aktørId=" + aktørId +
                ", fnr='" + fnr + '\'' +
                ", person=" + person +
                '}';
    }
}