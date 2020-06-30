package no.nav.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import no.stelvio.domain.person.Pid;
import no.stelvio.domain.time.ChangeStamp;

import java.io.Serializable;

public class Omsorg implements Serializable {
    private static final long serialVersionUID = -7616646189361324702L;

    private Long omsorgId;

    private Pid fnr;

    private Pid fnrOmsorgFor;

    private String omsorgType;

    private String kilde;

    private Integer ar;

    private ChangeStamp changeStamp;

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
    @JsonIgnore
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
     * @return the omsorgId
     */
    public Long getOmsorgId() {
        return omsorgId;
    }

    /**
     * @param omsorgId the omsorgId to set
     */
    public void setOmsorgId(Long omsorgId) {
        this.omsorgId = omsorgId;
    }

    /**
     * @return the omsorgType
     */
    public String getOmsorgType() {
        return omsorgType;
    }

    /**
     * @param omsorgType the omsorgType to set
     */
    public void setOmsorgType(String omsorgType) {
        this.omsorgType = omsorgType;
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
