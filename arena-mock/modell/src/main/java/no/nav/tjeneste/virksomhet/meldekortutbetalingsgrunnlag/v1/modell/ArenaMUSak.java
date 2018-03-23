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
@Table(name = "ARENAMU_SAK")
public class ArenaMUSak implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy=SEQUENCE, generator="SEQ_ARENAMU_SAK")
    @SequenceGenerator(name="SEQ_ARENAMU_SAK", sequenceName="SEQ_ARENAMU_SAK")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ARENAMU_AKTOER_ID", nullable = false)
    ArenaMUAktør aktoer;

    @Column(name = "ARENA_SAK_NR", nullable = false)
    private String saksnummer;

    @Column(name = "TEMA", nullable = false)
    private String tema;

    @Column(name = "SAKSTATUS", nullable = false)
    private String sakStatus;

    @OneToMany(mappedBy = "arenaMuSak", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ArenaMUVedtak> arenaMuVedtak = new ArrayList<>();

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public ArenaMUSak() {}

    public Long getId() {
        return id;
    }

    public ArenaMUAktør getAktoer() {
        return aktoer;
    }

    public String getSaksnummer() {
        return saksnummer;
    }

    public String getTema() {
        return tema;
    }

    public String getSakStatus() {
        return sakStatus;
    }

    public List<ArenaMUVedtak> getArenaVedtak() {
        return arenaMuVedtak;
    }

}
