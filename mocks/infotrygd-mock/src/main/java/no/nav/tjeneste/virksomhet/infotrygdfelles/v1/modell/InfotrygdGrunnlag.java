package no.nav.tjeneste.virksomhet.infotrygdfelles.v1.modell;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name = "InfotrygdGrunnlag")
@Table(name = "INFOTRYGDGRUNNLAG")
public class InfotrygdGrunnlag {

    @Id
    @Column(name = "ID", nullable = false)
    Long id;

    @ManyToOne
    @JoinColumn(name = "INFOTRYGDSVAR_ID", nullable = false)
    InfotrygdSvar infotrygdSvar;

    @Column(name = "STARTDATO", nullable = false)
    LocalDate startDato;

    // BrukerModell SP, FA, PS, ES,
    @Column(name = "TEMA", nullable = false)
    String tema;

    @Column(name = "BEHANDLINGSTEMA", nullable = false)
    String behandlingstema;

    @Column(name = "PERIODE_FOM", nullable = false)
    LocalDate periodeFom;

    @Column(name = "PERIODE_TOM", nullable = false)
    LocalDate periodeTom;

    @Column(name = "ARBEIDSKATEGORI")
    String arbeidsKategori;

    @Column(name = "OPPR_STDATO")
    LocalDate opprinneligStartDato;

    // Foreldrepenger: dekningsGrad, Sykepenger: inntektsgrunnlagProsent
    @Column(name = "DEKNING_PST")
    Long dekningProsent;

    @Column(name = "GRADERING")
    Long gradering;

    // Foreldrepenger: foedselsdatoBarn, PårørendeSykdom: foedselsdatoPleietrengende
    @Column(name = "FDATO_ANNEN")
    LocalDate foedselsDatoAnnenpart;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "infotrygdGrunnlag", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<InfotrygdVedtak> infotrygdVedtakList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "infotrygdGrunnlag", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<InfotrygdArbeid> infotrygdArbeidList = new ArrayList<>();

    InfotrygdGrunnlag() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InfotrygdSvar getInfotrygdSvar() {
        return infotrygdSvar;
    }

    public LocalDate getStartDato() {
        return startDato;
    }

    public void setStartDato(LocalDate startDato) {
        this.startDato = startDato;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getBehandlingstema() {
        return behandlingstema;
    }

    public void setBehandlingstema(String behandlingstema) {
        this.behandlingstema = behandlingstema;
    }

    public LocalDate getPeriodeFom() {
        return periodeFom;
    }

    public void setPeriodeFom(LocalDate periodeFom) {
        this.periodeFom = periodeFom;
    }

    public LocalDate getPeriodeTom() {
        return periodeTom;
    }

    public void setPeriodeTom(LocalDate periodeTom) {
        this.periodeTom = periodeTom;
    }

    public String getArbeidsKategori() {
        return arbeidsKategori;
    }

    public void setArbeidsKategori(String arbeidsKategori) {
        this.arbeidsKategori = arbeidsKategori;
    }

    public LocalDate getOpprinneligStartDato() {
        return opprinneligStartDato;
    }

    public void setOpprinneligStartDato(LocalDate opprinneligStartDato) {
        this.opprinneligStartDato = opprinneligStartDato;
    }

    public Long getDekningProsent() {
        return dekningProsent;
    }

    public void setDekningProsent(Long dekningProsent) {
        this.dekningProsent = dekningProsent;
    }

    public Long getGradering() {
        return gradering;
    }

    public void setGradering(Long gradering) {
        this.gradering = gradering;
    }

    public LocalDate getFoedselsDatoAnnenpart() {
        return foedselsDatoAnnenpart;
    }

    public void setFoedselsDatoAnnenpart(LocalDate foedselsDatoAnnenpart) {
        this.foedselsDatoAnnenpart = foedselsDatoAnnenpart;
    }

    public List<InfotrygdVedtak> getInfotrygdVedtakList(){
        return infotrygdVedtakList;
    }

    public void addInfotrygdVedtak(InfotrygdVedtak infotrygdVedtak){
        Objects.requireNonNull(infotrygdVedtak, "oppdrag116A");
        if (!infotrygdVedtakList.contains(infotrygdVedtak)) {
            infotrygdVedtakList.add(infotrygdVedtak);
        }
    }

    public List<InfotrygdArbeid> getInfotrygdArbeidList() {
        return infotrygdArbeidList;
    }

    public void addInfotrygdArbeid(InfotrygdArbeid infotrygdArbeid) {
        Objects.requireNonNull(infotrygdArbeid, "oppdrag116B");
        if (!infotrygdArbeidList.contains(infotrygdArbeid)) {
            infotrygdArbeidList.add(infotrygdArbeid);
        }
    }
}

