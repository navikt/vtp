package no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.modell;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "ArenaYtelseskontrakt")
@Table(name = "ARENAYTELSESKONTRAKT")
public class ArenaYtelseskontrakt {

    @Id
    @Column(name = "ID")
    Long id;

    @ManyToOne
    @JoinColumn(name = "ID_ARENASVAR")
    ArenaSvar arenaSvar;

    @Column(name = "DATO_KRAV_MOTATT")
    LocalDateTime datoKravMottatt;

    @Column(name = "GYLDIGHETSPERIODE_FOM")
    LocalDateTime fomGyldighetsperiode;

    @Column(name = "GYLDIGHETSPERIODE_TOM")
    LocalDateTime tomGyldighetsperiode;

    @Column(name = "FAGSYSTEM_SAK_ID")
    Long fagsystemSakId;

    @Column(name = "STATUS")
    String status;

    @Column(name = "YTELSESTYPE")
    String ytelsestype;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "arenaYtelseskontrakt")
    private List<ArenaVedtak> vedtakListe = new ArrayList<>();

    @Column(name = "BORTFALL_PROSENT_DAGER_IGJEN")
    Long bortfallsprosentDagerIgjen;

    @Column(name = "BORTFALL_PROSENT_UKER_IGJEN")
    Long bortfallsprosentUkerIgjen;


    ArenaYtelseskontrakt() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArenaSvar getArenaSvar() {
        return arenaSvar;
    }

    public void setArenaSvar(ArenaSvar arenaSvar) {
        this.arenaSvar = arenaSvar;
    }

    public LocalDateTime getDatoKravMottatt() {
        return datoKravMottatt;
    }

    public void setDatoKravMottatt(LocalDateTime datoKravMottatt) {
        this.datoKravMottatt = datoKravMottatt;
    }

    public LocalDateTime getFomGyldighetsperiode() {
        return fomGyldighetsperiode;
    }

    public void setFomGyldighetsperiode(LocalDateTime fomGyldighetsperiode) {
        this.fomGyldighetsperiode = fomGyldighetsperiode;
    }

    public LocalDateTime getTomGyldighetsperiode() {
        return tomGyldighetsperiode;
    }

    public void setTomGyldighetsperiode(LocalDateTime tomGyldighetsperiode) {
        this.tomGyldighetsperiode = tomGyldighetsperiode;
    }

    public Long getFagsystemSakId() {
        return fagsystemSakId;
    }

    public void setFagsystemSakId(Long fagsystemSakId) {
        this.fagsystemSakId = fagsystemSakId;
    }

    public List<ArenaVedtak> getIhtVedtak() {
        return vedtakListe;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getYtelsestype() {
        return ytelsestype;
    }

    public void setYtelsestype(String ytelsestype) {
        this.ytelsestype = ytelsestype;
    }

    public Long getBortfallsprosentDagerIgjen() {
        return bortfallsprosentDagerIgjen;
    }

    public void setBortfallsprosentDagerIgjen(Long bortfallsprosentDagerIgjen) {
        this.bortfallsprosentDagerIgjen = bortfallsprosentDagerIgjen;
    }

    public Long getBortfallsprosentUkerIgjen() {
        return bortfallsprosentUkerIgjen;
    }

    public void setBortfallsprosentUkerIgjen(Long bortfallsprosentUkerIgjen) {
        this.bortfallsprosentUkerIgjen = bortfallsprosentUkerIgjen;
    }
}
