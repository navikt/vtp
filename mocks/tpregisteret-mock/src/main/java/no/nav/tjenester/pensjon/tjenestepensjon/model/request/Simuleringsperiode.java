package no.nav.tjenester.pensjon.tjenestepensjon.model.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

import java.time.LocalDate;
import java.util.List;

@ApiModel()
@JsonIgnoreProperties(ignoreUnknown = true)
public class Simuleringsperiode {

    private LocalDate datoFom;
    private int utg;
    private int stillingsprosentOffentlig;
    private int poengArTom1991;
    private int poengArFom1992;
    private Double sluttpoengtall;
    private int anvendtTrygdetid;
    private Double forholdstall;
    private Double delingstall;
    private int uforegradVedOmregning;
    private List<Delytelse> delytelser;

    @JsonCreator
    public Simuleringsperiode(
            @JsonProperty(value = "datoFom", required = true) LocalDate datoFom,
            @JsonProperty(value = "utg", required = true) int utg,
            @JsonProperty(value = "stillingsprosentOffentlig", required = true) int stillingsprosentOffentlig,
            @JsonProperty(value = "poengArTom1991", required = true) int poengArTom1991,
            @JsonProperty(value = "poengArFom1992", required = true) int poengArFom1992,
            @JsonProperty(value = "sluttpoengtall", required = true) Double sluttpoengtall,
            @JsonProperty(value = "anvendtTrygdetid", required = true) int anvendtTrygdetid,
            @JsonProperty(value = "forholdstall", required = true) Double forholdstall,
            @JsonProperty(value = "delingstall", required = true) Double delingstall,
            @JsonProperty(value = "uforegradVedOmregning", required = true) int uforegradVedOmregning,
            @JsonProperty(value = "delytelser", required = true) List<Delytelse> delytelser
    ) {
        this.datoFom = datoFom;
        this.utg = utg;
        this.stillingsprosentOffentlig = stillingsprosentOffentlig;
        this.poengArTom1991 = poengArTom1991;
        this.poengArFom1992 = poengArFom1992;
        this.sluttpoengtall = sluttpoengtall;
        this.anvendtTrygdetid = anvendtTrygdetid;
        this.forholdstall = forholdstall;
        this.delingstall = delingstall;
        this.uforegradVedOmregning = uforegradVedOmregning;
        this.delytelser = delytelser;
    }

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
