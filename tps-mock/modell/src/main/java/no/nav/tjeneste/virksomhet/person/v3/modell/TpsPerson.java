package no.nav.tjeneste.virksomhet.person.v3.modell;

import no.nav.tjeneste.virksomhet.person.v3.informasjon.Bruker;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@NamedQueries({
        @NamedQuery(name = "TpsPerson.findAll",
                query = "SELECT t FROM TpsPerson t")
})

@Entity(name = "TpsPerson")
@Table(name = "PERSON")
public class TpsPerson {

    // Konstanter for standardbrukere (kan refereres eksternt)
    public static final long STD_KVINNE_AKTØR_ID = 9000000000036L;
    public static final String STD_KVINNE_FNR = "03039004649";

    @Id
    @Column(name = "ID", nullable = false)
    private long id;

    @Column(name = "AKTORID", nullable = false)
    private long aktørId;

    @Column(name = "FNR", nullable = false)
    private String fnr;

    @Column(name = "KJONN")
    private String kjønn;

    @Column(name = "FORNAVN", nullable = false)
    private String fornavn;

    @Column(name = "ETTERNAVN", nullable = false)
    private String etternavn;

    @Column(name = "SPRAAK", nullable = true)
    private String maalform;

    @Column(name = "GEOTILKN", nullable = true)
    private String geografiskTilknytning;

    @Column(name = "BSPESREG", nullable = true)
    private String diskresjonskode;

    @Column(name = "STATSBORGERSKAP", nullable = false)
    private String statsborgerskap;

    @Column(name = "GJELDENDEPADRESSETYPE", nullable = false)
    private String gjeldendeAdresseType;

    @Transient
    private Bruker person;

    TpsPerson() {
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

    public Bruker getPerson() {
        return person;
    }

    public void setPerson(Bruker person) {
        this.person = person;
    }

    public String getFornavn() {
        return fornavn;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public String getMaalform() {
        return maalform;
    }

    public String getGeografiskTilknytning() {
        return geografiskTilknytning;
    }

    public String getDiskresjonskode() {
        return diskresjonskode;
    }

    public String getStatsborgerskap() {
        return statsborgerskap;
    }

    public String getGjeldendeAdresseType() {
        return gjeldendeAdresseType;
    }

    public String getKjønn() {
        return kjønn;
    }
}