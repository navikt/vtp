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
public class TpsRelasjon {

    @Id
    @Column(name = "ID", nullable = false)
    long id;

    @Column(name = "FNR", nullable = false)
    public
    String fnr;

    @Column(name = "FNR_RELASJON", nullable = false)
    public
    String relasjonFnr;

    @Column(name = "RELASJONSTYPE", nullable = false)
    public
    String relasjonsType;

    @Column(name = "FORNAVN", nullable = false)
    public
    String fornavn;

    @Column(name = "ETTERNAVN", nullable = false)
    public
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