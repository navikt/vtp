package no.nav.domain;

import no.stelvio.domain.person.Pid;
import no.stelvio.domain.time.ChangeStamp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Forstegangstjeneste implements Serializable {
    private static final long serialVersionUID = -3682514745110077173L;

    private Long forstegangstjenesteId;

    private Pid fnr;

    private List<ForstegangstjenestePeriode> forstegangstjenestePeriodeListe;

    private ChangeStamp changeStamp;

    private String kilde;

    private String rapportType;

    private Date tjenestestartDato;

    private Date dimitteringDato;

    public Forstegangstjeneste() {
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
     * @return the forstegangstjenesteId
     */
    public Long getForstegangstjenesteId() {
        return forstegangstjenesteId;
    }

    /**
     * @param forstegangstjenesteId the forstegangstjenesteId to set
     */
    public void setForstegangstjenesteId(Long forstegangstjenesteId) {
        this.forstegangstjenesteId = forstegangstjenesteId;
    }

    /**
     * @return the forstegangstjenestePeriodeListe
     */
    public List<ForstegangstjenestePeriode> getForstegangstjenestePeriodeListe() {
        return forstegangstjenestePeriodeListe;
    }

    /**
     * @param forstegangstjenestePeriodeListe the forstegangstjenestePeriodeListe to set
     */
    public void setForstegangstjenestePeriodeListe(List<ForstegangstjenestePeriode> forstegangstjenestePeriodeListe) {
        this.forstegangstjenestePeriodeListe = forstegangstjenestePeriodeListe;
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
     * @return the tjenestestartDato
     */
    public Date getTjenestestartDato() {
        return tjenestestartDato;
    }

    /**
     * @param tjenestestartDato the tjenestestartDato to set
     */
    public void setTjenestestartDato(Date tjenestestartDato) {
        this.tjenestestartDato = tjenestestartDato;
    }

    /**
     * @return the dimitteringDato
     */
    public Date getDimitteringDato() {
        return dimitteringDato;
    }

    /**
     * @param dimitteringDato the dimitteringDato to set
     */
    public void setDimitteringDato(Date dimitteringDato) {
        this.dimitteringDato = dimitteringDato;
    }
}
