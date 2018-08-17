package no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.modell;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "ArenaSvar")
@Table(name = "ARENASVAR")
public class ArenaSvar {

    @Id
    @Column(name = "ID")
    Long id;

    @Column(name = "FNR")
    String fnr;

    @Column(name = "RETTIGHETSGRUPPE")
    String rettighetsGruppe;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "arenaSvar")
    private List<ArenaYtelseskontrakt> ytelseskontraktListe = new ArrayList<>();

    ArenaSvar(){
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

    public String getRettighetsGruppe() {
        return rettighetsGruppe;
    }

    public void setRettighetsGruppe(String rettighetsGruppe) {
        this.rettighetsGruppe = rettighetsGruppe;
    }

    public List<ArenaYtelseskontrakt> getYtelseskontraktListe() {
        return ytelseskontraktListe;
    }

    public void addArenaYtelseskontrakt(ArenaYtelseskontrakt arenaYtelseskontrakt){
        Objects.requireNonNull(arenaYtelseskontrakt, "arenaYtelseskontrakt");
        if (!ytelseskontraktListe.contains(arenaYtelseskontrakt)) {
            ytelseskontraktListe.add(arenaYtelseskontrakt);
        }
    }

}
