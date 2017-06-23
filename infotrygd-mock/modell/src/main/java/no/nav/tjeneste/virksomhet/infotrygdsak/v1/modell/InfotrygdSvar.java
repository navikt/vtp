package no.nav.tjeneste.virksomhet.infotrygdsak.v1.modell;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "InfotrygdSvar")
@Table(name = "INFOTRYGDSVAR")
public class InfotrygdSvar {

    @Id
    @Column(name = "ID")
    Long id;

    @Column(name = "FNR")
    String fnr;

    @Column(name = "FEILKODE")
    String feilkode;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "infotrygdSvar")
    private List<InfotrygdYtelse> infotrygdYtelseListe = new ArrayList<>();

    InfotrygdSvar(){
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFnr() {
        return fnr;
    }

    public void setFnr(String fnr) {
        this.fnr = fnr;
    }

    public String getFeilkode() {
        return feilkode;
    }

    public void setFeilkode(String feilkode) {
        this.feilkode = feilkode;
    }

    public List<InfotrygdYtelse> getInfotrygdYtelseListe(){
        return infotrygdYtelseListe;
    }

    public void addInfotrygdYtelse(InfotrygdYtelse infotrygdYtelse){
        Objects.requireNonNull(infotrygdYtelse, "oppdrag110");
        if (!infotrygdYtelseListe.contains(infotrygdYtelse)) {
            infotrygdYtelseListe.add(infotrygdYtelse);
        }
    }
}
