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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "ARENAMU_AKTOER")
public class ArenaMUAktør implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy=SEQUENCE, generator="SEQ_ARENAMU_AKTOER")
    @SequenceGenerator(name="SEQ_ARENAMU_AKTOER", sequenceName="SEQ_ARENAMU_AKTOER")
    private Long id;

    @Column(name = "IDENT")
    private String ident;

    @Column(name = "FEILKODE")
    private String feilkode;

    @OneToMany(mappedBy = "aktoer", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ArenaMUSak> arenaSaker = new ArrayList<>();

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public ArenaMUAktør(){}

    public Long getId() {
        return id;
    }

    public String getIdent() {
        return ident;
    }

    public String getFeilkode() {
        return feilkode;
    }

    public List<ArenaMUSak> getArenaSaker() {
        return arenaSaker;
    }

}
