package no.nav.tjeneste.virksomhet.person.v2.modell;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NamedQueries({
        @NamedQuery(name="TpsRelasjon.findAll",
                    query="SELECT t FROM TpsRelasjon t")
})

@Entity(name = "TpsRelasjon")
@Table(name = "MOCK_TPS_PERSONRELASJONER")
class TpsRelasjon {

    @Id
    @Column(name = "ID", nullable = false)
    long id;

    @Column(name = "FNR", nullable = false)
    String fnr;

    @Column(name = "FNR_RELASJON", nullable = false)
    String relasjonFnr;

    @Column(name = "RELASJONSTYPE", nullable = false)
    String relasjonsType;

    @Column(name = "FORNAVN", nullable = false)
    String fornavn;

    @Column(name = "ETTERNAVN", nullable = false)
    String etternavn;

    public TpsRelasjon() {
    }

    public long getId() {
        return id;
    }

    public String getFnr() {
        return fnr;
    }

    public String getRelasjonFnr() {
        return relasjonFnr;
    }

    public String getRelasjonsType() {
        return relasjonsType;
    }

    public String getFornavn() {
        return fornavn;
    }

    public String getEtternavn() {
        return etternavn;
    }

    @Override
    public String toString() {
        return "TpsRelasjon{" +
                "id=" + id +
                ", fnr='" + fnr + '\'' +
                ", relasjonFnr='" + relasjonFnr + '\'' +
                ", relasjonsType='" + relasjonsType + '\'' +
                ", fornavn='" + fornavn + '\'' +
                ", etternavn='" + etternavn + '\'' +
                '}';
    }
}