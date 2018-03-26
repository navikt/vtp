package no.nav.tjeneste.virksomhet.medlemskap.v2.modell;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "Medlemsskapsperiode")
@Table(name = "MEDLEMSSKAPSPERIODE")
public class Medlemsskapsperiode {

    @Id
    @Column(name = "ID", nullable = false)
    private long id;

    @Column(name = "FNR", nullable = false)
    private String fnr;

    @Column(name = "DATO_FOM", nullable = false)
    private LocalDate datoFom;

    @Column(name = "DATO_TOM", nullable = false)
    private LocalDate datoTom;

    @Column(name = "DATO_BESLUTTET")
    private LocalDate datoBesluttet;

    @Column(name = "DEKNING")
    private String dekning;

    @Column(name = "LAND")
    private String land;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "LOVVALG")
    private String lovvalg;

    @Column(name = "KILDE")
    private String kilde;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "GRUNNLAG")
    private String grunnlag;

    @Column(name = "STUDIE_STATSBORGERLAND")
    private String studieStatsborgerland;

    @Column(name = "STUDIE_STUDIELAND")
    private String studieStudieland;

    Medlemsskapsperiode() {
    }

    public long getId() {
        return id;
    }

    public String getFnr() {
        return fnr;
    }

    public LocalDate getDatoFom() {
        return datoFom;
    }

    public LocalDate getDatoTom() {
        return datoTom;
    }

    public LocalDate getDatoBesluttet() {
        return datoBesluttet;
    }

    public String getDekning() {
        return dekning;
    }

    public String getType() {
        return type;
    }

    public String getLand() {
        return land;
    }

    public String getLovvalg() {
        return lovvalg;
    }

    public String getKilde() {
        return kilde;
    }

    public String getStatus() {
        return status;
    }

    public String getGrunnlag() {
        return grunnlag;
    }

    public String getStudieStatsborgerland() {
        return studieStatsborgerland;
    }

    public String getStudieStudieland() {
        return studieStudieland;
    }
}
