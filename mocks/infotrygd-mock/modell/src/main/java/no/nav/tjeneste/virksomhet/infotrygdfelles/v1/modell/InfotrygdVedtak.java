package no.nav.tjeneste.virksomhet.infotrygdfelles.v1.modell;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import no.nav.tjeneste.virksomhet.infotrygdfelles.v1.modell.InfotrygdGrunnlag;

@Entity(name = "InfotrygdVedtak")
@Table(name = "INFOTRYGDVEDTAK")
public class InfotrygdVedtak {

    @Id
    @Column(name = "ID", nullable = false)
    Long id;

    @ManyToOne
    @JoinColumn(name = "INFOTRYGDGLAG_ID", nullable = false)
    InfotrygdGrunnlag infotrygdGrunnlag;

    @Column(name = "ANVIST_FOM", nullable = false)
    LocalDate anvistFom;

    @Column(name = "ANVIST_TOM", nullable = false)
    LocalDate anvistTom;

    @Column(name = "UTBETALING_GRAD")
    Long utbetalingGrad;

    InfotrygdVedtak() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InfotrygdGrunnlag getInfotrygdGrunnlag() {
        return infotrygdGrunnlag;
    }

    public LocalDate getAnvistFom() {
        return anvistFom;
    }

    public void setAnvistFom(LocalDate anvistFom) {
        this.anvistFom = anvistFom;
    }

    public LocalDate getAnvistTom() {
        return anvistTom;
    }

    public void setAnvistTom(LocalDate anvistTom) {
        this.anvistTom = anvistTom;
    }

    public Long getUtbetalingGrad() {
        return utbetalingGrad;
    }

    public void setUtbetalingGrad(Long utbetalingGrad) {
        this.utbetalingGrad = utbetalingGrad;
    }
}

