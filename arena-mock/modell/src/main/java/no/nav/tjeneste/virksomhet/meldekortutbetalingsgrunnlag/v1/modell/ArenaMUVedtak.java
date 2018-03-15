package no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.modell;


import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ARENAMU_VEDTAK")
public class ArenaMUVedtak implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy=SEQUENCE, generator="SEQ_ARENAMU_VEDTAK")
    @SequenceGenerator(name="SEQ_ARENAMU_VEDTAK", sequenceName="SEQ_ARENAMU_VEDTAK")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ARENAMU_SAK_ID", nullable = false)
    ArenaMUSak arenaMuSak;

    @Column(name = "VEDTAKSTATUS", nullable = false)
    private String vedtakStatus;

    @Column(name = "VEDTAKSPERIODEFOM", nullable = false)
    private String vedtaksperiodefom;

    @Column(name = "VEDTAKSPERIODETOM")
    private String vedtaksperiodetom;

    @Column(name = "VEDTAKSDATO")
    private String vedtaksdato;

    @Column(name = "KRAVMOTTATTDATO", nullable = false)
    private String kravMottattDato;

    @OneToMany(mappedBy = "arenaMuVedtak", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ArenaMUMeldekort> meldekort = new ArrayList<>();

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public ArenaMUVedtak(){}

    public Long getId() {
        return id;
    }

    public ArenaMUSak getMuSak() {
        return arenaMuSak;
    }

    public String getVedtakStatus() {
        return vedtakStatus;
    }

    public String getVedtakFom() {
        return vedtaksperiodefom;
    }

    public String getVedtakTom() {
        return vedtaksperiodetom;
    }

    public String getVedtakDato() {
        return vedtaksdato;
    }

    public String getKravMottattDato() {
        return kravMottattDato;
    }

    public List<ArenaMUMeldekort> getMeldekort() {
        return meldekort;
    }

}
