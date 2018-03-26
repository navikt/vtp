package no.nav.tjeneste.virksomhet.infotrygdfelles.v1.modell;

import javax.persistence.CascadeType;
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
    @Column(name = "ID", nullable = false)
    Long id;

    @Column(name = "FNR", nullable = false, unique = true)
    String fnr;

    @Column(name = "FEILKODE")
    String feilkode;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "infotrygdSvar", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<InfotrygdYtelse> infotrygdYtelseListe = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "infotrygdSvar", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<InfotrygdGrunnlag> infotrygdGrunnlagList = new ArrayList<>();


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

    public List<InfotrygdGrunnlag> getInfotrygdGrunnlagList(){
        return infotrygdGrunnlagList;
    }

    public void addInfotrygdGrunnlag(InfotrygdGrunnlag infotrygdGrunnlag){
        Objects.requireNonNull(infotrygdGrunnlag, "oppdrag116");
        if (!infotrygdGrunnlagList.contains(infotrygdGrunnlag)) {
            infotrygdGrunnlagList.add(infotrygdGrunnlag);
        }
    }
}
