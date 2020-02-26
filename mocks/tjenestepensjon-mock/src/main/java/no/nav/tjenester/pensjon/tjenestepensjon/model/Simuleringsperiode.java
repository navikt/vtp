package no.nav.tjenester.pensjon.tjenestepensjon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

import java.time.LocalDate;
import java.util.List;

@ApiModel()
@JsonIgnoreProperties(ignoreUnknown = true)
public class Simuleringsperiode {

    @JsonProperty(value = "datoFom", required = true)
    private LocalDate datoFom;

    @JsonProperty(required = true)
    private int utg;

    @JsonProperty(required = true)
    private int stillingsprosentOffentlig;

    @JsonProperty(required = true)
    private int poengArTom1991;

    @JsonProperty(required = true)
    private int poengArFom1992;

    @JsonProperty(required = true)
    private Double sluttpoengtall;

    @JsonProperty(required = true)
    private int anvendtTrygdetid;

    @JsonProperty(required = true)
    private Double forholdstall;

    @JsonProperty(required = true)
    private Double delingstall;

    @JsonProperty(required = true)
    private int uforegradVedOmregning;

    @JsonProperty(required = true)
    private List<Delytelse> delytelser;

    public LocalDate getDatoFom() {
        return datoFom;
    }

    public void setDatoFom(LocalDate datoFom) {
        this.datoFom = datoFom;
    }

    public int getUtg() {
        return utg;
    }

    public void setUtg(int utg) {
        this.utg = utg;
    }

    public int getStillingsprosentOffentlig() {
        return stillingsprosentOffentlig;
    }

    public void setStillingsprosentOffentlig(int stillingsprosentOffentlig) {
        this.stillingsprosentOffentlig = stillingsprosentOffentlig;
    }

    public int getPoengArTom1991() {
        return poengArTom1991;
    }

    public void setPoengArTom1991(int poengArTom1991) {
        this.poengArTom1991 = poengArTom1991;
    }

    public int getPoengArFom1992() {
        return poengArFom1992;
    }

    public void setPoengArFom1992(int poengArFom1992) {
        this.poengArFom1992 = poengArFom1992;
    }

    public Double getSluttpoengtall() {
        return sluttpoengtall;
    }

    public void setSluttpoengtall(Double sluttpoengtall) {
        this.sluttpoengtall = sluttpoengtall;
    }

    public int getAnvendtTrygdetid() {
        return anvendtTrygdetid;
    }

    public void setAnvendtTrygdetid(int anvendtTrygdetid) {
        this.anvendtTrygdetid = anvendtTrygdetid;
    }

    public Double getForholdstall() {
        return forholdstall;
    }

    public void setForholdstall(Double forholdstall) {
        this.forholdstall = forholdstall;
    }

    public Double getDelingstall() {
        return delingstall;
    }

    public void setDelingstall(Double delingstall) {
        this.delingstall = delingstall;
    }

    public int getUforegradVedOmregning() {
        return uforegradVedOmregning;
    }

    public void setUforegradVedOmregning(int uforegradVedOmregning) {
        this.uforegradVedOmregning = uforegradVedOmregning;
    }

    public List<Delytelse> getDelytelser() {
        return delytelser;
    }

    public void setDelytelser(List<Delytelse> delytelser) {
        this.delytelser = delytelser;
    }
}
