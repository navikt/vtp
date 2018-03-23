package no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.modell;


import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ARENAMU_MELDEKORT")
public class ArenaMUMeldekort implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy=SEQUENCE, generator="SEQ_ARENAMU_MELDEKORT")
    @SequenceGenerator(name="SEQ_ARENAMU_MELDEKORT", sequenceName="SEQ_ARENAMU_MELDEKORT")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ARENAMU_VEDTAK_ID", nullable = false)
    ArenaMUVedtak arenaMuVedtak;

    @Column(name = "MELDEKORTFOM", nullable = false)
    private String meldekortFom;

    @Column(name = "MELDEKORTTOM", nullable = false)
    private String meldekortTom;

    @Column(name = "DAGSATS", nullable = false)
    private BigDecimal dagsats;

    @Column(name = "BELOEP", nullable = false)
    private BigDecimal beløp;

    @Column(name = "UTBETALINGSGRAD", nullable = false)
    private BigDecimal utbetalingsGrad;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public ArenaMUMeldekort(){}

    public Long getId() {
        return id;
    }

    public ArenaMUVedtak getArenaVedtak() {
        return arenaMuVedtak;
    }

    public String getMeldekortFom() {
        return meldekortFom;
    }

    public String getMeldekortTom() {
        return meldekortTom;
    }

    public BigDecimal getDagsats() {
        return dagsats;
    }

    public BigDecimal getBeløp() {
        return beløp;
    }

    public BigDecimal getUtbetalingsGrad() {
        return utbetalingsGrad;
    }

}
