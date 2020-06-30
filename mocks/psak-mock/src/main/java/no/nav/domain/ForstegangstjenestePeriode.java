package no.nav.domain;

import no.stelvio.domain.time.ChangeStamp;

import java.io.Serializable;
import java.util.Date;

public class ForstegangstjenestePeriode implements Serializable {
    private static final long serialVersionUID = 547620561116301736L;

    private Long forstegangstjenestePeriodeId;
    private String periodeType;
    private String tjenesteType;
    private Date fomDato;
    private Date tomDato;
    private ChangeStamp changeStamp;

    public ForstegangstjenestePeriode() {
    }

    /**
     * @return the forstegangstjenestePeriodeId
     */
    public Long getForstegangstjenestePeriodeId() {
        return forstegangstjenestePeriodeId;
    }

    /**
     * @param forstegangstjenestePeriodeId the forstegangstjenestePeriodeId to set
     */
    public void setForstegangstjenestePeriodeId(Long forstegangstjenestePeriodeId) {
        this.forstegangstjenestePeriodeId = forstegangstjenestePeriodeId;
    }

    /**
     * @return the periodeType
     */
    public String getPeriodeType() {
        return periodeType;
    }

    /**
     * @param periodeType the periodeType to set
     */
    public void setPeriodeType(String periodeType) {
        this.periodeType = periodeType;
    }

    /**
     * @return the tjenesteType
     */
    public String getTjenesteType() {
        return tjenesteType;
    }

    /**
     * @param tjenesteType the tjenesteType to set
     */
    public void setTjenesteType(String tjenesteType) {
        this.tjenesteType = tjenesteType;
    }

    /**
     * @return the fomDato
     */
    public Date getFomDato() {
        return fomDato;
    }

    /**
     * @param fomDato the fomDato to set
     */
    public void setFomDato(Date fomDato) {
        this.fomDato = fomDato;
    }

    /**
     * @return the tomDato
     */
    public Date getTomDato() {
        return tomDato;
    }

    /**
     * @param tomDato the tomDato to set
     */
    public void setTomDato(Date tomDato) {
        this.tomDato = tomDato;
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
}
