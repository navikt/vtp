package no.nav.domain;

import java.io.Serializable;
import java.util.List;

public class OpptjeningsGrunnlag implements Serializable {
    private static final long serialVersionUID = -1857770826401757298L;

    private List<Inntekt> inntektListe;
    private List<Omsorg> omsorgListe;
    private List<Dagpenger> dagpengerListe;
    private Forstegangstjeneste forstegangstjeneste;

    /**
     * Gets the inntekt this opptjeningsGrunnlag is made up of
     *
     * @return the inntekt
     */
    public List<Inntekt> getInntektListe() {
        return inntektListe;
    }

    /**
     * Sets the inntekt this opptjeningsGrunnlag is made up of
     *
     * @param inntektListe the inntekt to set
     */
    public void setInntektListe(List<Inntekt> inntektListe) {
        this.inntektListe = inntektListe;
    }

    /**
     * Gets the dagpenger this opptjeningsGrunnlag is made up of
     *
     * @return the dagpengerListe
     */
    public List<Dagpenger> getDagpengerListe() {
        return dagpengerListe;
    }

    /**
     * Sets the dagpenger this opptjeningsGrunnlag is made up of
     *
     * @param dagpengerListe the dagpengerListe to set
     */
    public void setDagpengerListe(List<Dagpenger> dagpengerListe) {
        this.dagpengerListe = dagpengerListe;
    }

    /**
     * Gets the forstegangstjeneste opptjeningsGrunnlag is made up of
     *
     * @return
     */
    public Forstegangstjeneste getForstegangstjeneste() {
        return forstegangstjeneste;
    }

    /**
     * Sets the forstegangstjeneste opptjeningsGrunnlags is made up of
     *
     * @param forstegangstjeneste
     */
    public void setForstegangstjeneste(Forstegangstjeneste forstegangstjeneste) {
        this.forstegangstjeneste = forstegangstjeneste;
    }

    /**
     * Gets the omsorg this opptjeningsGrunnlag is made up of
     *
     * @return the omsorgListe
     */
    public List<Omsorg> getOmsorgListe() {
        return omsorgListe;
    }

    /**
     * Sets the omsorg this opptjeningsGrunnlag is made up of
     *
     * @param omsorgListe the omsorgListe to set
     */
    public void setOmsorgListe(List<Omsorg> omsorgListe) {
        this.omsorgListe = omsorgListe;
    }
}
