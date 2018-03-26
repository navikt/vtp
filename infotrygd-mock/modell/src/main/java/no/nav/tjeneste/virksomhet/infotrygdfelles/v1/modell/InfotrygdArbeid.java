package no.nav.tjeneste.virksomhet.infotrygdfelles.v1.modell;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "InfotrygdArbeid")
@Table(name = "INFOTRYGDARBEIDSFORHOLD")
public class InfotrygdArbeid {

    @Id
    @Column(name = "ID", nullable = false)
    Long id;

    @ManyToOne
    @JoinColumn(name = "INFOTRYGDGLAG_ID", nullable = false)
    InfotrygdGrunnlag infotrygdGrunnlag;

    @Column(name = "BELOEP")
    BigDecimal beløp;

    @Column(name = "INNT_PER_TYPE")
    String inntektPeriodeType;

    @Column(name = "ORGNR")
    String orgnr;

    InfotrygdArbeid() {
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

    public BigDecimal getBeløp() {
        return beløp;
    }

    public void setBeløp(BigDecimal beløp) {
        this.beløp = beløp;
    }

    public String getInntektPeriodeType() {
        return inntektPeriodeType;
    }

    public void setInntektPeriodeType(String inntektPeriodeType) {
        this.inntektPeriodeType = inntektPeriodeType;
    }

    public String getOrgnr() {
        return orgnr;
    }

    public void setOrgnr(String orgnr) {
        this.orgnr = orgnr;
    }
}

