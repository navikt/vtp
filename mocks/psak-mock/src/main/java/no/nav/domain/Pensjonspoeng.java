package no.nav.domain;

import no.stelvio.domain.person.Pid;
import no.stelvio.domain.time.ChangeStamp;

import java.io.Serializable;

public class Pensjonspoeng implements Serializable {
    private static final long serialVersionUID = -7483919769504185663L;

    private Long pensjonspoengId;

    private Pid fnr;

    private Pid fnrOmsorgFor;

    private String kilde;

    private String pensjonspoengType;

    private Inntekt inntekt;

    private Omsorg omsorg;

    private Integer ar;

    private Integer anvendtPi;

    private Double poeng;

    private Integer maxUforegrad;

    private ChangeStamp changeStamp;

    /**
     * @return the anvendtPi
     */
    public Integer getAnvendtPi() {
        return anvendtPi;
    }

    /**
     * @param anvendtPi the anvendtPi to set
     */
    public void setAnvendtPi(Integer anvendtPi) {
        this.anvendtPi = anvendtPi;
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
     * @return the fnrOmsorgFor
     */
    public Pid getFnrOmsorgFor() {
        return fnrOmsorgFor;
    }

    /**
     * @param fnrOmsorgFor the fnrOmsorgFor to set
     */
    public void setFnrOmsorgFor(Pid fnrOmsorgFor) {
        this.fnrOmsorgFor = fnrOmsorgFor;
    }

    /**
     * @return the inntekt
     */
    public Inntekt getInntekt() {
        return inntekt;
    }

    /**
     * @param inntekt the inntekt to set
     */
    public void setInntekt(Inntekt inntekt) {
        this.inntekt = inntekt;
    }

    /**
     * @return the maxUforegrad
     */
    public Integer getMaxUforegrad() {
        return maxUforegrad;
    }

    /**
     * @param maxUforegrad the maxUforegrad to set
     */
    public void setMaxUforegrad(Integer maxUforegrad) {
        this.maxUforegrad = maxUforegrad;
    }

    /**
     * @return the omsorg
     */
    public Omsorg getOmsorg() {
        return omsorg;
    }

    /**
     * @param omsorg the omsorg to set
     */
    public void setOmsorg(Omsorg omsorg) {
        this.omsorg = omsorg;
    }

    /**
     * @return the pensjonspoengId
     */
    public Long getPensjonspoengId() {
        return pensjonspoengId;
    }

    /**
     * @param pensjonspoengId the pensjonspoengId to set
     */
    public void setPensjonspoengId(Long pensjonspoengId) {
        this.pensjonspoengId = pensjonspoengId;
    }

    /**
     * @return the poeng
     */
    public Double getPoeng() {
        return poeng;
    }

    /**
     * @param poeng the poeng to set
     */
    public void setPoeng(Double poeng) {
        this.poeng = poeng;
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
     * @return the pensjonspoengType
     */
    public String getPensjonspoengType() {
        return pensjonspoengType;
    }

    /**
     * @param pensjonspoengType the pensjonspoengType to set
     */
    public void setPensjonspoengType(String pensjonspoengType) {
        this.pensjonspoengType = pensjonspoengType;
    }
}
