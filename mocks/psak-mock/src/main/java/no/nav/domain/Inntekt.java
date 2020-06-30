package no.nav.domain;

import no.stelvio.domain.person.Pid;
import no.stelvio.domain.time.ChangeStamp;

import java.io.Serializable;

public class Inntekt implements Serializable {
    private static final long serialVersionUID = -2136847548272793125L;

    private Long inntektId;

    private Pid fnr;

    private String kilde;

    private String kommune;

    private String piMerke;

    private Integer inntektAr;

    private Long belop;

    private ChangeStamp changeStamp;

    private String inntektType;

    public Inntekt() {
    }

    public Inntekt copy() {
        Inntekt inntekt = new Inntekt();
        inntekt.setFnr(fnr != null ? new Pid(fnr.getPid()) : null);
        inntekt.setKilde(kilde);
        inntekt.setKommune(kommune);
        inntekt.setPiMerke(piMerke);
        inntekt.setInntektType(inntektType);
        inntekt.setInntektAr(inntektAr);
        inntekt.setBelop(belop);
        if (changeStamp != null) {
            ChangeStamp cs = changeStamp;
            inntekt.setChangeStamp(new ChangeStamp(cs.getCreatedBy(), cs.getCreatedDate(), cs.getUpdatedBy(), cs.getUpdatedDate()));
        }
        return inntekt;
    }

    /**
     * @return the belop
     */
    public Long getBelop() {
        return belop;
    }

    /**
     * @param belop the belop to set
     */
    public void setBelop(Long belop) {
        this.belop = belop;
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
     * @return the inntektAr
     */
    public Integer getInntektAr() {
        return inntektAr;
    }

    /**
     * @param inntektAr the inntektAr to set
     */
    public void setInntektAr(Integer inntektAr) {
        this.inntektAr = inntektAr;
    }

    /**
     * @return the inntektId
     */
    public Long getInntektId() {
        return inntektId;
    }

    /**
     * @param inntektId the inntektId to set
     */
    public void setInntektId(Long inntektId) {
        this.inntektId = inntektId;
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

    /**
     * @return the kommune
     */
    public String getKommune() {
        return kommune;
    }

    /**
     * @param kommune the kommune to set
     */
    public void setKommune(String kommune) {
        this.kommune = kommune;
    }

    /**
     * @return the piMerke
     */
    public String getPiMerke() {
        return piMerke;
    }

    /**
     * @param piMerke the piMerke to set
     */
    public void setPiMerke(String piMerke) {
        this.piMerke = piMerke;
    }

    /**
     * @return the inntektType
     */
    public String getInntektType() {
        return inntektType;
    }

    /**
     * @param inntektType the inntektType to set
     */
    public void setInntektType(String inntektType) {
        this.inntektType = inntektType;
    }
}