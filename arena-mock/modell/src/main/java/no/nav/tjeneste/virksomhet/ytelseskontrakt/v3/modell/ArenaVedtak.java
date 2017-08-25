package no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.modell;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "ArenaVedtak")
@Table(name = "ARENAVEDTAK")
public class ArenaVedtak {

    @Id
    @Column(name = "ID")
    Long id;

    @ManyToOne
    @JoinColumn(name = "ID_YTELSESKONTRAKT")
    ArenaYtelseskontrakt arenaYtelseskontrakt;

    @Column(name = "BESLUTNINGSDATO")
    LocalDateTime beslutningsdato;

    @Column(name = "PERIODETYPE_FOR_YTELSE")
    String periodetypeForYtelse;

    @Column(name = "UTTAKSGRAD")
    Long uttaksgrad;

    @Column(name = "VEDTAK_BRUTTO_BELOP")
    Long vedtakBruttoBeloep;

    @Column(name = "VEDTAK_NETTO_BELOP")
    Long vedtakNettoBeloep;

    @Column(name = "PERIODE_FOM")
    LocalDateTime vedtaksperiodeFom;

    @Column(name = "PERIODE_TOM")
    LocalDateTime vedtaksperiodeTom;

    @Column(name = "STATUS")
    String status;

    @Column(name = "VEDTAKSTYPE")
    String vedtaksType;

    @Column(name = "AKTIVITETSFASE")
    String aktivitetsfase;

    @Column(name = "DAGSATS")
    Long dagsats;

    ArenaVedtak(){
    }

    public Long getId() {
        return id;
    }

    public ArenaYtelseskontrakt getArenaYtelse() {
        return arenaYtelseskontrakt;
    }

    public LocalDateTime getBeslutningsdato() {
        return beslutningsdato;
    }

    public void setBeslutningsdato(LocalDateTime beslutningsdato) {
        this.beslutningsdato = beslutningsdato;
    }

    public String getPeriodetypeForYtelse() {
        return periodetypeForYtelse;
    }

    public void setPeriodetypeForYtelse(String periodetypeForYtelse) {
        this.periodetypeForYtelse = periodetypeForYtelse;
    }

    public Long getUttaksgrad() {
        return uttaksgrad;
    }

    public void setUttaksgrad(Long uttaksgrad) {
        this.uttaksgrad = uttaksgrad;
    }

    public Long getVedtakBruttoBeloep() {
        return vedtakBruttoBeloep;
    }

    public void setVedtakBruttoBeloep(Long vedtakBruttoBeloep) {
        this.vedtakBruttoBeloep = vedtakBruttoBeloep;
    }

    public Long getVedtakNettoBeloep() {
        return vedtakNettoBeloep;
    }

    public void setVedtakNettoBeloep(Long vedtakNettoBeloep) {
        this.vedtakNettoBeloep = vedtakNettoBeloep;
    }

    public LocalDateTime getVedtakseriodeFom() {
        return vedtaksperiodeFom;
    }

    public void setVedtakseriodeFom(LocalDateTime vedtaksperiodeFom) {
        this.vedtaksperiodeFom = vedtaksperiodeFom;
    }

    public LocalDateTime getVedtaksperiodeTom() {
        return vedtaksperiodeTom;
    }

    public void setVedtaksperiodeTom(LocalDateTime vedtaksperiodeTom) {
        this.vedtaksperiodeTom = vedtaksperiodeTom;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVedtaksType() {
        return vedtaksType;
    }

    public void setVedtaksType(String vedtaksType) {
        this.vedtaksType = vedtaksType;
    }

    public String getAktivitetsfase() {
        return aktivitetsfase;
    }

    public void setAktivitetsfase(String aktivitetsfase) {
        this.aktivitetsfase = aktivitetsfase;
    }

    public Long getDagsats() {
        return dagsats;
    }

    public void setDagsats(Long dagsats) {
        this.dagsats = dagsats;
    }
}
