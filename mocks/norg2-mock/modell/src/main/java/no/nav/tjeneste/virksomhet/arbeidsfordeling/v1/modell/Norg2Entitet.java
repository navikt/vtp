package no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.modell;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "Norg2")
@Table(name = "NORG2")
public class Norg2Entitet {

    @Id
    private Long id;
    private String nokkel;
    private String verdi;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNokkel() {
        return nokkel;
    }

    public void setNokkel(String nokkel) {
        this.nokkel = nokkel;
    }

    public String getVerdi() {
        return verdi;
    }

    public void setVerdi(String verdi) {
        this.verdi = verdi;
    }
}
