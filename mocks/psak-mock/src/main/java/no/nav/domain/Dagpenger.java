package no.nav.domain;

import no.stelvio.domain.person.Pid;
import no.stelvio.domain.time.ChangeStamp;

import java.io.Serializable;

public class Dagpenger implements Serializable {
    private static final long serialVersionUID = -2380699536090247014L;

    private Long dagpengerId;

    private Pid fnr;

    private String dagpengerType;

    private String rapportType;

    private String kilde;

    private Integer ar;

    private Integer utbetalteDagpenger;

    private Integer uavkortetDagpengegrunnlag;

    private Integer ferietillegg;

    private Integer barnetillegg;

    private ChangeStamp changeStamp;

    public Dagpenger() {
    }

    public Dagpenger copy() {
        Dagpenger dagpenger = new Dagpenger();
        dagpenger.setFnr(fnr != null ? new Pid(fnr.getPid()) : null);
        dagpenger.setDagpengerType(dagpengerType);
        dagpenger.setRapportType(rapportType);
        dagpenger.setKilde(kilde);
        dagpenger.setAr(ar);
        dagpenger.setUtbetalteDagpenger(utbetalteDagpenger);
        dagpenger.setUavkortetDagpengegrunnlag(uavkortetDagpengegrunnlag);
        dagpenger.setFerietillegg(ferietillegg);
        dagpenger.setBarnetillegg(barnetillegg);
        if (changeStamp != null) {
            ChangeStamp cs = changeStamp;
            dagpenger.setChangeStamp(new ChangeStamp(cs.getCreatedBy(), cs.getCreatedDate(), cs.getUpdatedBy(), cs.getUpdatedDate()));
        }
        return dagpenger;
    }

    /**
     * @return the ar
     */
    public Integer getAr() {
        return ar;
    }

    /**
     * @param ar the ar to set
     */
    public void setAr(Integer ar) {
        this.ar = ar;
    }

    /**
     * @return the barnetillegg
     */
    public Integer getBarnetillegg() {
        return barnetillegg;
    }

    /**
     * @param barnetillegg the barnetillegg to set
     */
    public void setBarnetillegg(Integer barnetillegg) {
        this.barnetillegg = barnetillegg;
    }

    /**
     * @return the changeStamp
     */
    public ChangeStamp getChangeStamp() {
        return changeStamp;
    }

    /**
     * @param changeStamp the changeStamp to set
     */
    public void setChangeStamp(ChangeStamp changeStamp) {
        this.changeStamp = changeStamp;
    }

    /**
     * @return the dagpengerId
     */
    public Long getDagpengerId() {
        return dagpengerId;
    }

    /**
     * @param dagpengerId the dagpengerId to set
     */
    public void setDagpengerId(Long dagpengerId) {
        this.dagpengerId = dagpengerId;
    }

    /**
     * @return the ferietillegg
     */
    public Integer getFerietillegg() {
        return ferietillegg;
    }

    /**
     * @param ferietillegg the ferietillegg to set
     */
    public void setFerietillegg(Integer ferietillegg) {
        this.ferietillegg = ferietillegg;
    }

    /**
     * @return the fnr
     */
    public Pid getFnr() {
        return fnr;
    }

    /**
     * @param fnr the fnr to set
     */
    public void setFnr(Pid fnr) {
        this.fnr = fnr;
    }

    /**
     * @return the rapportType
     */
    public String getRapportType() {
        return rapportType;
    }

    /**
     * @param rapportType the rapportType to set
     */
    public void setRapportType(String rapportType) {
        this.rapportType = rapportType;
    }

    /**
     * @return the utbetalteDagpenger
     */
    public Integer getUtbetalteDagpenger() {
        return utbetalteDagpenger;
    }

    /**
     * @param utbetalteDagpenger the utbetalteDagpenger to set
     */
    public void setUtbetalteDagpenger(Integer utbetalteDagpenger) {
        this.utbetalteDagpenger = utbetalteDagpenger;
    }

    /**
     * @return the uavkortetDagpengegrunnlag
     */
    public Integer getUavkortetDagpengegrunnlag() {
        return uavkortetDagpengegrunnlag;
    }

    /**
     * @param uavkortetDagpengegrunnlag the uavkortetDagpengegrunnlag to set
     */
    public void setUavkortetDagpengegrunnlag(Integer uavkortetDagpengegrunnlag) {
        this.uavkortetDagpengegrunnlag = uavkortetDagpengegrunnlag;
    }

    /**
     * @return the dagpengerType
     */
    public String getDagpengerType() {
        return dagpengerType;
    }

    /**
     * @param dagpengerType the dagpengerType to set
     */
    public void setDagpengerType(String dagpengerType) {
        this.dagpengerType = dagpengerType;
    }

    /**
     * @return the kilde
     */
    public String getKilde() {
        return kilde;
    }

    /**
     * @param kilde the kilde to set
     */
    public void setKilde(String kilde) {
        this.kilde = kilde;
    }
}
